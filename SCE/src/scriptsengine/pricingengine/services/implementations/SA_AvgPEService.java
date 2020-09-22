package scriptsengine.pricingengine.services.implementations;

import java.util.ArrayList;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import scriptsengine.constants.SheetNamesConstants;
import scriptsengine.enums.SCEenums.direction;
import scriptsengine.pojos.OB_Scrip_BalSheet;
import scriptsengine.pricingengine.definitions.TY_AllowancesBean;
import scriptsengine.pricingengine.definitions.TY_AvgPE;
import scriptsengine.pricingengine.definitions.TY_PE;
import scriptsengine.pricingengine.definitions.TY_PEWeights;
import scriptsengine.pricingengine.services.interfaces.ISA_AvgPEService;
import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripDataContainerSrv;
import scriptsengine.utilities.implementations.DeltaCalcService;

/**
 * Adjusted Average PE Service within Allowances- To Calcaulate Average PE for a Scrip
 * Application Context Scoped Service
 */
@Service
public class SA_AvgPEService implements ISA_AvgPEService, ApplicationContextAware
{

	@Autowired
	private TY_AllowancesBean		allowancesBean;
	@Autowired
	private DeltaCalcService			deltaCalcService;
	private ApplicationContext		appCtxt;
	private IScripDataContainerSrv	scDataConSrv;

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		this.appCtxt = ctxt;

	}

	@SuppressWarnings("rawtypes")
	@Override
	public TY_AvgPE getPERatiosforScripCode(String scCode) throws EX_General
	{
		TY_AvgPE avgPE = new TY_AvgPE();

		ArrayList<TY_PE> peList = new ArrayList<TY_PE>();
		ArrayList<TY_PE> peadjList = new ArrayList<TY_PE>();
		ArrayList<TY_PE> peadjListSorted = new ArrayList<TY_PE>();
		if (scCode != null)
		{
			if (appCtxt != null)
			{
				if (scDataConSrv == null)
				{
					scDataConSrv = appCtxt.getBean(IScripDataContainerSrv.class);
				}
				if (scDataConSrv != null && allowancesBean != null)
				{
					ScripDataContainer scDataCon = scDataConSrv.getScripDetailsfromDB(scCode);
					if (scDataCon != null)
					{
						SheetEntities SheetEntities = scDataCon.getEntitiesforSheet(SheetNamesConstants.BalanceSheet);
						if (SheetEntities != null)
						{
							ArrayList<OB_Scrip_BalSheet> balShEntities = SheetEntities.getSheetEntityList();
							if (balShEntities != null)
							{
								/**
								 * Get the minimum Face Value
								 */
								double minFVal = balShEntities.stream().mapToDouble(OB_Scrip_BalSheet::getFaceValue).min().getAsDouble();

								if (balShEntities.size() > 0)
								{
									ArrayList<OB_Scrip_BalSheet> balShEntities_SorteddescByYear = new ArrayList<OB_Scrip_BalSheet>();
									// Sort Balance Sheet by Year Descending
									balShEntities.stream().sorted((e2, e1) -> Integer.compare(e1.getyear(), e2.getyear()))
									          .forEach(e -> balShEntities_SorteddescByYear.add(e));
									for ( OB_Scrip_BalSheet ob_Scrip_BalSheet : balShEntities_SorteddescByYear )
									{

										if (ob_Scrip_BalSheet.getavgPrice() > 0 && ob_Scrip_BalSheet.getEPS() != 0)
										{

											double effEPS = ob_Scrip_BalSheet.getEPS() / (ob_Scrip_BalSheet.getFaceValue() / minFVal);
											TY_PE objPE = new TY_PE(ob_Scrip_BalSheet.getyear(),
											          (ob_Scrip_BalSheet.getavgPrice() / effEPS));
											peList.add(objPE);
										}

									}
								}

								peadjList = adjustPE(peList);
								peadjList.stream().sorted((e2, e1) -> Integer.compare(e1.getYear(), e2.getYear()))
								          .forEach(e -> peadjListSorted.add(e));

								double avg = 0, max = 0, min = 0, deltaminavg = 0, deltaavgmax = 0, avg_unadjusted = 0;

								if (balShEntities.size() <= allowancesBean.getPEWeight_NYearsBaseline())
								{
									avg_unadjusted = peList.stream().mapToDouble(TY_PE::getPE).average().getAsDouble();
									avgPE.setAvgPE_unadjusted(avg_unadjusted);
									avg = peadjList.stream().mapToDouble(TY_PE::getPE).average().getAsDouble();
									max = peadjList.stream().mapToDouble(TY_PE::getPE).max().getAsDouble();
									min = peadjList.stream().mapToDouble(TY_PE::getPE).min().getAsDouble();

								}
								else // PE adjustment to give more weightage to recent years PE as per
								     // allowances bean configuration
								{
									TY_PEWeights peWeights = computeWeights(peList);
									avg_unadjusted = this.getAveragePEfactoringWeights(peWeights, peList);
									avgPE.setAvgPE_unadjusted(avg_unadjusted);
									avg = this.getAveragePEfactoringWeights(peWeights, peadjListSorted);
									max = peadjList.stream().mapToDouble(TY_PE::getPE).max().getAsDouble();
									min = peadjList.stream().mapToDouble(TY_PE::getPE).min().getAsDouble();

								}

								if (deltaCalcService != null && min != 0 && max != 0 && avg != 0)
								{
									deltaminavg = deltaCalcService.getDeltaPercentage(min, avg);
									if (deltaminavg > allowancesBean.getVarPE_allowance())
									{
										/**
										 * Minimum PE should be Average PE in case mini PE differs from
										 * Avg.
										 * PE by more than allowance% as the PE re-rating of stock has
										 * happened
										 */

										min = deltaCalcService.adjustNumberbyPercentage(avg, allowancesBean.getFosPE_allowance(),
										          scriptsengine.enums.SCEenums.direction.DECREASE);
										avgPE.setPeAdjusted(true);
									}

									deltaavgmax = deltaCalcService.getDeltaPercentage(avg, max);
									if (deltaavgmax > allowancesBean.getVarPE_allowance())
									{
										// Max PE evn in case of a re-rating should be the average of the
										// max
										// and average PE for realistic PE re-rating
										// max = avg + max
										max = deltaCalcService.adjustNumberbyPercentage(avg, allowancesBean.getVarPE_allowance(),
										          scriptsengine.enums.SCEenums.direction.INCREASE);
										avgPE.setPeAdjusted(true);
									}

								}

								// Get the Average PE
								avgPE.setAvgPE(avg);
								// Get Min'm PE
								avgPE.setMinPE(min);
								// Get Max'm PE
								avgPE.setMaxPE(max);

								// Set Percentage scope for Adjusted PE
								avgPE.setPeadj_percentage(deltaCalcService.getDeltaPercentage(avg, avg_unadjusted));

								// Set Adjusted PE List
								avgPE.setAdjustedPEList(peadjListSorted);
							}
						}
					}
				}
			}
		}

		return avgPE;
	}

	/*
	 * ------------------------------------ PRIVATE SECTION -------------------------------------
	 */

	/**
	 * Perform PE Adjustments for too high PE in consecutive years
	 * 
	 * @param peList
	 * @return
	 */
	private ArrayList<TY_PE> adjustPE(ArrayList<TY_PE> peList)
	{
		ArrayList<TY_PE> adjpeList = new ArrayList<TY_PE>();
		if (peList != null && allowancesBean != null)
		{
			if (peList.size() >= 2 && allowancesBean.getAdjPE_allowance() > 0)
			{
				// 1. Get Min'm year
				int minYear = peList.stream().mapToInt(TY_PE::getYear).min().getAsInt();
				int maxYear = peList.stream().mapToInt(TY_PE::getYear).max().getAsInt();

				// 2. Add first entity as Minimum year with the same adjusted PE as effective PE
				TY_PE adjpeEnt = new TY_PE(minYear, peList.stream().filter(x -> x.getYear() == minYear).findFirst().get().getPE());
				adjpeList.add(adjpeEnt);

				// 2. Process for rest of entities
				for ( int cyear = minYear + 1; cyear <= maxYear; cyear++ )
				{
					int qyear = cyear;
					double cyearPE, lyearPE, lyearPE_allow = 0;

					// Get Cyear PE
					cyearPE = peList.stream().filter(x -> x.getYear() == qyear).findFirst().get().getPE();

					// Get Last's year PE
					lyearPE = peList.stream().filter(x -> x.getYear() == qyear - 1).findFirst().get().getPE();

					// Get Last year PE Increased by alowance customized
					lyearPE_allow = deltaCalcService.adjustNumberbyPercentage(lyearPE, allowancesBean.getAdjPE_allowance(), direction.INCREASE);

					// Perform Comparison in PE
					if (cyearPE > lyearPE_allow)
					{
						// Exorbitant Increase - Set Adjusted PE for Evaluation Year as per allowance
						adjpeEnt = new TY_PE(cyear, lyearPE_allow);
						adjpeList.add(adjpeEnt);
					}
					else
					{
						// Nominal Increase
						adjpeEnt = new TY_PE(cyear, cyearPE);
						adjpeList.add(adjpeEnt);
					}

				}

			}
		}

		return adjpeList;

	}

	private TY_PEWeights computeWeights(ArrayList<TY_PE> peList)
	{
		TY_PEWeights peWeights = new TY_PEWeights();

		int size = peList.size();
		double perunitWt = 100 / size;

		double baselinePerAdjusted = deltaCalcService.adjustNumberbyPercentage((allowancesBean.getPEWeight_NYearsBaseline() * perunitWt),
		          allowancesBean.getLastNYrsPE_BoostFactor(), direction.INCREASE);
		if (baselinePerAdjusted < allowancesBean.getLastNYrsPE_minContr())
		{
			baselinePerAdjusted = allowancesBean.getLastNYrsPE_minContr();
		}

		peWeights.setLastNYrs_Wght(baselinePerAdjusted);
		peWeights.setRestYrs_Wght(100 - peWeights.getLastNYrs_Wght());

		return peWeights;

	}

	private double getAveragePEfactoringWeights(TY_PEWeights peWeights, ArrayList<TY_PE> peList)
	{
		double avg = 0;

		double avgLastN = 0;
		double avgRest = 0;

		int restCount = peList.size() - allowancesBean.getPEWeight_NYearsBaseline();

		for ( int x = 0; x < peList.size(); x++ )
		{

			if ((x + 1) <= allowancesBean.getPEWeight_NYearsBaseline())
			{
				avgLastN += peList.get(x).getPE();

				if ((x + 1) == allowancesBean.getPEWeight_NYearsBaseline())
				{
					avgLastN = avgLastN / allowancesBean.getPEWeight_NYearsBaseline();
				}
			}
			else
			{
				avgRest += peList.get(x).getPE();
			}

		}

		avgRest = avgRest / restCount;

		avg = avgLastN * (peWeights.getLastNYrs_Wght() / 100) + avgRest * (peWeights.getRestYrs_Wght() / 100);

		return avg;
	}

}
