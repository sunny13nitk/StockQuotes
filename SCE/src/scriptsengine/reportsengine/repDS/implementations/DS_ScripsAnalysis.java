package scriptsengine.reportsengine.repDS.implementations;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import scriptsengine.enums.SCEenums.alertMode;
import scriptsengine.enums.SCEenums.direction;
import scriptsengine.reportsengine.repDS.annotations.AlertSubscribeTrendDelta;
import scriptsengine.reportsengine.repDS.annotations.AlertSubscribeTrendPenultimate;
import scriptsengine.reportsengine.repDS.annotations.ReportRefFieldName;
import scriptsengine.reportsengine.repDS.definitions.TY_ScRoot_AttrContainers;
import scriptsengine.reportsengine.repDS.interfaces.IReportDataSource;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Scrip BalanceSheet Deltas Data Source - Context specific Cache Supported Bean
 */
@Service("DS_ScripsAnalysis")
public class DS_ScripsAnalysis implements IReportDataSource
{

	@ReportRefFieldName(attrName = "Nett. Sales")
	@AlertSubscribeTrendDelta(Occurances = 3, TrendDirection = direction.DECREASE, valueToCompare = 10, msgID = "AL_NETTSALES", isMsgGeneric = false)
	public final String	NettSalesLabel		= "CAGR Sales";

	@ReportRefFieldName(attrName = "Nett. Profit")
	@AlertSubscribeTrendDelta(Occurances = 2, TrendDirection = direction.DECREASE, valueToCompare = 10, msgID = "AL_NETTPRTREND", isMsgGeneric = false)
	@AlertSubscribeTrendDelta(alertMode = alertMode.FAVOR, Occurances = 2, TrendDirection = direction.INCREASE, valueToCompare = 15)
	@AlertSubscribeTrendPenultimate(TrendDirection = direction.DECREASE, valueToCompare = 10)
	public final String	NettProfitLabel	= "CAGR Profit";

	@ReportRefFieldName(attrName = "Effective EPS")
	@AlertSubscribeTrendPenultimate(TrendDirection = direction.DECREASE, valueToCompare = 10)
	@AlertSubscribeTrendDelta(TrendDirection = direction.DECREASE, valueToCompare = 20, Occurances = 2)
	@AlertSubscribeTrendDelta(alertMode = alertMode.FAVOR, Occurances = 3, TrendDirection = direction.INCREASE, valueToCompare = 20)
	public final String	effEPS			= "CAGR EPS";

	@ReportRefFieldName(attrName = "Nett. Profit Margin")
	@AlertSubscribeTrendDelta(Occurances = 2, TrendDirection = direction.DECREASE, valueToCompare = 20)
	@AlertSubscribeTrendDelta(Occurances = 3, TrendDirection = direction.INCREASE, valueToCompare = 20, alertMode = alertMode.FAVOR)
	@AlertSubscribeTrendPenultimate(TrendDirection = direction.DECREASE, valueToCompare = 30)
	public final String	NPM				= "CAGR Net. Profit Margin";

	@ReportRefFieldName(attrName = "Inventory Turnover Ratio")
	@AlertSubscribeTrendDelta(Occurances = 3, TrendDirection = direction.DECREASE, valueToCompare = 20)
	@AlertSubscribeTrendDelta(Occurances = 3, TrendDirection = direction.INCREASE, valueToCompare = 20, alertMode = alertMode.FAVOR)
	@AlertSubscribeTrendPenultimate(TrendDirection = direction.DECREASE, valueToCompare = 25)
	public final String	ITR				= "CAGR Inventory Turnover Ratio";

	@ReportRefFieldName(attrName = "Debt Equity Ratio")
	@AlertSubscribeTrendDelta(Occurances = 2, TrendDirection = direction.DECREASE, valueToCompare = 20)
	@AlertSubscribeTrendDelta(Occurances = 3, TrendDirection = direction.INCREASE, valueToCompare = 15, alertMode = alertMode.FAVOR)
	@AlertSubscribeTrendPenultimate(TrendDirection = direction.DECREASE, valueToCompare = 20)
	public final String	DER				= "CAGR Debt Equity Ratio";

	@ReportRefFieldName(attrName = "Total Debt")
	@AlertSubscribeTrendDelta(Occurances = 3, TrendDirection = direction.INCREASE, valueToCompare = 20, msgID = "AL_TOTALDEBT", isMsgGeneric = false)
	@AlertSubscribeTrendDelta(Occurances = 3, TrendDirection = direction.DECREASE, valueToCompare = 10, alertMode = alertMode.FAVOR)
	@AlertSubscribeTrendPenultimate(TrendDirection = direction.INCREASE, valueToCompare = 30, msgID = "AL_NETTPRCURR", isMsgGeneric = false)
	public final String	NettDebtLabel		= "CAGR Debt";

	@ReportRefFieldName(attrName = "Capex Borrowings Ratio")
	@AlertSubscribeTrendDelta(Occurances = 2, TrendDirection = direction.DECREASE, valueToCompare = 10)
	@AlertSubscribeTrendPenultimate(TrendDirection = direction.DECREASE, valueToCompare = 15)
	public final String	CapexBwRatio		= "CAGR Capex Borrowings";

	@ReportRefFieldName(attrName = "Reserves Provisions Ratio")
	@AlertSubscribeTrendDelta(Occurances = 2, TrendDirection = direction.DECREASE, valueToCompare = 20)
	@AlertSubscribeTrendPenultimate(TrendDirection = direction.DECREASE, valueToCompare = 30)
	public final String	resrvProvRatio		= "CAGR Reserves Provisions Ratio";

	@ReportRefFieldName(attrName = "Effective DPS")
	public final String	divRatio			= "CAGR Dividend";

	@ReportRefFieldName(attrName = "FII Holding")
	public final String	fiiHolding		= "FII Holding";

	@ReportRefFieldName(attrName = "Eff. Promoter Holding")
	public final String	effPromHolding		= "Eff. Promoter Holding";

	@ReportRefFieldName(attrName = "Price Deviation from Avg")
	public final String	priceDeviationAvg	= "Avg. Price Deviation";

	/**
	 * 
	 */
	public DS_ScripsAnalysis()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Do Nothing will be handled by Aspect Centrally
	 */
	@Override
	@Cacheable(cacheManager = "cacheManagerSCE", value = "DSCache", keyGenerator = "DSkeyGenerator")
	public TY_ScRoot_AttrContainers generateReportDataSourceforScripCode(String scCode) throws EX_General
	{
		// TODO Auto-generated method stub
		return null;
	}

}
