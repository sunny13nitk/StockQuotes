package scriptsengine.pricingengine.JAXB.definitions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Price Projection Factor Metadata
 */

@XmlRootElement(name = "PPFactorMetadata")
@XmlAccessorType(XmlAccessType.FIELD)
public class PPFactorMetadata
{
	private String		ppFactor;

	private String		beanName;

	private boolean	isBase;

	private boolean	isCumulative;

	/**
	 * @return the ppFactor
	 */
	public String getPpFactor()
	{
		return ppFactor;
	}

	/**
	 * @param ppFactor
	 *             the ppFactor to set
	 */
	public void setPpFactor(String ppFactor)
	{
		this.ppFactor = ppFactor;
	}

	/**
	 * @return the beanName
	 */
	public String getBeanName()
	{
		return beanName;
	}

	/**
	 * @param beanName
	 *             the beanName to set
	 */
	public void setBeanName(String beanName)
	{
		this.beanName = beanName;
	}

	/**
	 * @return the isBase
	 */
	public boolean isBase()
	{
		return isBase;
	}

	/**
	 * @param isBase
	 *             the isBase to set
	 */
	public void setBase(boolean isBase)
	{
		this.isBase = isBase;
	}

	/**
	 * @return the isCumulative
	 */
	public boolean isCumulative()
	{
		return isCumulative;
	}

	/**
	 * @param isCumulative
	 *             the isCumulative to set
	 */
	public void setCumulative(boolean isCumulative)
	{
		this.isCumulative = isCumulative;
	}

	/**
	 * @param ppFactor
	 * @param beanName
	 * @param isBase
	 * @param isCumulative
	 */
	public PPFactorMetadata(String ppFactor, String beanName, boolean isBase, boolean isCumulative)
	{
		super();
		this.ppFactor = ppFactor;
		this.beanName = beanName;
		this.isBase = isBase;
		this.isCumulative = isCumulative;
	}

	/**
	 * 
	 */
	public PPFactorMetadata()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
