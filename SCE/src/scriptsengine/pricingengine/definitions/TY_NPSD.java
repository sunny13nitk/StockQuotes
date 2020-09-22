package scriptsengine.pricingengine.definitions;

import scriptsengine.utilities.types.PenultimateQYear;

/**
 * Type to Hold Nett Profit DElta
 *
 */
public class TY_NPSD
{
	private PenultimateQYear	qyearTo;
	private PenultimateQYear	qyearFrom;
	private double			nettProfit_Current;
	private double			nettProfit_Penultimate;
	private double			NPD;
	private double			nettSales_Current;
	private double			nettSales_Penultimate;
	private double			NSD;

	/**
	 * @return the qyearTo
	 */
	public PenultimateQYear getQyearTo()
	{
		return qyearTo;
	}

	/**
	 * @param qyearTo
	 *             the qyearTo to set
	 */
	public void setQyearTo(PenultimateQYear qyearTo)
	{
		this.qyearTo = qyearTo;
	}

	/**
	 * @return the qyearFrom
	 */
	public PenultimateQYear getQyearFrom()
	{
		return qyearFrom;
	}

	/**
	 * @param qyearFrom
	 *             the qyearFrom to set
	 */
	public void setQyearFrom(PenultimateQYear qyearFrom)
	{
		this.qyearFrom = qyearFrom;
	}

	/**
	 * @return the nettSales_Current
	 */
	public double getNettSales_Current()
	{
		return nettSales_Current;
	}

	/**
	 * @param nettSales_Current
	 *             the nettSales_Current to set
	 */
	public void setNettSales_Current(double nettSales_Current)
	{
		this.nettSales_Current = nettSales_Current;
	}

	/**
	 * @return the nettSales_Penultimate
	 */
	public double getNettSales_Penultimate()
	{
		return nettSales_Penultimate;
	}

	/**
	 * @param nettSales_Penultimate
	 *             the nettSales_Penultimate to set
	 */
	public void setNettSales_Penultimate(double nettSales_Penultimate)
	{
		this.nettSales_Penultimate = nettSales_Penultimate;
	}

	/**
	 * @return the nSD
	 */
	public double getNSD()
	{
		return NSD;
	}

	/**
	 * @param nSD
	 *             the nSD to set
	 */
	public void setNSD(double nSD)
	{
		NSD = nSD;
	}

	/**
	 * @return the nettProfit_Current
	 */
	public double getNettProfit_Current()
	{
		return nettProfit_Current;
	}

	/**
	 * @param nettProfit_Current
	 *             the nettProfit_Current to set
	 */
	public void setNettProfit_Current(double nettProfit_Current)
	{
		this.nettProfit_Current = nettProfit_Current;
	}

	/**
	 * @return the nettProfit_Penultimate
	 */
	public double getNettProfit_Penultimate()
	{
		return nettProfit_Penultimate;
	}

	/**
	 * @param nettProfit_Penultimate
	 *             the nettProfit_Penultimate to set
	 */
	public void setNettProfit_Penultimate(double nettProfit_Penultimate)
	{
		this.nettProfit_Penultimate = nettProfit_Penultimate;
	}

	/**
	 * @return the nPD
	 */
	public double getNPD()
	{
		return NPD;
	}

	/**
	 * @param nPD
	 *             the nPD to set
	 */
	public void setNPD(double nPD)
	{
		NPD = nPD;
	}

	/**
	 * @param qyearTo
	 * @param qyearFrom
	 * @param nettProfit_Current
	 * @param nettProfit_Penultimate
	 * @param nPD
	 * @param nettSales_Current
	 * @param nettSales_Penultimate
	 * @param nSD
	 */
	public TY_NPSD(PenultimateQYear qyearTo, PenultimateQYear qyearFrom, double nettProfit_Current, double nettProfit_Penultimate, double nPD,
	          double nettSales_Current, double nettSales_Penultimate, double nSD)
	{
		super();
		this.qyearTo = qyearTo;
		this.qyearFrom = qyearFrom;
		this.nettProfit_Current = nettProfit_Current;
		this.nettProfit_Penultimate = nettProfit_Penultimate;
		NPD = nPD;
		this.nettSales_Current = nettSales_Current;
		this.nettSales_Penultimate = nettSales_Penultimate;
		NSD = nSD;
	}

	/**
	 * 
	 */
	public TY_NPSD()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
