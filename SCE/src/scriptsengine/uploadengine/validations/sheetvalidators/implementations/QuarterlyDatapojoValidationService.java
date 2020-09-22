package scriptsengine.uploadengine.validations.sheetvalidators.implementations;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.pojos.OB_Scrip_QuartersData;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.sheetvalidators.interfaces.ISheetPojoValidator;
import scriptsengine.utilities.implementations.DurationValidateService;

@Service("QuarterlyDatapojoValidationService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QuarterlyDatapojoValidationService implements ISheetPojoValidator
{

	@Autowired
	private MessagesFormatter				msgFormatter;
	@Autowired
	private DurationValidateService			durSrv;

	private ArrayList<OB_Scrip_QuartersData>	Qentities;

	@SuppressWarnings(
	{ "unchecked", "rawtypes"
	})
	@Override
	public void validatePojosfromSheetEntities(SheetEntities sheetEntities) throws EX_General
	{
		if (sheetEntities != null)
		{
			Qentities = sheetEntities.getSheetEntityList();
			ArrayList<OB_Scrip_QuartersData> QCopy = new ArrayList<OB_Scrip_QuartersData>();
			ArrayList<OB_Scrip_QuartersData> QRes = new ArrayList<OB_Scrip_QuartersData>();

			if (Qentities != null)
			{
				if (Qentities.size() > 0)
				{
					QCopy = Qentities;
					for ( OB_Scrip_QuartersData Qentity : Qentities )
					{
						// Year - Base to Penultimate Validation
						if (durSrv != null)
						{
							durSrv.validateYear_BasetoCurrent(Qentity.getYear());
						}

						// Quarter numerical Validation
						if (Qentity.getQuarter() < 1 || Qentity.getQuarter() > 4)
						{
							EX_General egen = new EX_General("INVQUARTER", new Object[]
							{ Qentity.getQuarter()
							});
							msgFormatter.generate_message_snippet(egen);
							throw egen;
						}

						// Quarter- Year Combination Validation to current Year Last Quarter
						if (durSrv != null)
						{
							durSrv.validateQYear_BasetoPenultimateQ(Qentity.getYear(), Qentity.getQuarter());
						}

						// Duplicate Quarter Year combination - lambda to find instances count that match
						QRes = QCopy.stream().filter(x -> x.getQuarter() == Qentity.getQuarter()).filter(x -> x.getYear() == Qentity.getYear())
						          .collect(Collectors.toCollection(ArrayList::new));
						if (QRes.size() > 1)
						{
							EX_General egen = new EX_General("ERRDUPYQ", new Object[]
							{ Qentity.getYear(), Qentity.getQuarter()
							});
							msgFormatter.generate_message_snippet(egen);
							throw egen;
						}

						// Profit Cannot be greater than Sales
						if (Qentity.getNettProfit() > Qentity.getNettSales())
						{
							EX_General egen = new EX_General("ERRPRSLMISMATCH", new Object[]
							{ Qentity.getNettProfit(), Qentity.getNettSales()
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
