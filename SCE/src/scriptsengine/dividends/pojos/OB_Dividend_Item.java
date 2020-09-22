package scriptsengine.dividends.pojos;

import java.util.Date;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.implementations.DependantObject;

/**
 * 
 * Dividend Item POJO- Associated in 1..* relation to OB_Positions_Header
 *
 */
@Component("OB_Dividend_Item")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OB_Dividend_Item extends DependantObject
{
	private int	PosID;
	private Date	TxnDate;
	private double	DPS;
	private double	Amount;

	public int getPosID()
	{
		return PosID;
	}

	public void setPosID(int posID)
	{
		PosID = posID;
	}

	public Date getTxnDate()
	{
		return TxnDate;
	}

	public void setTxnDate(Date txnDate)
	{
		TxnDate = txnDate;
	}

	public double getDPS()
	{
		return DPS;
	}

	public void setDPS(double dPS)
	{
		DPS = dPS;
	}

	public double getAmount()
	{
		return Amount;
	}

	public void setAmount(double amount)
	{
		Amount = amount;
	}

	public OB_Dividend_Item(int posID, Date txnDate, double dPS, double amount)
	{
		super();
		PosID = posID;
		TxnDate = txnDate;
		DPS = dPS;
		Amount = amount;
	}

	public OB_Dividend_Item()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
