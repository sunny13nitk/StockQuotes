package scriptsengine.uploadengine.services.interfaces;

import java.io.IOException;

import org.springframework.context.ApplicationContext;

import scriptsengine.uploadengine.JAXB.definitions.WorkbookMetadata;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Scrip Data Service - Generic Interface
 */
public interface IScripDataService
{

	public void createScripfromXls_WB(String filepath) throws IOException, EX_General;

	public void updateScripfromXls_WB(String filepath) throws IOException, EX_General;

	public void generateScripCreateTemplate(String filepath) throws IOException, EX_General;

	public void generateScripUpdateTemplate(String filepath, String scripCode) throws IOException, EX_General;

	public void generateScripUpdateTemplatebyScripDesc(String filepath, String scripDesc) throws IOException, EX_General;

	public void initialize() throws EX_General;

	public WorkbookMetadata getWBMetaData();

	public ApplicationContext getContext();

	// public void createScripfromBasicData(OB_Scrip_General genData) throws Exception;

}
