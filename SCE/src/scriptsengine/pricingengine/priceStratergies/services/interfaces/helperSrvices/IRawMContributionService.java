package scriptsengine.pricingengine.priceStratergies.services.interfaces.helperSrvices;

import java.util.ArrayList;

import scriptsengine.pojos.OB_Scrip_RawMaterial;
import scriptsengine.pricingengine.definitions.TY_PFactor;
import scriptsengine.pricingengine.definitions.TY_PFactorDetail;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Raw Material Contribution Service
 */
public interface IRawMContributionService
{
	public ArrayList<TY_PFactorDetail> getEffectiveRMC(TY_PFactor rawMFactor, ArrayList<OB_Scrip_RawMaterial> RawMaterialsList) throws EX_General;

}
