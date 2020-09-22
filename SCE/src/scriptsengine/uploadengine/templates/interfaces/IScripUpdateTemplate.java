package scriptsengine.uploadengine.templates.interfaces;

import java.io.IOException;

import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Scrip Update Template - Interface
 */
public interface IScripUpdateTemplate
{
	public void generateTemplateforScripUpdation(String filePath, String scripCode) throws IOException, EX_General;

	public void generateTemplateforScripUpdationbyscripDesc(String filePath, String scripDescPattern) throws IOException, EX_General;

}
