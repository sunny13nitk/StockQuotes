package scriptsengine.uploadengine.validations.sheetvalidators.implementations;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.pojos.OB_Scrip_BalSheet;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.sheetvalidators.interfaces.ISheetPojoValidator;
import scriptsengine.utilities.implementations.DurationValidateService;

@Service("BalanceSheetpojoValidationService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BalanceSheetpojoValidationService implements ISheetPojoValidator
{

	@Autowired
	private MessagesFormatter			msgFormatter;
	@Autowired
	private DurationValidateService		durSrv;

	private ArrayList<OB_Scrip_BalSheet>	balSheetEntities;

	@SuppressWarnings("unchecked")
	@Override
	public void validatePojosfromSheetEntities(SheetEntities sheetEntities) throws EX_General
	{
		if (sheetEntities != null)
		{
			this.balSheetEntities = sheetEntities.getSheetEntityList();
			if (balSheetEntities != null)
			{
				if (balSheetEntities.size() > 0)
				{
					for ( OB_Scrip_BalSheet ob_Scrip_BalSheet : balSheetEntities )
					{
						// Year Validation - Base to Penultimate

						if (durSrv != null)
						{
							durSrv.validateYear_BasetoPenultimate(ob_Scrip_BalSheet.getyear());
						}
					}
				}
			}
		}

	}

}
