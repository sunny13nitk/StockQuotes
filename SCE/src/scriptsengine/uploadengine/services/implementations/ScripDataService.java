package scriptsengine.uploadengine.services.implementations;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.enums.SCEenums;
import scriptsengine.uploadengine.JAXB.definitions.PathsJAXB;
import scriptsengine.uploadengine.JAXB.definitions.WorkbookMetadata;
import scriptsengine.uploadengine.JAXB.implementations.Workbook_JAXB;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripBaseSrv;
import scriptsengine.uploadengine.services.interfaces.IScripCreate;
import scriptsengine.uploadengine.services.interfaces.IScripDataService;
import scriptsengine.uploadengine.services.interfaces.IScripUpdate;
import scriptsengine.uploadengine.templates.interfaces.IScripCreateTemplate;
import scriptsengine.uploadengine.templates.interfaces.IScripUpdateTemplate;

/**
 * Scrips Data Service Bean - Singleton Context Bean to
 * Create/Update Scrip from Excel Workbook file
 */

@Service("ScripDataService")
public class ScripDataService implements IScripDataService, ApplicationContextAware
{

	@Autowired
	private MessagesFormatter	msgFormatter;

	// private ScripExistsService scExistsService;

	// private SectorsService secSrv;

	private WorkbookMetadata		WBMetaData;

	private String				XmlPath;

	private ApplicationContext	Context;

	/**
	 * @return the context
	 */
	@Override
	public ApplicationContext getContext()
	{
		return Context;
	}

	/**
	 * @return the wBMetaData
	 */
	@Override
	public WorkbookMetadata getWBMetaData()
	{
		return WBMetaData;
	}

	/**
	 * @param wBMetaData
	 *             the wBMetaData to set
	 */
	public void setWBMetaData(WorkbookMetadata wBMetaData)
	{
		WBMetaData = wBMetaData;
	}

	/**
	 * @return the xmlPath
	 */
	public String getXmlPath()
	{
		return XmlPath;
	}

	/**
	 * @param xmlPath
	 *             the xmlPath to set
	 */
	public void setXmlPath(String xmlPath)
	{
		XmlPath = xmlPath;
	}

	/**
	 * Create Scrip from Work Book Xls File
	 * 
	 * @throws IOException
	 * @throws EX_General
	 */
	@Override
	public void createScripfromXls_WB(String filepath) throws IOException, EX_General
	{
		if (Context != null)
		{
			IScripCreate scripCrSrv = Context.getBean(IScripCreate.class);
			if (scripCrSrv != null)
			{
				// Create new Session Bean for Base Service and Set It in Scrip Create Service

				IScripBaseSrv baseSrv = Context.getBean(IScripBaseSrv.class);
				scripCrSrv.setBaseService(baseSrv);

				// Set the Work Book Context in Scrip Base Service Instance Bean
				scripCrSrv.getBaseService().setWBcontext(filepath);
				// Set the Mode in SCrip Base Service Instance Bean as Create
				scripCrSrv.getBaseService().setMode(SCEenums.ModeOperation.CREATE);
				// Validate the WB using Scrip Base Service
				scripCrSrv.getBaseService().validateWB();
				// Finally Create the Scrip
				scripCrSrv.createScrip();
			}
		}
	}

	@Override
	public void updateScripfromXls_WB(String filepath) throws IOException, EX_General
	{
		if (Context != null)
		{
			IScripUpdate scripUpdSrv = Context.getBean(IScripUpdate.class);
			if (scripUpdSrv != null)
			{
				// Create new Session Bean for Base Service and Set It in Scrip Create Service

				IScripBaseSrv baseSrv = Context.getBean(IScripBaseSrv.class);
				scripUpdSrv.setBaseService(baseSrv);

				// Set the Work Book Context in Scrip Base Service Instance Bean
				scripUpdSrv.getBaseService().setWBcontext(filepath);
				// Set the Mode in SCrip Base Service Instance Bean as Create
				scripUpdSrv.getBaseService().setMode(SCEenums.ModeOperation.UPDATE);
				// Validate the WB using Scrip Base Service
				scripUpdSrv.getBaseService().validateWB();
				// Finally Update the Scrip
				scripUpdSrv.updateScrip();
			}
		}
	}

	/**
	 * Create Scrip Create Template from WBMetadata at specified location
	 */
	@Override
	public void generateScripCreateTemplate(String filepath) throws IOException, EX_General
	{
		if (filepath != null && Context != null)
		{
			IScripCreateTemplate scCrTmplSrv = Context.getBean(IScripCreateTemplate.class);
			if (scCrTmplSrv != null)
			{
				scCrTmplSrv.generateTemplateforScripCreation(filepath);
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		if (ctxt != null)
		{
			PathsJAXB pathsWBMdt = ctxt.getBean(PathsJAXB.class);
			if (pathsWBMdt != null)
			{
				this.XmlPath = pathsWBMdt.getJaxPath_WB_xml();
			}
			this.Context = ctxt;
		}

	}

	/**
	 * Initialize the Work Book Metadata in the Scrip Data Service
	 */
	@Override
	public void initialize() throws EX_General
	{
		Workbook_JAXB wbUnmarshal = new Workbook_JAXB();
		try
		{
			this.WBMetaData = (WorkbookMetadata) wbUnmarshal.Load_XML_to_Objects(this.getXmlPath()).get(0);
		}
		catch (Exception e)
		{
			EX_General msgChgErr = new EX_General("UPLOAD_SRV_ERR", null);
			msgFormatter.generate_message_snippet(msgChgErr);
			throw msgChgErr;
		}
	}

	@Override
	public void generateScripUpdateTemplate(String filepath, String scripCode) throws IOException, EX_General
	{
		if (filepath != null && Context != null)
		{
			IScripUpdateTemplate scUpdTmplSrv = Context.getBean(IScripUpdateTemplate.class);
			if (scUpdTmplSrv != null)
			{
				scUpdTmplSrv.generateTemplateforScripUpdation(filepath, scripCode);
			}
		}

	}

	@Override
	public void generateScripUpdateTemplatebyScripDesc(String filepath, String scripDesc) throws IOException, EX_General
	{
		if (filepath != null && Context != null)
		{
			IScripUpdateTemplate scUpdTmplSrv = Context.getBean(IScripUpdateTemplate.class);
			if (scUpdTmplSrv != null)
			{
				scUpdTmplSrv.generateTemplateforScripUpdationbyscripDesc(filepath, scripDesc);
			}
		}

	}

	/*
	 * public void createScripfromBasicData(OB_Scrip_General genData) throws Exception
	 * {
	 * if (genData != null)
	 * {
	 * if (genData.getscCode() != null && genData.getscSector() != null)
	 * {
	 * if (scExistsService != null && secSrv != null)
	 * {
	 * if (scExistsService.getScripRootbyCode(genData.getscCode()) != null)
	 * {
	 * EX_General egen = new EX_General("SCRIPALREADYEXISTS", new Object[]
	 * { genData.getscCode()
	 * });
	 * msgFormatter.generate_message_snippet(egen);
	 * throw egen;
	 * }
	 * 
	 * else
	 * {
	 * // Validate if Sector Exists or create if it is a new Sector
	 * secSrv.createSectorwithexistenceCheck(genData.getscSector());
	 * 
	 * // High should be > low
	 * 
	 * if (genData.getscHigh() < genData.getscLow())
	 * {
	 * EX_General egen = new EX_General("ERRHIGHLOW", new Object[]
	 * { genData.getscCode()
	 * });
	 * msgFormatter.generate_message_snippet(egen);
	 * throw egen;
	 * 
	 * }
	 * 
	 * // 200 DMA should be > 52wk low and less than 52wk High
	 * if (genData.getsc200DMA() < genData.getscLow() || genData.getsc200DMA() > genData.getscHigh())
	 * {
	 * EX_General egen = new EX_General("ERRHIGHLOWDMA", new Object[]
	 * { genData.getscCode()
	 * });
	 * msgFormatter.generate_message_snippet(egen);
	 * throw egen;
	 * }
	 * 
	 * OB_Scrip_General scRoot = ModelObjectFactory.createObjectbyClass(OB_Scrip_General.class);
	 * if (scRoot != null)
	 * {
	 * scRoot.setscCode(genData.getscCode());
	 * scRoot.setscName(genData.getscName());
	 * scRoot.setfaceValue(genData.getfaceValue());
	 * scRoot.setmCap(genData.getmCap());
	 * scRoot.setsc200DMA(genData.getsc200DMA());
	 * scRoot.setscHigh(genData.getscHigh());
	 * scRoot.setscLow(genData.getscLow());
	 * scRoot.seturl(genData.geturl());
	 * scRoot.setscSector(genData.getscSector());
	 * 
	 * try
	 * {
	 * scRoot.Save();
	 * }
	 * catch (Exception e)
	 * {
	 * EX_General egen = new EX_General("ERRCRSCROOT", new Object[]
	 * { genData.getscCode(), e.getMessage()
	 * });
	 * msgFormatter.generate_message_snippet(egen);
	 * throw egen;
	 * }
	 * 
	 * }
	 * }
	 * 
	 * }
	 * }
	 * 
	 * }
	 * 
	 * }
	 */
}
