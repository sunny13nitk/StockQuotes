package scriptsengine.aspects;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import scriptsengine.reportsengine.JAXB.interfaces.IRepSrvConfigMetadata;
import scriptsengine.reportsengine.interfaces.IXLSReportGenerator;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * 
 * Excel Report Aspect - Triggered when generateXLSReport(String filepath) or generateXLSReport(XSSFWorkbook wbCtxt)
 * method of any caling application that implements IXLSReportAware interface is called
 *
 */
@Aspect
@Component
public class XLSReportAspect implements ApplicationContextAware
{

	// Report Service Config. Metadata
	@Autowired
	private IRepSrvConfigMetadata		srvMapMdt;
	@Autowired
	private static MessagesFormatter	msgFormatter;

	private ApplicationContext		appCtxt;

	/**
	 * ----------------- Advice to Handle XLS report generation for a particular filepath *
	 * -------------------------------------
	 * 
	 * @param pjp
	 * @param filePath
	 * @throws Exception
	 *              ---------------------------------------------------------------------------------------------------
	 *              ----------
	 */

	@Around("XLSReportAwareInterface() &&generateXLSReport() && args(filePath)")
	public void handleXLSreportGeneration(ProceedingJoinPoint pjp, String filePath) throws Exception
	{

		if (pjp != null)
		{
			if (pjp.getTarget() != null && filePath != null)
			{
				String targetName = pjp.getTarget().getClass().getSimpleName();
				if (targetName != null && srvMapMdt != null)
				{

					// Get XLSReport Generator Bean Name
					String xlsBeanName = srvMapMdt.getRepSrvforSrvBeanName(targetName);
					if (xlsBeanName != null)
					{
						if (appCtxt != null)
						{
							try
							{
								/**
								 * Get the XLSReport Generator Bean
								 */
								IXLSReportGenerator repGenBean = (IXLSReportGenerator) appCtxt.getBean(xlsBeanName);
								if (repGenBean != null)
								{
									// Trigger XLS Generation
									repGenBean.triggerXLSReportGeneration(filePath, pjp.getTarget());
								}
							}
							catch (Exception e)
							{
								EX_General msgChgErr = new EX_General("ERR_XLS_BEANNOTFOUND", new Object[]
								{ targetName
								});
								msgFormatter.generate_message_snippet(msgChgErr);
								throw msgChgErr;
							}
						}
					}
				}

			}
		}
	}

	/**
	 * --------------------Advice to handle XLS Report Generation by addition of a new Sheet in specified Workbook
	 * Context
	 * -
	 * 
	 * @param pjp
	 *             - Invoker Instance
	 * @param wbCtxt
	 *             - XLS Workbook Context
	 * @throws Exception
	 *              ---------------------------------------------------------------------------------------------------
	 * 
	 */
	@Around("XLSReportAwareInterface() &&generateXLSReport() && args(wbCtxt)")
	public void handleXLSreportGenerationforWBCtxt(ProceedingJoinPoint pjp, XSSFWorkbook wbCtxt) throws Exception
	{
		if (pjp != null)
		{
			if (pjp.getTarget() != null && wbCtxt != null)
			{
				String targetName = pjp.getTarget().getClass().getSimpleName();
				if (targetName != null && srvMapMdt != null)
				{

					// Get XLSReport Generator Bean Name
					String xlsBeanName = srvMapMdt.getRepSrvforSrvBeanName(targetName);
					if (xlsBeanName != null)
					{
						if (appCtxt != null)
						{
							try
							{
								/**
								 * Get the XLSReport Generator Bean
								 */
								IXLSReportGenerator repGenBean = (IXLSReportGenerator) appCtxt.getBean(xlsBeanName);
								if (repGenBean != null)
								{
									// Trigger XLS Generation
									repGenBean.triggerXLSReportGeneration(wbCtxt, pjp.getTarget());
								}
							}
							catch (Exception e)
							{
								EX_General msgChgErr = new EX_General("ERR_XLS_BEANNOTFOUND", new Object[]
								{ targetName
								});
								msgFormatter.generate_message_snippet(msgChgErr);
								throw msgChgErr;
							}
						}
					}
				}

			}
		}
	}

	/********************************************************************************************************************
	 **************************************** POINTCUT DEFINITIONS SECTION ********************************************
	 ********************************************************************************************************************/

	/**
	 * Pointcut for Implementing Interface 'IXLSReportAware'
	 */
	@Pointcut("target(scriptsengine.reportsengine.interfaces.IXLSReportAware)")
	public void XLSReportAwareInterface()
	{

	}

	/**
	 * PointCut for generate XLS Report with only one parameter as WorkBook Context
	 */
	@Pointcut("execution(public void *.generateXLSReport(..))")
	public void generateXLSReportforWBctxt()
	{

	}

	/**
	 * PointCut for generate XLS Report with only one parameter as filePath
	 */
	@Pointcut("execution(public void *.generateXLSReport(..))")
	public void generateXLSReport()
	{

	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		this.appCtxt = ctxt;

	}

}
