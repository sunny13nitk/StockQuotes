package scriptsengine.uploadengine.validations.implementations;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_InvalidObjectException;
import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.exceptions.EX_NotRootException;
import modelframework.exceptions.EX_NullParamsException;
import modelframework.exceptions.EX_ParamCountMismatchException;
import modelframework.exceptions.EX_ParamInitializationException;
import modelframework.implementations.MessagesFormatter;
import scriptsengine.uploadengine.JAXB.definitions.SheetMetadata;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.exceptions.EX_ScripCodeNotFoundException;
import scriptsengine.uploadengine.exceptions.EX_ScripCodeSheetNotFoundException;
import scriptsengine.uploadengine.exceptions.EX_ScripMetadata;
import scriptsengine.uploadengine.services.implementations.ScripSheetMetadataService;
import scriptsengine.uploadengine.services.interfaces.ISheetEntityParser;
import scriptsengine.uploadengine.validations.interfaces.IWBValidator;
import scriptsengine.uploadengine.validations.sheetvalidators.interfaces.ISheetPojoValidator;

/**
 * Create Scrip work Book Validation Service
 * Prototype Bean - Instantiated while creating scrip
 */

@Service("CreateScripWBValidationService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CreateScripWBValidationService implements IWBValidator, ApplicationContextAware
{

	@Autowired
	private ScripExistsService		scExsSrv;

	@Autowired
	private ScripSheetMetadataService	scMdtSrv;

	@Autowired
	private MessagesFormatter		msgFormatter;

	private XSSFWorkbook			wbContext;

	private String					scripCode;

	private ApplicationContext		appCtxt;

	@SuppressWarnings("rawtypes")
	private ArrayList<SheetEntities>	wbEntities;

	private final String			pojoValSuffix		= "pojoValidationService";

	private final String			Xsheetvalidatorname	= "ScripXSheetValidationService";

	/**
	 * @return the scMdtSrv
	 */
	@Override
	public ScripSheetMetadataService getScMdtSrv()
	{
		return scMdtSrv;
	}

	/**
	 * @param scMdtSrv
	 *             the scMdtSrv to set
	 */
	public void setScMdtSrv(ScripSheetMetadataService scMdtSrv)
	{
		this.scMdtSrv = scMdtSrv;
	}

	/**
	 * @return the xsheetvalidatorname
	 */
	@Override
	public String getXsheetvalidatorname()
	{
		return Xsheetvalidatorname;
	}

	/**
	 * @return the wbEntities
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ArrayList<SheetEntities> getWbEntities()
	{
		return wbEntities;
	}

	/**
	 * @param wbEntities
	 *             the wbEntities to set
	 */
	@SuppressWarnings("rawtypes")
	public void setWbEntities(ArrayList<SheetEntities> wbEntities)
	{
		this.wbEntities = wbEntities;
	}

	/**
	 * @return the wbContext
	 */
	public XSSFWorkbook getWbContext()
	{
		return wbContext;
	}

	/**
	 * @param wbContext
	 *             the wbContext to set
	 */
	public void setWbContext(XSSFWorkbook wbContext)
	{
		this.wbContext = wbContext;
	}

	/**
	 * @return the scripCode
	 */
	@Override
	public String getScripCode()
	{
		return scripCode;
	}

	/**
	 * @param scripCode
	 *             the scripCode to set
	 */
	public void setScripCode(String scripCode)
	{
		this.scripCode = scripCode;
	}

	@Override
	public void validateWB(XSSFWorkbook wb, String filepath) throws EX_General
	{

		// 1. Check if the Scrip already exists - If Yes throw error

		if (scExsSrv != null && wb != null && filepath != null)
		{
			try
			{
				this.wbContext = wb;

				this.scripCode = scExsSrv.getScripCodefromWB(wb, filepath);
				if (scripCode != null)
				{

					if (scExsSrv.ISScripExisting(scripCode))
					{
						/**
						 * Scrip already exists - Should Not allow to create further
						 */
						EX_General egen = new EX_General("SCRIPALREADYEXISTS", new Object[]
						{ scripCode
						});
						msgFormatter.generate_message_snippet(egen);
						throw egen;

					}
				}
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NoSuchMethodException
			          | SecurityException | EX_ScripCodeSheetNotFoundException | EX_ScripCodeNotFoundException | EX_ScripMetadata
			          | EX_InvalidObjectException | EX_NotRootException | SQLException | EX_NullParamsException | EX_ParamCountMismatchException
			          | EX_ParamInitializationException | EX_InvalidRelationException e)
			{
				EX_General egen = new EX_General("SCRIPEXISTERROR", new Object[]
				{ e.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}
		}

		// 2. Check if the Work book has all the mandatory sheets
		/**
		 * General Sheet existence will be anyways validated by ScripExistsService call
		 * Get the mandatory sheets from ScripSheetMetadataService
		 */

		validateMandatorySheets();

		// 3.Generate Pojos from Sheets by Sheet Parsers
		generateSheetEntities();

		// 4. Finally validate the Pojos generated from Sheets
		validatePojosSheets();

	}

	@SuppressWarnings("rawtypes")
	private void validatePojosSheets() throws EX_General
	{
		if (this.wbEntities.size() > 0)
		{
			for ( SheetEntities shEntities : this.wbEntities )
			{
				if (shEntities != null)
				{
					String pojoValidatorImplBean = shEntities.getSheetName() + pojoValSuffix;

					ISheetPojoValidator pojoValidatorBean = (ISheetPojoValidator) appCtxt.getBean(pojoValidatorImplBean);
					if (pojoValidatorBean != null)
					{
						try
						{
							pojoValidatorBean.validatePojosfromSheetEntities(shEntities);
						}
						catch (EX_General e)
						{
							EX_General egen = new EX_General("ERRPOJOVAL", new Object[]
							{ shEntities.getSheetName(), e.getMessage()
							});
							msgFormatter.generate_message_snippet(egen);
							throw egen;
						}
					}
				}
			}
		}
	}

	/**
	 * Generate Sheet Entities
	 * 
	 * @throws EX_General
	 */
	@SuppressWarnings(
	{ "rawtypes"
	})
	private void generateSheetEntities() throws EX_General
	{
		this.wbEntities = new ArrayList<SheetEntities>();
		if (appCtxt != null && wbContext != null)
		{
			for ( int i = 0; i < wbContext.getNumberOfSheets(); i++ )
			{
				XSSFSheet sheet = wbContext.getSheetAt(i);
				if (sheet != null)
				{
					// Get Sheet Parser BeanName if any explicitly defined
					SheetMetadata shtMdt = scMdtSrv.getSheetMdtbySheetName(sheet.getSheetName());
					if (shtMdt != null)
					{
						/**
						 * Processing via Specific entity Parser Bean
						 */
						if (shtMdt.getEntParserBeanName() != null)
						{
							// Get Bean for specific Sheet Parser
							ISheetEntityParser spSheetParserSrv = (ISheetEntityParser) appCtxt.getBean(shtMdt.getEntParserBeanName());
							if (spSheetParserSrv != null)
							{
								try
								{
									wbEntities.add(spSheetParserSrv.getEntitiesfromSheet(sheet));
								}
								catch (InstantiationException | IllegalAccessException | IllegalArgumentException
								          | InvocationTargetException e)
								{
									EX_General egen = new EX_General("ERRENTGENSHEET", new Object[]
									{ sheet.getSheetName(), e.getMessage()
									});
									msgFormatter.generate_message_snippet(egen);
									throw egen;
								}
							}
						}
						/**
						 * Processing via Generic Sheet Parser Bean
						 */
						else
						{
							/*
							 * SheetEntities shEntities = new SheetEntities();
							 * shEntities.setSheetName(sheet.getSheetName());
							 */
							// Get General Parser Bean
							ISheetEntityParser genericSheetParserSrv = (ISheetEntityParser) appCtxt.getBean("SheetEntityParserService");
							if (genericSheetParserSrv != null)
							{
								try
								{
									// shEntities.setSheetEntityList(genericSheetParserSrv.getEntitiesfromSheet(sheet));
									wbEntities.add(genericSheetParserSrv.getEntitiesfromSheet(sheet));
								}
								catch (InstantiationException | IllegalAccessException | IllegalArgumentException
								          | InvocationTargetException e)
								{
									EX_General egen = new EX_General("ERRENTGENSHEET", new Object[]
									{ sheet.getSheetName(), e.getMessage()
									});
									msgFormatter.generate_message_snippet(egen);
									throw egen;
								}
							}

						}
					}
				}

			}
		}

	}

	private boolean validateMandatorySheets() throws EX_General
	{
		boolean allmandatoryfound = true;

		if (scMdtSrv != null)
		{
			ArrayList<String> mandSheets = scMdtSrv.getMandatorySheets();
			if (mandSheets != null)
			{
				if (mandSheets.size() > 0)
				{
					for ( String sheet : mandSheets )
					{
						XSSFSheet sheetRef = this.getWbContext().getSheet(sheet);
						if (sheetRef == null)
						{
							EX_General egen = new EX_General("MANDSHEETNOTFOUND", new Object[]
							{ sheet, this.getScripCode()
							});
							msgFormatter.generate_message_snippet(egen);
							throw egen;

						}
					}
				}
			}
		}

		return allmandatoryfound;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		this.appCtxt = ctxt;

	}

	@Override
	// Handled via Aspect where dedicated bean implementing IXSheetValidator would be instantiated
	public void validateXsheets()
	{
		// TODO Auto-generated method stub

	}

}
