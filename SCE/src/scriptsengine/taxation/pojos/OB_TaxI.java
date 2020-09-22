package scriptsengine.taxation.pojos;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.implementations.DependantObject;
import scriptsengine.enums.SCEenums;
import scriptsengine.enums.SCEenums.taxType;

/**
 * 
 * Taxation Item
 *
 */
@Component("OB_TaxI") // Initialize as bean in Context with name as Class Name
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Always create a new bean Instance
public class OB_TaxI extends DependantObject
{
	private String			FYAsmYear;
	private SCEenums.taxType	TaxType;
	private String			Description;
	private double			Amount;

	public String getFYAsmYear()
	{
		return FYAsmYear;
	}

	public void setFYAsmYear(String fYAsmYear)
	{
		FYAsmYear = fYAsmYear;
	}

	public SCEenums.taxType getTaxType()
	{
		return TaxType;
	}

	public void setTaxType(SCEenums.taxType taxType)
	{
		TaxType = taxType;
	}

	public String getDescription()
	{
		return Description;
	}

	public void setDescription(String description)
	{
		Description = description;
	}

	public double getAmount()
	{
		return Amount;
	}

	public void setAmount(double amount)
	{
		Amount = amount;
	}

	public OB_TaxI(String fYAsmYear, taxType taxType, String description, double amount)
	{
		super();
		FYAsmYear = fYAsmYear;
		TaxType = taxType;
		Description = description;
		Amount = amount;
	}

	public OB_TaxI()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
