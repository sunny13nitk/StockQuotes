package scriptsengine.uploadengineSC.entities;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.implementations.RootObject;

/**
 * 
 * Scrip Root Object
 *
 */
@Component("EN_SC_General")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EN_SC_General extends RootObject
{

	private String	SCCode;
	private String	SCName;
	private String	Sector;
	private double	UPH;
	private double	CMP;
	private double	CurrPE;
	private double	CurrPB;
	private double	DivYield;
	private double	MktCap;
	private double	MCapToSales;
	private double	EPS;
	private double	PEG;
	private double	CPS;
	private double	SGToCapex;
	private double	NumShares;

	public String getSCCode()
	{
		return SCCode;
	}

	public void setSCCode(String sCCode)
	{
		SCCode = sCCode;
	}

	public String getSCName()
	{
		return SCName;
	}

	public void setSCName(String sCName)
	{
		SCName = sCName;
	}

	public String getSector()
	{
		return Sector;
	}

	public void setSector(String sector)
	{
		Sector = sector;
	}

	public double getUPH()
	{
		return UPH;
	}

	public void setUPH(double uPH)
	{
		UPH = uPH;
	}

	public double getCMP()
	{
		return CMP;
	}

	public void setCMP(double cMP)
	{
		CMP = cMP;
	}

	public double getCurrPE()
	{
		return CurrPE;
	}

	public void setCurrPE(double currPE)
	{
		CurrPE = currPE;
	}

	public double getCurrPB()
	{
		return CurrPB;
	}

	public void setCurrPB(double currPB)
	{
		CurrPB = currPB;
	}

	public double getDivYield()
	{
		return DivYield;
	}

	public void setDivYield(double divYield)
	{
		DivYield = divYield;
	}

	public double getMktCap()
	{
		return MktCap;
	}

	public void setMktCap(double mktCap)
	{
		MktCap = mktCap;
	}

	public double getMCapToSales()
	{
		return MCapToSales;
	}

	public void setMCapToSales(double mCapToSales)
	{
		MCapToSales = mCapToSales;
	}

	public double getEPS()
	{
		return EPS;
	}

	public void setEPS(double ePS)
	{
		EPS = ePS;
	}

	public double getPEG()
	{
		return PEG;
	}

	public void setPEG(double pEG)
	{
		PEG = pEG;
	}

	public double getCPS()
	{
		return CPS;
	}

	public void setCPS(double cPS)
	{
		CPS = cPS;
	}

	public double getSGToCapex()
	{
		return SGToCapex;
	}

	public void setSGToCapex(double sGToCapex)
	{
		SGToCapex = sGToCapex;
	}

	public double getNumShares()
	{
		return NumShares;
	}

	public void setNumShares(double numShares)
	{
		NumShares = numShares;
	}

	public EN_SC_General()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public EN_SC_General(String sCCode, String sCName, String sector, double uPH, double cMP, double currPE, double currPB, double divYield,
	          double mktCap, double mCapToSales, double ePS, double pEG, double cPS, double sGToCapex, double numShares)
	{
		super();
		SCCode = sCCode;
		SCName = sCName;
		Sector = sector;
		UPH = uPH;
		CMP = cMP;
		CurrPE = currPE;
		CurrPB = currPB;
		DivYield = divYield;
		MktCap = mktCap;
		MCapToSales = mCapToSales;
		EPS = ePS;
		PEG = pEG;
		CPS = cPS;
		SGToCapex = sGToCapex;
		NumShares = numShares;
	}

}
