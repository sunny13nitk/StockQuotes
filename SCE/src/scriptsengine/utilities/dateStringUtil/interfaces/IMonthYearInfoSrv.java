package scriptsengine.utilities.dateStringUtil.interfaces;

import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.utilities.dateStringUtil.types.MonthYearInfo;

/**
 * Month Year Info. Service Interface
 *
 */
public interface IMonthYearInfoSrv
{
	MonthYearInfo getMonthYearInfoforString(String MonYearTxt, String separator) throws EX_General;

}
