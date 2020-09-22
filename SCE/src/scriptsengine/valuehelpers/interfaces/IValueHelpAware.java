package scriptsengine.valuehelpers.interfaces;

import java.util.ArrayList;

import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Value Help Aware Interface - Implemented by context scope Services that have value help enabled
 */
public interface IValueHelpAware
{
	public <T> ArrayList<T> getvalueHelpList() throws EX_General;
}
