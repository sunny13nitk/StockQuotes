package scriptsengine.simulations.sell.definitions;

import java.util.ArrayList;

/**
 * 
 * SELL Proposal - Container that holds the complete selling Proposal
 */
public class TY_Sell_Proposal
{
	private TY_SellPoposalH					sellProposalHeader;

	private ArrayList<TY_SellProposalI>		sellProposalItems;

	private ArrayList<TY_SellProposalI_Summary>	sellProposalSummary;

	private ArrayList<TY_SaleEligibleItems>		eligibleItems;

	private TY_TaxPerspective				taxPerspective;

	private TY_SCTxn_Summary					scTxnSummary;

	/**
	 * @return the scTxnSummary
	 */
	public TY_SCTxn_Summary getScTxnSummary()
	{
		return scTxnSummary;
	}

	/**
	 * @param scTxnSummary
	 *             the scTxnSummary to set
	 */
	public void setScTxnSummary(TY_SCTxn_Summary scTxnSummary)
	{
		this.scTxnSummary = scTxnSummary;
	}

	/**
	 * @return the taxPerspective
	 */
	public TY_TaxPerspective getTaxPerspective()
	{
		return taxPerspective;
	}

	/**
	 * @param taxPerspective
	 *             the taxPerspective to set
	 */
	public void setTaxPerspective(TY_TaxPerspective taxPerspective)
	{
		this.taxPerspective = taxPerspective;
	}

	/**
	 * @return the eligibleItems
	 */
	public ArrayList<TY_SaleEligibleItems> getEligibleItems()
	{
		return eligibleItems;
	}

	/**
	 * @param eligibleItems
	 *             the eligibleItems to set
	 */
	public void setEligibleItems(ArrayList<TY_SaleEligibleItems> eligibleItems)
	{
		this.eligibleItems = eligibleItems;
	}

	/**
	 * @return the sellProposalSummary
	 */
	public ArrayList<TY_SellProposalI_Summary> getSellProposalSummary()
	{
		return sellProposalSummary;
	}

	/**
	 * @param sellProposalSummary
	 *             the sellProposalSummary to set
	 */
	public void setSellProposalSummary(ArrayList<TY_SellProposalI_Summary> sellProposalSummary)
	{
		this.sellProposalSummary = sellProposalSummary;
	}

	/**
	 * @return the sellProposalHeader
	 */
	public TY_SellPoposalH getSellProposalHeader()
	{
		return sellProposalHeader;
	}

	/**
	 * @param sellProposalHeader
	 *             the sellProposalHeader to set
	 */
	public void setSellProposalHeader(TY_SellPoposalH sellProposalHeader)
	{
		this.sellProposalHeader = sellProposalHeader;
	}

	/**
	 * @return the sellProposalItems
	 */
	public ArrayList<TY_SellProposalI> getSellProposalItems()
	{
		return sellProposalItems;
	}

	/**
	 * @param sellProposalItems
	 *             the sellProposalItems to set
	 */
	public void setSellProposalItems(ArrayList<TY_SellProposalI> sellProposalItems)
	{
		this.sellProposalItems = sellProposalItems;

	}

	/**
	 * 
	 */
	public TY_Sell_Proposal()
	{
		super();
		this.sellProposalItems = new ArrayList<TY_SellProposalI>();
		this.sellProposalSummary = new ArrayList<TY_SellProposalI_Summary>();
		this.eligibleItems = new ArrayList<TY_SaleEligibleItems>();
		this.taxPerspective = new TY_TaxPerspective();
		this.scTxnSummary = new TY_SCTxn_Summary();
	}

}
