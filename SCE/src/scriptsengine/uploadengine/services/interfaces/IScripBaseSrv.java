package scriptsengine.uploadengine.services.interfaces;

import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import scriptsengine.enums.SCEenums;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.implementations.FilepathValidationService;
import scriptsengine.uploadengine.validations.interfaces.IWBValidator;

public interface IScripBaseSrv
{

	/**
	 * @return the wbCreateValdSrv
	 */
	IWBValidator getWbCreateValdSrv();

	IWBValidator getWbUpdateValdSrv();

	/**
	 * @param wbCreateValdSrv
	 *             the wbCreateValdSrv to set
	 */
	void setWbCreateValdSrv(IWBValidator wbCreateValdSrv);

	/**
	 * @return the mode
	 */
	SCEenums.ModeOperation getMode();

	/**
	 * @param mode
	 *             the mode to set
	 */
	void setMode(SCEenums.ModeOperation mode);

	/**
	 * @return the wbContext
	 */
	XSSFWorkbook getWbContext();

	/**
	 * @param wbContext
	 *             the wbContext to set
	 */
	void setWbContext(XSSFWorkbook wbContext);

	void setWBcontext(String Filepath) throws IOException;

	void validateWB() throws EX_General;

	public FilepathValidationService getFpValidationSrv();

}