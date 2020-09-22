package root.scripsEngine.utilities.dateStringUtil.interfaces;

import root.scripsEngine.uploadEngine.exceptions.EX_General;
import root.scripsEngine.utilities.dateStringUtil.types.MonthYearInfo;

/**
 * Month Year Info. Service Interface
 *
 */
public interface IMonthYearInfoSrv
{
	MonthYearInfo getMonthYearInfoforString(
	        String MonYearTxt, String separator
	) throws EX_General;
	
}
