package scriptsengine.managers;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import scriptsengine.statistics.exceptions.EX_StdDeviation;
import scriptsengine.statistics.interfaces.IStDev;

@Service("StatisticsManager")
public class StatisticsManager implements ApplicationContextAware
{

	private static ApplicationContext Context;

	public static IStDev getStDeviationService(String filepath) throws FileNotFoundException, EX_StdDeviation, IOException
	{
		IStDev StDevService = null;
		if (Context != null)
		{
			StDevService = (IStDev) Context.getBean("StdDeviationService");
			if (StDevService != null)
			{
				if (filepath != null)
				{
					StDevService.initializefromXlsx(filepath);
				}
				else
				{
					StDevService.initialize();
				}
			}
		}

		return StDevService;
	}

	public static IStDev getStDeviationServiceforWBSheet(XSSFWorkbook wb, String sheetName)
	          throws FileNotFoundException, EX_StdDeviation, IOException
	{
		IStDev StDevService = null;
		if (Context != null)
		{
			StDevService = (IStDev) Context.getBean("StdDeviationService");
			if (StDevService != null)
			{
				if (wb != null && sheetName != null)
				{
					StDevService.initializefromWB(wb, sheetName);
				}
				else
					throw new EX_StdDeviation("INVALID_WB_SHEET", new Object[]
					{ sheetName
					});

			}
		}

		return StDevService;
	}

	public static IStDev getStDeviationServiceforSheet(XSSFSheet sheet) throws EX_StdDeviation
	{
		IStDev StDevService = null;
		if (Context != null)
		{
			StDevService = (IStDev) Context.getBean("StdDeviationService");
			if (StDevService != null)
			{
				if (sheet != null)
				{
					StDevService.initializefromSheet(sheet);
				}
				else
					throw new EX_StdDeviation("INVALID_WB_SHEET", new Object[]
					{ sheet.getSheetName()
					});

			}
		}

		return StDevService;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		Context = ctxt;
	}

}
