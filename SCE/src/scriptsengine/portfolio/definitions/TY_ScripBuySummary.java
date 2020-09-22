package scriptsengine.portfolio.definitions;

/**
 * POJO for Scrip Purchase Summary - Shows Before purchase and After Purchase Scenarios
 *
 */
public class TY_ScripBuySummary
{

	private TY_PosSS	beforeBuy;
	private TY_PosSS	afterBuy;

	public TY_PosSS getBeforeBuy()
	{
		return beforeBuy;
	}

	public void setBeforeBuy(TY_PosSS beforeBuy)
	{
		this.beforeBuy = beforeBuy;
	}

	public TY_PosSS getAfterBuy()
	{
		return afterBuy;
	}

	public void setAfterBuy(TY_PosSS afterBuy)
	{
		this.afterBuy = afterBuy;
	}

	public TY_ScripBuySummary()
	{
		super();
		this.beforeBuy = new TY_PosSS();
		this.afterBuy = new TY_PosSS();
	}

}
