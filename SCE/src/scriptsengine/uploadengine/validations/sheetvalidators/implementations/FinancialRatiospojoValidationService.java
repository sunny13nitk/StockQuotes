package scriptsengine.uploadengine.validations.sheetvalidators.implementations;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.pojos.OB_Scrip_Ratios;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.sheetvalidators.interfaces.ISheetPojoValidator;
import scriptsengine.utilities.implementations.DurationValidateService;

@Service("FinancialRatiospojoValidationService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FinancialRatiospojoValidationService implements ISheetPojoValidator
{

	@Autowired
	private MessagesFormatter		msgFormatter;
	@Autowired
	private DurationValidateService	durSrv;

	private ArrayList<OB_Scrip_Ratios>	ratioEntities;

	@SuppressWarnings("unchecked")
	@Override
	public void validatePojosfromSheetEntities(SheetEntities sheetEntities) throws EX_General
	{
		if (sheetEntities != null)
		{
			ratioEntities = sheetEntities.getSheetEntityList();
			if (ratioEntities != null)
			{
				if (ratioEntities.size() > 0)
				{
					for ( OB_Scrip_Ratios ob_Scrip_Ratios : ratioEntities )
					{
						// Year Validation - Base to Penultimate

						if (durSrv != null)
						{
							durSrv.validateYear_BasetoPenultimate(ob_Scrip_Ratios.getYear());
						}

						// Earnings Retention Ratio should not be greater than 100

						if (ob_Scrip_Ratios.getERR() > 100)
						{
							EX_General egen = new EX_General("ERRRETRATIO", new Object[]
							{ ob_Scrip_Ratios.getERR()
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
