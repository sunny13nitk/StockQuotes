package scriptsengine.aspects;

import java.util.ArrayList;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.interfaces.IWBValidator;
import scriptsengine.uploadengine.validations.interfaces.IXSheetValidator;

@Aspect
@Component
public class XSheetValidationAspect implements ApplicationContextAware
{

	@Autowired
	private MessagesFormatter		msgFormatter;

	private ArrayList<SheetEntities>	wbEntities;

	private ApplicationContext		appCtxt;

	@Around("WBValidatorInterface() && validateXsheetsMethod()")
	public void processXSheetValidations(ProceedingJoinPoint pjp) throws EX_General
	{
		if (pjp.getTarget() instanceof IWBValidator)
		{
			IWBValidator wbValidator = (IWBValidator) pjp.getTarget();
			this.wbEntities = wbValidator.getWbEntities();
			if (wbEntities != null)
			{
				if (appCtxt != null)
				{
					IXSheetValidator xsheetValidator = (IXSheetValidator) appCtxt.getBean(wbValidator.getXsheetvalidatorname());
					if (xsheetValidator != null)
					{
						xsheetValidator.validateXSheets(wbEntities);
					}
				}
			}
		}
	}

	/********************************************************************************************************************
	 **************************************** POINTCUT DEFINITIONS SECTION ********************************************
	 ********************************************************************************************************************/

	/**
	 * Pointcut for Implementing Interface 'IWBValidator'
	 */
	@Pointcut("target(scriptsengine.uploadengine.validations.interfaces.IWBValidator)")
	public void WBValidatorInterface()
	{

	}

	/**
	 * PointCut for validateXsheets()
	 */
	@Pointcut("execution(public void *.validateXsheets())")
	public void validateXsheetsMethod()
	{

	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		if (ctxt != null)
		{
			this.appCtxt = ctxt;
		}

	}

}
