package scriptsengine.uploadengine.validations.sheetvalidators.implementations;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import scriptsengine.statistics.definitions.StDevResult;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.sheetvalidators.interfaces.ISheetPojoValidator;
import scriptsengine.utilities.implementations.DurationValidateService;

@Service("PricespojoValidationService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PricespojoValidationService implements ISheetPojoValidator
{

	@Autowired
	private DurationValidateService	durSrv;

	private ArrayList<StDevResult>	prices;

	@SuppressWarnings(
	{ "unchecked", "rawtypes"
	})
	@Override
	public void validatePojosfromSheetEntities(SheetEntities sheetEntities) throws EX_General
	{
		if (sheetEntities != null)
		{
			prices = sheetEntities.getSheetEntityList();
			if (prices != null)
			{
				if (prices.size() > 0)
				{

					for ( StDevResult stDevResult : prices )
					{
						// Year Validation - Base to Penultimate
						int cYear = Integer.parseInt(stDevResult.getTitle());
						if (durSrv != null)
						{
							durSrv.validateYear_BasetoPenultimate(cYear);
						}

					}
				}
			}
		}

	}

}
