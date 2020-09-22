package scriptsengine.valuehelpers.interfaces;

import java.util.ArrayList;

import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.valuehelpers.definitions.TY_StringKeyDesc;

/**
 * 
 * Value Help Interface for Scrips
 */
public interface IValueHelpScrips
{
	public ArrayList<TY_StringKeyDesc> getScripCodesDesc() throws EX_General;

	public ArrayList<TY_StringKeyDesc> getScripCodesSectors() throws EX_General;

	public ArrayList<TY_StringKeyDesc> getScripCodesUrl() throws EX_General;

	public ArrayList<String> getUniqueSectorsforScripsMaintained() throws EX_General;

	public ArrayList<TY_StringKeyDesc> getScripsforSector(String Sector) throws EX_General;

}
