package scriptsengine.uploadengine.templates.interfaces;

import java.io.IOException;

import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Scrip Create Template - Interface
 */
public interface IScripCreateTemplate
{
	public void generateTemplateforScripCreation(String filePath) throws IOException, EX_General;

}
