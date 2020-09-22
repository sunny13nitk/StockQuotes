package scriptsengine.pricingengine.priceStratergies.services.interfaces;

import java.util.ArrayList;

import scriptsengine.pricingengine.definitions.TY_RawMPP_Stats;
import scriptsengine.uploadengine.exceptions.EX_General;

public interface IRawMaterial_PPStatistics
{

	public ArrayList<TY_RawMPP_Stats> getStatisticsforRawMaterialSrv() throws EX_General;

}
