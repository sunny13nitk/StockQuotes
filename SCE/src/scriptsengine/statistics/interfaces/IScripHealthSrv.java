package scriptsengine.statistics.interfaces;

import java.util.ArrayList;

import scriptsengine.reportsengine.repDS.definitions.TY_Attr_Container;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Scrip Health Service Interface
 */
public interface IScripHealthSrv
{
	public ArrayList<TY_Attr_Container> getScripHealthforScrip(String scCode) throws EX_General;

	public ArrayList<TY_Attr_Container> getScripHealthforScripDesc(String desc_startswith) throws EX_General;

}
