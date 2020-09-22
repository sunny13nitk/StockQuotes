package scriptsengine.portfolio.services.interfaces;

import scriptsengine.portfolio.definitions.TY_Scrip_PositionModel;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Interface to Get Positions for a Scrip
 */
public interface IScripPositionsDB
{

	/**
	 * REturn Position model for a Scrip - Does not Involves Validation of Scrip Code
	 * If Used without Sc Code Validation Wrapper for Scrip Code Validation to be Used before
	 * 
	 * @param scCode
	 *             - Scrip Code
	 * @return - Position Model
	 * @throws EX_General
	 *              - Exception
	 */
	public TY_Scrip_PositionModel getPosModelforScCode(String scCode) throws EX_General;

}
