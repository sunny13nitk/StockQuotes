package scriptsengine.uploadengine.validations.sheetvalidators.interfaces;

import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Interface to validate Sheet Pojo
 * Each Sheet to have it's own defintion if Sheet Pojo Validator Implmentation
 * 
 * @param <T>
 */
public interface ISheetPojoValidator
{
	@SuppressWarnings("rawtypes")
	public void validatePojosfromSheetEntities(SheetEntities sheetEntities) throws EX_General;

}
