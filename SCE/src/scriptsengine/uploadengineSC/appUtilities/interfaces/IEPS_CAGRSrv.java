package scriptsengine.uploadengineSC.appUtilities.interfaces;

import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengineSC.entities.EN_SC_General;

public interface IEPS_CAGRSrv
{
	public double getEPSCAGR(EN_SC_General scRoot, int Duration) throws EX_General;
}
