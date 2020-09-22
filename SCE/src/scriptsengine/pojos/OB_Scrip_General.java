package scriptsengine.pojos;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.implementations.RootObject;

/**
 * Scrip Root Object Class
 * New Instance everytime initiated - during Data Maintenance
 * During Data Consumption the beans individually generated for each Scrip
 * will be stored in a collection guided via a manager and an interface
 *
 */
@Component("OB_Scrip_General") // Initialize as bean in Context with name as Class Name
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Always Create a New Instance
public class OB_Scrip_General extends RootObject
{
	private String	scCode;
	private String	scName;
	private String	scSector;
	private double	scHigh;
	private double	scLow;
	private double	faceValue;
	private double	sc200DMA;
	private double	mCap;
	private String	url;

	/**
	 * @return the scCode
	 */
	public String getscCode()
	{
		return scCode;
	}

	/**
	 * @return the faceValue
	 */
	public double getfaceValue()
	{
		return faceValue;
	}

	/**
	 * @param faceValue
	 *             the faceValue to set
	 */
	public void setfaceValue(double faceValue)
	{
		this.faceValue = faceValue;
	}

	/**
	 * @param scCode
	 *             the scCode to set
	 */
	public void setscCode(String scCode)
	{
		this.scCode = scCode;
	}

	/**
	 * @return the scName
	 */
	public String getscName()
	{
		return scName;
	}

	/**
	 * @param scName
	 *             the scName to set
	 */
	public void setscName(String scName)
	{
		this.scName = scName;
	}

	/**
	 * @return the scSector
	 */
	public String getscSector()
	{
		return scSector;
	}

	/**
	 * @param scSector
	 *             the scSector to set
	 */
	public void setscSector(String scSector)
	{
		this.scSector = scSector;
	}

	/**
	 * @return the scHigh
	 */
	public double getscHigh()
	{
		return scHigh;
	}

	/**
	 * @param scHigh
	 *             the scHigh to set
	 */
	public void setscHigh(double scHigh)
	{
		this.scHigh = scHigh;
	}

	/**
	 * @return the scLow
	 */
	public double getscLow()
	{
		return scLow;
	}

	/**
	 * @param scLow
	 *             the scLow to set
	 */
	public void setscLow(double scLow)
	{
		this.scLow = scLow;
	}

	/**
	 * @return the sc200DMA
	 */
	public double getsc200DMA()
	{
		return sc200DMA;
	}

	/**
	 * @param sc200dma
	 *             the sc200DMA to set
	 */
	public void setsc200DMA(double sc200dma)
	{
		sc200DMA = sc200dma;
	}

	/**
	 * @return the mCap
	 */
	public double getmCap()
	{
		return mCap;
	}

	/**
	 * @param mCap
	 *             the mCap to set
	 */
	public void setmCap(double mCap)
	{
		this.mCap = mCap;
	}

	/**
	 * @return the url
	 */
	public String geturl()
	{
		return url;
	}

	/**
	 * @param url
	 *             the url to set
	 */
	public void seturl(String url)
	{
		this.url = url;
	}

	/**
	 * 
	 */
	public OB_Scrip_General()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param scCode
	 * @param scName
	 * @param scSector
	 * @param scHigh
	 * @param scLow
	 * @param faceValue
	 * @param sc200dma
	 * @param mCap
	 * @param url
	 */
	public OB_Scrip_General(String scCode, String scName, String scSector, double scHigh, double scLow, double faceValue, double sc200dma,
	          double mCap, String url)
	{
		super();
		this.scCode = scCode;
		this.scName = scName;
		this.scSector = scSector;
		this.scHigh = scHigh;
		this.scLow = scLow;
		this.faceValue = faceValue;
		sc200DMA = sc200dma;
		this.mCap = mCap;
		this.url = url;
	}

}
