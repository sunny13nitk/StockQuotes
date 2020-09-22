package scriptsengine.uploadengineSC.appUtilities.interfaces;
/*
 * Scrip Data REtrieval Service Interface
 */

import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengineSC.appUtilities.defn.ScCalcData_PoJo;
import scriptsengine.uploadengineSC.entities.EN_SC_General;

public interface IScDataRetvSrv
{
	public ScCalcData_PoJo retrieveDataforSCCode(String ScCode, int DurationCAGR) throws EX_General;

	public ScCalcData_PoJo retrieveDataforSCRoot(EN_SC_General scRoot, int DurationCAGR) throws EX_General;

}
