package scriptsengine.pricingengine.JAXB.definitions;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Prices Projection Metadata - Constitutes PPFactorMetadata List
 * as per the configuration File
 */

@XmlRootElement(name = "PricesProjectionMetadata")
@XmlAccessorType(XmlAccessType.FIELD)
public class PricesProjectionMetadata
{
	private ArrayList<PPFactorMetadata> pricingFactorsConfig;

	/**
	 * @return the pricingFactorsConfig
	 */
	public ArrayList<PPFactorMetadata> getPricingFactorsConfig()
	{
		return pricingFactorsConfig;
	}

	/**
	 * @param pricingFactorsConfig
	 *             the pricingFactorsConfig to set
	 */
	public void setPricingFactorsConfig(ArrayList<PPFactorMetadata> pricingFactorsConfig)
	{
		this.pricingFactorsConfig = pricingFactorsConfig;
	}

	/**
	 * @param pricingFactorsConfig
	 */
	public PricesProjectionMetadata(ArrayList<PPFactorMetadata> pricingFactorsConfig)
	{
		super();
		this.pricingFactorsConfig = pricingFactorsConfig;
	}

	/**
	 * 
	 */
	public PricesProjectionMetadata()
	{
		this.pricingFactorsConfig = new ArrayList<PPFactorMetadata>();
	}

}
