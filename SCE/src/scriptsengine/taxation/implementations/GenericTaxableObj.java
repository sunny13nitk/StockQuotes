package scriptsengine.taxation.implementations;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import scriptsengine.enums.SCEenums;
import scriptsengine.taxation.interfaces.ITaxable;

/**
 * Service to Intialize a Generic Prototype Scoped Taxable Interface Bean
 * To be referred in Aspect
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GenericTaxableObj implements ITaxable
{
	private java.util.Date	Date;
	private double			TxnAmount;
	private SCEenums.taxType	taxType;
	private String			Desc;

	@Override
	public java.util.Date getDate()
	{
		return Date;
	}

	@Override
	public void setDate(java.util.Date date)
	{
		Date = date;
	}

	@Override
	public double getTxnAmount()
	{
		return TxnAmount;
	}

	@Override
	public void setTxnAmount(double txnAmount)
	{
		TxnAmount = txnAmount;
	}

	@Override
	public SCEenums.taxType getTaxType()
	{
		return taxType;
	}

	@Override
	public void setTaxType(SCEenums.taxType taxType)
	{
		this.taxType = taxType;
	}

	@Override
	public String getDesc()
	{
		return Desc;
	}

	@Override
	public void setDesc(String desc)
	{
		Desc = desc;
	}

	public GenericTaxableObj()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void adjustTaxes()
	{
		// DO Nothing - Will be Handled by an Aspect - TaxationAspect

	}

}
