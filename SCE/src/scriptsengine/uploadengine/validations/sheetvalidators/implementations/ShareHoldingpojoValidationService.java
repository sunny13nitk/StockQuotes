package scriptsengine.uploadengine.validations.sheetvalidators.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.pojos.OB_Scrip_Shareholding;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.sheetvalidators.interfaces.ISheetPojoValidator;

@Service("ShareHoldingpojoValidationService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShareHoldingpojoValidationService implements ISheetPojoValidator
{
	@Autowired
	private MessagesFormatter	msgFormatter;

	private OB_Scrip_Shareholding	scShareholding;;

	@SuppressWarnings("rawtypes")
	@Override
	public void validatePojosfromSheetEntities(SheetEntities sheetEntities) throws EX_General
	{
		if (sheetEntities != null)
		{
			scShareholding = (OB_Scrip_Shareholding) sheetEntities.getSheetEntityList().get(0);
			if (scShareholding != null)
			{
				// Total of shareholdings to be equal to 100
				double totalShareholding = scShareholding.getpromoter() + scShareholding.getfii() + scShareholding.getdii()
				          + scShareholding.getgeneral();
				// Only .3% variance in shareholding total is allowed
				if (!(totalShareholding >= 99.7 && totalShareholding <= 100.1))
				{
					EX_General egen = new EX_General("ERRSHTOTAL", new Object[]
					{ (scShareholding.getpromoter() + scShareholding.getfii() + scShareholding.getdii() + scShareholding.getgeneral()),
					          scShareholding.getscCode()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}

				// Pledged Share holding cannot exceed Promoter Holding
				if (scShareholding.getpledged() >= 100)
				{
					EX_General egen = new EX_General("ERRPLEPROM", new Object[]
					{ scShareholding.getpromoter(), scShareholding.getpledged(), scShareholding.getscCode()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}
		}
	}

}
