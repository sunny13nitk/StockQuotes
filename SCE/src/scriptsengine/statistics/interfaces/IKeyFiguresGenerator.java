package scriptsengine.statistics.interfaces;

import java.util.ArrayList;

import scriptsengine.statistics.definitions.TY_KeyFigure;
import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Key Figures Generator Interface - Implemented for Statistics Attributes which require logical processing for
 * generation of Key figures
 */
public interface IKeyFiguresGenerator
{
	public ArrayList<TY_KeyFigure> generateKeyFigures(ScripDataContainer scDataCon) throws EX_General;
}
