package scriptsengine.uploadengineSC.appUtilities.interfaces;

import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengineSC.appUtilities.defn.AvgPEPricesPojo;
import scriptsengine.uploadengineSC.entities.EN_SC_General;

public interface IAvgPEPricesSrv
{
	public AvgPEPricesPojo getAvgPricesforScCode(String ScCode) throws EX_General;

	public AvgPEPricesPojo getAvgPricesforScDesc(String ScDescBeginsWith) throws EX_General;

	public AvgPEPricesPojo getAvgPricesforScRoot(EN_SC_General scRoot) throws EX_General;

}
