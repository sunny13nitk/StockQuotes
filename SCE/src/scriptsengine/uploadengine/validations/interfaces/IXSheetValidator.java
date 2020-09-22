package scriptsengine.uploadengine.validations.interfaces;

import java.util.ArrayList;

import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Cross sheets Validator Bean Interface
 * e.g. compare Years in prices and BalanceSheet Sheets during Scrip Creation etc.
 * Address CrossSheet Validation conerns
 */
public interface IXSheetValidator
{
	@SuppressWarnings("rawtypes")
	public void validateXSheets(ArrayList<SheetEntities> wbEntities) throws EX_General;

}
