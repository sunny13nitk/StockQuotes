package scriptsengine.uploadengine.validations.sheetvalidators.implementations;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.pojos.OB_Scrip_FinishedProduct;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.sheetvalidators.interfaces.ISheetPojoValidator;
import scriptsengine.utilities.implementations.DurationValidateService;

@Service("FinishedProductspojoValidationService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FinishedProductspojoValidationService implements ISheetPojoValidator
{

	@Autowired
	private MessagesFormatter				msgFormatter;
	@Autowired
	private DurationValidateService			durSrv;

	private ArrayList<OB_Scrip_FinishedProduct>	fpEntities;

	@SuppressWarnings("unchecked")
	@Override
	public void validatePojosfromSheetEntities(SheetEntities sheetEntities) throws EX_General
	{
		if (sheetEntities != null)
		{
			this.fpEntities = sheetEntities.getSheetEntityList();
			if (this.fpEntities != null)
			{
				if (this.fpEntities.size() > 0)
				{
					for ( OB_Scrip_FinishedProduct ob_Scrip_FinishedProduct : fpEntities )
					{
						// Year Validation - Base to Penultimate

						if (durSrv != null)
						{
							durSrv.validateYear_BasetoPenultimate(ob_Scrip_FinishedProduct.getYear());
						}
					}
				}
			}
		}

	}
}