package scriptsengine.uploadengine.validations.sheetvalidators.implementations;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.pojos.OB_Scrip_RawMaterial;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.sheetvalidators.interfaces.ISheetPojoValidator;
import scriptsengine.utilities.implementations.DurationValidateService;

@Service("RawMaterialspojoValidationService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RawMaterialspojoValidationService implements ISheetPojoValidator
{

	@Autowired
	private MessagesFormatter			msgFormatter;
	@Autowired
	private DurationValidateService		durSrv;

	private ArrayList<OB_Scrip_RawMaterial>	rawMEntities;

	@SuppressWarnings("unchecked")
	@Override
	public void validatePojosfromSheetEntities(SheetEntities sheetEntities) throws EX_General
	{
		if (sheetEntities != null)
		{
			this.rawMEntities = sheetEntities.getSheetEntityList();
			if (this.rawMEntities != null)
			{
				if (this.rawMEntities.size() > 0)
				{
					for ( OB_Scrip_RawMaterial ob_Scrip_RawMaterial : this.rawMEntities )
					{
						// Year Validation - Base to Penultimate

						if (durSrv != null)
						{
							durSrv.validateYear_BasetoPenultimate(ob_Scrip_RawMaterial.getYear());
						}
					}
				}
			}
		}

	}

}
