package scriptsengine.taxation.interfaces;

import java.util.Date;

import scriptsengine.enums.SCEenums;

/**
 * INTERFACE for Taxable Object - TO be Implemented by all the Objects that need to be eligible for Tax computations
 *
 */
public interface ITaxable
{
	public Date getDate();

	public double getTxnAmount();

	public SCEenums.taxType getTaxType();

	public String getDesc();

	public void adjustTaxes();

	public void setDate(Date date);

	public void setTxnAmount(double txnAmount);

	public void setTaxType(SCEenums.taxType taxType);

	public void setDesc(String desc);

}
