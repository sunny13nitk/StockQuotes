package scriptsengine.uploadengine.updateIdentifiers.interfaces;

import java.util.ArrayList;

import modelframework.types.TY_NameValue;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Update Eligilbility Deteminator Interface - Will be implemented as a service for each of the respective sheet that
 * entitles to participate in Scrip Updation Eligibility determination. e.g. Balance Sheet, Quarterly Data Sheet
 *
 */
public interface IDetermineUpdateEligibility
{
	/**
	 * Determine if the sheet/scrip is eligible for Update based on key vals for that sheet
	 * 
	 * @param keyvals
	 *             - key values list in the sheet
	 * @return - true if eligible for update
	 * @throws EX_General
	 *              - Exception
	 */
	public boolean isEligibleforUpdate(ArrayList<TY_NameValue> keyvals) throws EX_General;
}
