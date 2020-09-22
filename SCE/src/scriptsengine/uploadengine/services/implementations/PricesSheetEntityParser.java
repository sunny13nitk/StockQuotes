package scriptsengine.uploadengine.services.implementations;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.managers.StatisticsManager;
import scriptsengine.statistics.definitions.StDevResult;
import scriptsengine.statistics.exceptions.EX_StdDeviation;
import scriptsengine.statistics.interfaces.IStDev;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.ISheetEntityParser;

@SuppressWarnings("rawtypes")
@Service("PricesSheetEntityParser")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PricesSheetEntityParser implements ISheetEntityParser
{

	@Autowired
	private StatisticsManager	statMgr;
	@Autowired
	private MessagesFormatter	msgFormatter;

	@SuppressWarnings(
	{ "unchecked"
	})
	@Override
	public SheetEntities getEntitiesfromSheet(XSSFSheet sheet)
	          throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, EX_General
	{

		SheetEntities shEntities = new SheetEntities();

		if (sheet != null)
		{
			if (statMgr != null)
			{
				try
				{
					IStDev stdSrv = StatisticsManager.getStDeviationServiceforSheet(sheet);
					if (stdSrv != null)
					{
						stdSrv.process();
						ArrayList<StDevResult> dev_summ = stdSrv.getResults();
						if (dev_summ != null)
						{
							if (dev_summ.size() > 0)
							{
								shEntities.setSheetName(sheet.getSheetName());
								for ( StDevResult stDevResult : dev_summ )
								{
									shEntities.getSheetEntityList().add(stDevResult);
								}
							}
						}
					}
				}
				catch (EX_StdDeviation e)
				{
					EX_General egen = new EX_General("ERRSTDDEV", new Object[]
					{ sheet.getSheetName(), e.getMessage()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}
		}

		return shEntities;
	}

}
