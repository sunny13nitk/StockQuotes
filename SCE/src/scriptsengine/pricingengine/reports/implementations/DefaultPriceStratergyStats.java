package scriptsengine.pricingengine.reports.implementations;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.constants.PriceProjectionFactorsConstants;
import scriptsengine.constants.SheetNamesConstants;
import scriptsengine.pojos.OB_Scrip_BalSheet;
import scriptsengine.pojos.OB_Scrip_QuartersData;
import scriptsengine.pricingengine.reports.definitions.TY_DefaultPriceForecast;
import scriptsengine.pricingengine.reports.definitions.TY_ReportPPS_Default;
import scriptsengine.pricingengine.reports.interfaces.IDefaultPriceStratergyStats;
import scriptsengine.pricingengine.services.interfaces.ISA_ScripPriceProjectionService;
import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripDataContainerSrv;
import scriptsengine.utilities.types.PenultimateQYear;

/**
 * 
 * Service for Default Pricing Stratergy Statistics
 * Since this statistics involves multiple data types and computations it is created as a separate Service
 * Need to provide Pricing projection Stratergy Interface BEan Instance
 */

@Service
public class DefaultPriceStratergyStats implements IDefaultPriceStratergyStats
{

	@Autowired
	private IScripDataContainerSrv	scContSrv;

	@Autowired
	private MessagesFormatter		msgFormatter;

	@SuppressWarnings(
	{ "rawtypes", "unchecked"
	})
	@Override
	public TY_ReportPPS_Default getDefaultPriceProjection_Stats(ISA_ScripPriceProjectionService ppsrv) throws EX_General
	{
		TY_ReportPPS_Default defStats = new TY_ReportPPS_Default();
		ScripDataContainer scDataCont = null;

		if (ppsrv != null && scContSrv != null)
		{

			/**
			 * Projected Prices Calculation should have been completed to get detailed statistics
			 */
			if (ppsrv.getProjectedPrices().size() > 0)
			{

				scDataCont = scContSrv.getScripDetailsfromDB(ppsrv.getScCode());
				if (scDataCont != null)
				{
					/**
					 * Get BalanceSheet entities and Start looping to form Stats
					 */
					defStats.setPfItems(getpriceForecastItems(scDataCont, ppsrv));

					/**
					 * Get Quarters Data
					 */

					if (ppsrv.getNpd() != null)
					{
						// to Year
						defStats.setCurrentYdata(getQuartersDataforYearQuarter(scDataCont, ppsrv.getNpd().getQyearTo()));
						// from Year
						defStats.setPenultimateYdata(getQuartersDataforYearQuarter(scDataCont, ppsrv.getNpd().getQyearFrom()));
					}

					try
					{
						defStats.setPricesReturn(ppsrv.getProjectedPrices().stream()
						          .filter(x -> x.getPriceType().equals(PriceProjectionFactorsConstants.DefaultPricing)).findFirst().get());
					}
					catch (Exception e)
					{
						EX_General msgChgErr = new EX_General("ERRPPSTATS_NODEF", new Object[]
						{ ppsrv.getScCode()
						});
						msgFormatter.generate_message_snippet(msgChgErr);
						throw msgChgErr;
					}

				}
			}
			else
			{
				EX_General msgChgErr = new EX_General("ERRPPSTATS_DEF", new Object[]
				{ ppsrv.getScCode()
				});
				msgFormatter.generate_message_snippet(msgChgErr);
				throw msgChgErr;
			}
		}

		return defStats;
	}

	@SuppressWarnings(
	{ "rawtypes", "unchecked"
	})
	private ArrayList<TY_DefaultPriceForecast> getpriceForecastItems(ScripDataContainer scDataCont, ISA_ScripPriceProjectionService ppsrv)
	{
		ArrayList<TY_DefaultPriceForecast> pfItems = new ArrayList<TY_DefaultPriceForecast>();
		ArrayList<OB_Scrip_BalSheet> balshEntities = null;
		SheetEntities SheetEntities = scDataCont.getEntitiesforSheet(SheetNamesConstants.BalanceSheet);
		if (SheetEntities != null)
		{
			balshEntities = SheetEntities.getSheetEntityList();
			if (balshEntities != null && ppsrv.getAvgPE().getAdjustedPEList() != null)
			{
				if (ppsrv.getAvgPE().getAdjustedPEList().size() > 0)
				{

					/**
					 * First determine Minimum face value for Face value optimization
					 */
					double minFV = balshEntities.stream().mapToDouble(OB_Scrip_BalSheet::getFaceValue).min().getAsDouble();

					for ( OB_Scrip_BalSheet ob_Scrip_BalSheet : balshEntities )
					{
						TY_DefaultPriceForecast pfItem = new TY_DefaultPriceForecast();

						pfItem.setYear(ob_Scrip_BalSheet.getyear());
						pfItem.setPrice(ob_Scrip_BalSheet.getavgPrice());
						pfItem.setNettProfit(ob_Scrip_BalSheet.getNettProfit());
						pfItem.setEPS(ob_Scrip_BalSheet.getEPS());
						pfItem.setFaceValue(ob_Scrip_BalSheet.getFaceValue());

						// --------------------EPS Unit Value - EUV = EPS/Face Value----------------------------
						pfItem.setEUV(pfItem.getEPS() / pfItem.getFaceValue());

						// ---------------ENPR = (EUV/Nett Profit) * 100 ---------------------------------------
						pfItem.setENPR((pfItem.getEUV() / pfItem.getNettProfit()) * 100);

						// --------------Face Value Ratio - FVR -------------------------------------------------
						pfItem.setLineFVR(pfItem.getFaceValue() / minFV);
						// --------------------- Eff. EPS = EPS/FVR ----------------------------------------------
						pfItem.setEffEPS(pfItem.getEPS() / pfItem.getLineFVR());
						// ----------------------- Price/Eff. EPS ------------------------------------------------
						pfItem.setEffPE(pfItem.getPrice() / pfItem.getEffEPS());
						// ----------------------- Adjusted PE ------------------------------------------------
						pfItem.setAdjPE(ppsrv.getAvgPE().getAdjustedPEList().stream().filter(x -> x.getYear() == ob_Scrip_BalSheet.getyear())
						          .findFirst().get().getPE());

						// Add to Collection
						pfItems.add(pfItem);
					}
				}
			}
		}

		return pfItems;
	}

	private ArrayList<OB_Scrip_QuartersData> getQuartersDataforYearQuarter(ScripDataContainer scDataCont, PenultimateQYear pQyear)
	{
		ArrayList<OB_Scrip_QuartersData> qtrEntitiesRet = null;
		ArrayList<OB_Scrip_QuartersData> qtrEntSorted = new ArrayList<OB_Scrip_QuartersData>();

		if (scDataCont != null && pQyear != null)
		{
			SheetEntities SheetEntities = scDataCont.getEntitiesforSheet(SheetNamesConstants.QuarterlyDataSheet);
			if (SheetEntities != null)
			{
				ArrayList<OB_Scrip_QuartersData> qtrEntities = SheetEntities.getSheetEntityList();
				if (qtrEntities != null)
				{
					qtrEntitiesRet = qtrEntities.stream().filter(x -> x.getYear() == pQyear.getYear() && x.getQuarter() <= pQyear.getQuarter())
					          .collect(Collectors.toCollection(ArrayList::new));
				}
			}
		}

		qtrEntitiesRet.stream().sorted((e2, e1) -> Integer.compare(e1.getQuarter(), e2.getQuarter())).forEach(e -> qtrEntSorted.add(e));

		return qtrEntSorted;

	}

}
