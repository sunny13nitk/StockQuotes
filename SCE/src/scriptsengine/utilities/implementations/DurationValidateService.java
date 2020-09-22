package scriptsengine.utilities.implementations;

import java.time.Year;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.utilities.interfaces.IDurationValdiateService;
import scriptsengine.utilities.types.CurrentFinancialPeriod;

@Service("DurationValidateService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DurationValidateService implements IDurationValdiateService
{
	@Autowired
	private MessagesFormatter	msgFormatter;

	private final int			baseYear	= 2010;

	private final int			baseQtr	= 1;

	private final int			maxQtr	= 4;

	@Override
	// Year not less than 2010 and not greater than (current year - 1)
	public void validateYear_BasetoPenultimate(int cmpYear) throws EX_General
	{
		int penYear = Year.now().getValue() - 1;
		if (cmpYear < baseYear || cmpYear > penYear)
		{
			EX_General egen = new EX_General("ERRINVYEAR", new Object[]
			{ baseYear, penYear, cmpYear
			});
			msgFormatter.generate_message_snippet(egen);
			throw egen;
		}

	}

	@Override
	public void validateQYear_BasetoPenultimateQ(int cmpYear, int Quarter) throws EX_General
	{
		CurrentFinancialPeriod period = new CurrentFinancialPeriod();
		if ((cmpYear > baseYear && Quarter >= baseQtr) && (cmpYear <= period.getYear() && Quarter < period.getQuarter()))
		{
			// Do Nothing
		}
		else
		{
			// If Current Quarter is 1st Quarter of new year
			if ((cmpYear < period.getYear() && Quarter <= maxQtr))
			{
				// Do Nothing
			}
			else
			{
				EX_General egen = new EX_General("INVYRQTR", new Object[]
				{ cmpYear, Quarter
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}

		}

	}

	@Override
	// Year not less than 2010 and not greater than current year
	public void validateYear_BasetoCurrent(int cmpYear) throws EX_General
	{
		int currYear = Year.now().getValue();
		if (cmpYear < baseYear || cmpYear > currYear)
		{
			EX_General egen = new EX_General("ERRINVYEAR", new Object[]
			{ baseYear, currYear, cmpYear
			});
			msgFormatter.generate_message_snippet(egen);
			throw egen;
		}

	}

	/***************************************************************************************************************
	 * PRIVATE SECTION
	 ***************************************************************************************************************/

}
