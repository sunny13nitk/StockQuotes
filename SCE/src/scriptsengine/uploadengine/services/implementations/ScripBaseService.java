package scriptsengine.uploadengine.services.implementations;

import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.GeneralMessage;
import modelframework.implementations.MessagesFormatter;
import scriptsengine.enums.SCEenums;
import scriptsengine.enums.SCEenums.ModeOperation;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripBaseSrv;
import scriptsengine.uploadengine.validations.implementations.FilepathValidationService;
import scriptsengine.uploadengine.validations.interfaces.IWBValidator;

/**
 * 
 * Scrip Base Service - Implments basic methods to support Scrip Creation/Update
 */
@Service("ScripBaseService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripBaseService implements IScripBaseSrv, ApplicationContextAware
{

	@Autowired
	private WBFilepathService		wbfpSrv;
	@Autowired
	private MessagesFormatter		msgFormatter;
	@Autowired
	private FilepathValidationService	fpValidationSrv;

	private XSSFWorkbook			wbContext;
	private SCEenums.ModeOperation	mode;

	private ApplicationContext		Context;

	private IWBValidator			wbCreateValdSrv;

	private IWBValidator			wbUpdateValdSrv;

	/**
	 * @return the fpValidationSrv
	 */
	@Override
	public FilepathValidationService getFpValidationSrv()
	{
		return fpValidationSrv;
	}

	/**
	 * @return the wbUpdateValdSrv
	 */
	@Override
	public IWBValidator getWbUpdateValdSrv()
	{
		return wbUpdateValdSrv;
	}

	/**
	 * @param wbUpdateValdSrv
	 *             the wbUpdateValdSrv to set
	 */
	public void setWbUpdateValdSrv(IWBValidator wbUpdateValdSrv)
	{
		this.wbUpdateValdSrv = wbUpdateValdSrv;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scriptsengine.uploadengine.services.implementations.IScripBaseSrv#getWbCreateValdSrv()
	 */
	@Override
	public IWBValidator getWbCreateValdSrv()
	{
		return wbCreateValdSrv;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * scriptsengine.uploadengine.services.implementations.IScripBaseSrv#setWbCreateValdSrv(scriptsengine.uploadengine.
	 * validations.interfaces.IWBValidator)
	 */
	@Override
	public void setWbCreateValdSrv(IWBValidator wbCreateValdSrv)
	{
		this.wbCreateValdSrv = wbCreateValdSrv;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scriptsengine.uploadengine.services.implementations.IScripBaseSrv#getMode()
	 */
	@Override
	public SCEenums.ModeOperation getMode()
	{
		return mode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scriptsengine.uploadengine.services.implementations.IScripBaseSrv#setMode(scriptsengine.enums.SCEenums.
	 * ModeOperation)
	 */
	@Override

	public void setMode(SCEenums.ModeOperation mode)
	{
		this.mode = mode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scriptsengine.uploadengine.services.implementations.IScripBaseSrv#getWbContext()
	 */
	@Override

	public XSSFWorkbook getWbContext()
	{
		return wbContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * scriptsengine.uploadengine.services.implementations.IScripBaseSrv#setWbContext(org.apache.poi.xssf.usermodel.
	 * XSSFWorkbook)
	 */
	@Override
	public void setWbContext(XSSFWorkbook wbContext)
	{
		this.wbContext = wbContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scriptsengine.uploadengine.services.implementations.IScripBaseSrv#setWBcontext(java.lang.String)
	 */
	@Override
	public void setWBcontext(String Filepath) throws IOException
	{
		if (Filepath != null && wbfpSrv != null)
		{
			try
			{
				if (fpValidationSrv != null)
				{
					if (!fpValidationSrv.validateFilePath(Filepath))
						throw new IOException();
					else
					{
						this.wbContext = wbfpSrv.getWBcontextfromFilepath(Filepath);
					}
				}
			}
			catch (IOException e)
			{
				GeneralMessage msgChgErr = new GeneralMessage("FILENOTFOUND", new Object[]
				{ Filepath
				});
				msgFormatter.generate_message_snippet(msgChgErr);
				throw e;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scriptsengine.uploadengine.services.implementations.IScripBaseSrv#validateWB()
	 */

	@Override
	public void validateWB() throws EX_General
	{
		// Create Mode
		if (mode == ModeOperation.CREATE)
		{
			/**
			 * Instantiate CreateScripWBValidationService bean
			 */
			this.wbCreateValdSrv = (IWBValidator) Context.getBean("CreateScripWBValidationService");
			if (wbCreateValdSrv != null)
			{
				try
				{
					/**
					 * Execute Validations and finally populate the Entities from respective sheets
					 * in the 'wbEntities' of the 'Create Scrip WB Validation Service' which is exposed via getter
					 * of interface 'IWBValidator' so it can be accesed while creating the Actual Root/Dependant
					 * Objects to finally create the Scrip in system
					 */
					wbCreateValdSrv.validateWB(this.wbContext, this.fpValidationSrv.getFilePath());

					/**
					 * Address Cross Sheet Validation Concerns - Handled via
					 * /SCE/src/scriptsengine/aspects/XSheetValidationAspect.java aspect
					 */
					wbCreateValdSrv.validateXsheets();

				}
				catch (EX_General e)
				{
					throw e;
				}
			}
		}
		else if (mode == ModeOperation.UPDATE)
		{
			/**
			 * Instantiate CreateScripWBValidationService bean
			 */
			this.wbUpdateValdSrv = (IWBValidator) Context.getBean("UpdateScripWBValidationService");
			if (wbUpdateValdSrv != null)
			{
				try
				{
					/**
					 * Execute Validations and finally populate the Entities from respective sheets
					 * in the 'wbEntities' of the 'Update Scrip WB Validation Service' which is exposed via getter
					 * of interface 'IWBValidator' so it can be accesed while creating the Actual Root/Dependant
					 * Objects to finally update the Scrip in system
					 */
					wbUpdateValdSrv.validateWB(this.wbContext, this.fpValidationSrv.getFilePath());

					/**
					 * Address Cross Sheet Validation Concerns - Handled via
					 * /SCE/src/scriptsengine/aspects/XSheetValidationAspect.java aspect
					 */
					wbUpdateValdSrv.validateXsheets();

				}
				catch (EX_General e)
				{
					throw e;
				}
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * scriptsengine.uploadengine.services.implementations.IScripBaseSrv#setApplicationContext(org.springframework.
	 * context.ApplicationContext)
	 */

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		this.Context = ctxt;

	}

}
