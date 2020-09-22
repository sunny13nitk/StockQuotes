package scriptsengine.pricingengine.JAXB.interfaces;

import scriptsengine.pricingengine.JAXB.definitions.PricesProjectionMetadata;
import scriptsengine.uploadengine.exceptions.EX_General;

public interface IPPConfigMetadata
{
	public String getDefaultPricingBeanName() throws EX_General;

	public String getProjectionFactorBeanName(String projectionFactorName) throws EX_General;

	public PricesProjectionMetadata getPPMetadata() throws EX_General;
}
