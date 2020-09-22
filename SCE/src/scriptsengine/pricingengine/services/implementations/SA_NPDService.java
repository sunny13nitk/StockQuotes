package scriptsengine.pricingengine.services.implementations;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import scriptsengine.constants.SheetNamesConstants;
import scriptsengine.pojos.OB_Scrip_QuartersData;
import scriptsengine.pricingengine.definitions.TY_NPSD;
import scriptsengine.pricingengine.services.interfaces.ISA_NPDService;
import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripDataContainerSrv;
import scriptsengine.utilities.implementations.DeltaCalcService;
import scriptsengine.utilities.types.PenultimateQYear;

/**
 * Nett. Profit Delta Service for a Scrip to compute the % difference in nett. Profit of the Scrip for Penultimate
 * Quarter of Current Year and the same duration of the last year
 * Application Context Scoped Service
 */
@Service
public class SA_NPDService implements ISA_NPDService, ApplicationContextAware
{

	@Autowired
	private DeltaCalcService			deltaCalcSrv;
	private ApplicationContext		appCtxt;
	private IScripDataContainerSrv	scDataConSrv;

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{

		this.appCtxt = ctxt;
	}

	@SuppressWarnings(
	{ "rawtypes", "unchecked"
	})
	@Override
	public TY_NPSD getNettProfitDeltaforScripCode(String scCode) throws EX_General
	{

		TY_NPSD nettProfitDelta = new TY_NPSD();

		PenultimateQYear qyearTo_beforeScan = new PenultimateQYear();
		PenultimateQYear qyearFrom;

		if (scCode != null)
		{
			if (appCtxt != null)
			{
				if (scDataConSrv == null)
				{
					scDataConSrv = appCtxt.getBean(IScripDataContainerSrv.class);

				}
				if (scDataConSrv != null)
				{
					ScripDataContainer scDataCon = scDataConSrv.getScripDetailsfromDB(scCode);
					if (scDataCon != null)
					{
						SheetEntities SheetEntities = scDataCon.getEntitiesforSheet(SheetNamesConstants.QuarterlyDataSheet);
						if (SheetEntities != null)
						{
							ArrayList<OB_Scrip_QuartersData> qtrEntities = SheetEntities.getSheetEntityList();
							if (qtrEntities != null)
							{
								// At Least 2 entities for Comparison
								if (qtrEntities.size() > 1)
								{
									/**
									 * Evaluate Year and Quarter as per Current Quarter Entries maintained in
									 * DB
									 */
									PenultimateQYear qyearTo = evaluate_PenultimateQYear(qyearTo_beforeScan, qtrEntities);
									nettProfitDelta.setQyearTo(qyearTo);

									// Prepare PenultimateQYEar Instance From
									qyearFrom = new PenultimateQYear((qyearTo.getYear() - 1), qyearTo.getQuarter());
									nettProfitDelta.setQyearFrom(qyearFrom);

									/**
									 * Calcualte Nett Profit Summed Cumulatively for From Year
									 */
									double nettProfitfromYear = qtrEntities.stream()
									          .filter(x -> x.getYear() == qyearFrom.getYear() && x.getQuarter() <= qyearFrom.getQuarter())
									          .mapToDouble(OB_Scrip_QuartersData::getNettProfit).sum();
									nettProfitDelta.setNettProfit_Penultimate(nettProfitfromYear);

									/**
									 * Calcualte Nett Sales Summed Cumulatively for From Year
									 */
									double nettSalesfromYear = qtrEntities.stream()
									          .filter(x -> x.getYear() == qyearFrom.getYear() && x.getQuarter() <= qyearFrom.getQuarter())
									          .mapToDouble(OB_Scrip_QuartersData::getNettSales).sum();
									nettProfitDelta.setNettSales_Penultimate(nettProfitfromYear);

									/**
									 * Calcualte Nett Profit Summed Cumulatively for To Year
									 */
									double nettProfittoYear = qtrEntities.stream()
									          .filter(x -> x.getYear() == qyearTo.getYear() && x.getQuarter() <= qyearTo.getQuarter())
									          .mapToDouble(OB_Scrip_QuartersData::getNettProfit).sum();

									nettProfitDelta.setNettProfit_Current(nettProfittoYear);

									/**
									 * Calcualte Nett Sales Summed Cumulatively for To Year
									 */
									double nettSalestoYear = qtrEntities.stream()
									          .filter(x -> x.getYear() == qyearTo.getYear() && x.getQuarter() <= qyearTo.getQuarter())
									          .mapToDouble(OB_Scrip_QuartersData::getNettSales).sum();

									nettProfitDelta.setNettSales_Current(nettSalestoYear);

									/**
									 * Calculate the Delta Percentage
									 */
									if (deltaCalcSrv != null)
									{
										double npdDeltaPer = deltaCalcSrv.getDeltaPercentage(nettProfitfromYear, nettProfittoYear);
										nettProfitDelta.setNPD(npdDeltaPer);

										double nsdDeltaPer = deltaCalcSrv.getDeltaPercentage(nettSalesfromYear, nettSalestoYear);
										nettProfitDelta.setNSD(nsdDeltaPer);

									}

								}
							}

						}

					}
				}
			}
		}

		return nettProfitDelta;

	}

	private PenultimateQYear evaluate_PenultimateQYear(PenultimateQYear pQyear, ArrayList<OB_Scrip_QuartersData> qtrEntList)
	{
		int year = 0, qtr = 0;
		OB_Scrip_QuartersData spYearQData = null;

		if (pQyear.getYear() != 0 && qtrEntList != null)
		{
			// First check if Quarterly Data exists for the To Year (e.g Current Year 1st quarter data is not yet
			// maintained and current month is April as first quarter results are not yet out

			try
			{
				spYearQData = qtrEntList.stream().filter(x -> x.getYear() == pQyear.getYear()).findFirst().get();
				year = pQyear.getYear();
			}

			catch (NoSuchElementException e)
			{
				// Data Not found for the Target Year can be case of April this year where 1st Quarter data of
				// current year is yet not maintianed for Scrip
				// Reduce the year by 1 in this case

				// No Try in this case let it dump if even the penultimate year data is not maintianed for a single
				// quarter
				spYearQData = qtrEntList.stream().filter(x -> x.getYear() == pQyear.getYear() - 1).findFirst().get();
				year = pQyear.getYear() - 1;

			}

			if (spYearQData != null)
			{
				int yComp = year;

				// get the max quarter for the ToQyear before Scan
				qtr = qtrEntList.stream().filter(x -> x.getYear() == yComp).mapToInt(OB_Scrip_QuartersData::getQuarter).max().getAsInt();

				pQyear.setYear(yComp);
				pQyear.setQuarter(qtr);

			}

		}

		return pQyear;
	}

}
