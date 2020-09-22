package scriptsengine.statistics.interfaces;

import scriptsengine.statistics.definitions.TY_AttribSingleVal;
import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Single Values Generator Interface- Generate Single Value Structure for a Statistics field specified as single value
 * in SS.XML
 * Will be implemented for fields that require logical manipulation for extracted values and thus have implementations
 * beans specified in respective fields in XML
 */
public interface ISingleValGenerator
{
	public TY_AttribSingleVal generateSinglValueAttribute(ScripDataContainer scDataCon) throws EX_General;
}
