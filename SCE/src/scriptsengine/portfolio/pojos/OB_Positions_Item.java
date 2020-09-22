package scriptsengine.portfolio.pojos;

import java.util.Date;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.implementations.DependantObject;
import scriptsengine.enums.SCEenums;
import scriptsengine.enums.SCEenums.txnType;

/**
 * Positions Items Bean - For Each Scrip line Item(s) for every trade
 *
 */
@Component("OB_Positions_Item") // Initialize as bean in Context with name as Class Name
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Always create a new bean Instance
public class OB_Positions_Item extends DependantObject
{

	private int			PosID;

	private Date			TxnDate;

	private int			QtyTxn;

	private double			PPUTxn;

	private SCEenums.txnType	TxnType;

	private String			DematAcNum;

	private int			ETQ;			// Effective Tax Qty - Qty available as Tax Rebate

	/**
	 * @return the posID
	 */
	public int getPosID()
	{
		return PosID;
	}

	/**
	 * @param posID
	 *             the posID to set
	 */
	public void setPosID(int posID)
	{
		PosID = posID;
	}

	/**
	 * @return the txnDate
	 */
	public Date getTxnDate()
	{
		return TxnDate;
	}

	/**
	 * @param txnDate
	 *             the txnDate to set
	 */
	public void setTxnDate(Date txnDate)
	{
		TxnDate = txnDate;
	}

	/**
	 * @return the qtyTxn
	 */
	public int getQtyTxn()
	{
		return QtyTxn;
	}

	/**
	 * @param qtyTxn
	 *             the qtyTxn to set
	 */
	public void setQtyTxn(int qtyTxn)
	{
		QtyTxn = qtyTxn;
	}

	/**
	 * @return the pPUTxn
	 */
	public double getPPUTxn()
	{
		return PPUTxn;
	}

	/**
	 * @param pPUTxn
	 *             the pPUTxn to set
	 */
	public void setPPUTxn(double pPUTxn)
	{
		PPUTxn = pPUTxn;
	}

	/**
	 * @return the txnType
	 */
	public SCEenums.txnType getTxnType()
	{
		return TxnType;
	}

	/**
	 * @param txnType
	 *             the txnType to set
	 */
	public void setTxnType(SCEenums.txnType txnType)
	{
		TxnType = txnType;
	}

	/**
	 * @return the dematAcNum
	 */
	public String getDematAcNum()
	{
		return DematAcNum;
	}

	/**
	 * @param dematAcNum
	 *             the dematAcNum to set
	 */
	public void setDematAcNum(String dematAcNum)
	{
		DematAcNum = dematAcNum;
	}

	/**
	 * @return the eTQ
	 */
	public int getETQ()
	{
		return ETQ;
	}

	/**
	 * @param eTQ
	 *             the eTQ to set
	 */
	public void setETQ(int eTQ)
	{
		ETQ = eTQ;
	}

	/**
	 * 
	 */
	public OB_Positions_Item()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param posID
	 * @param txnDate
	 * @param qtyTxn
	 * @param pPUTxn
	 * @param txnType
	 * @param dematAcNum
	 * @param eTQ
	 */
	public OB_Positions_Item(int posID, Date txnDate, int qtyTxn, double pPUTxn, txnType txnType, String dematAcNum, int eTQ)
	{
		super();
		PosID = posID;
		TxnDate = txnDate;
		QtyTxn = qtyTxn;
		PPUTxn = pPUTxn;
		TxnType = txnType;
		DematAcNum = dematAcNum;
		ETQ = eTQ;
	}

}
