package scriptsengine.taxation.pojos;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.annotations.UserAware;
import modelframework.implementations.RootObject;

/**
 * 
 * Taxation Header Object
 */
@Component("OB_TaxH")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Always create a new bean Instance
@UserAware
public class OB_TaxH extends RootObject
{
	private String		FYAsmYear;

	private double		AmntPayable;

	private boolean	Settled;

	public String getFYAsmYear()
	{
		return FYAsmYear;
	}

	public void setFYAsmYear(String fYAsmYear)
	{
		FYAsmYear = fYAsmYear;
	}

	public double getAmntPayable()
	{
		return AmntPayable;
	}

	public void setAmntPayable(double amntPayable)
	{
		AmntPayable = amntPayable;
	}

	public boolean isSettled()
	{
		return Settled;
	}

	public void setSettled(boolean settled)
	{
		Settled = settled;
	}

	public OB_TaxH(String fYAsmYear, double amntPayable, boolean settled)
	{
		super();
		FYAsmYear = fYAsmYear;
		AmntPayable = amntPayable;
		Settled = settled;
	}

	public OB_TaxH()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
