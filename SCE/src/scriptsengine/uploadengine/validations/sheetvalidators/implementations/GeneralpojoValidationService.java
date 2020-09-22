package scriptsengine.uploadengine.validations.sheetvalidators.implementations;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.pricingengine.definitions.TY_AllowancesBean;
import scriptsengine.sectors.services.implementations.SectorsService;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.sheetvalidators.interfaces.ISheetPojoValidator;

@Service("GeneralpojoValidationService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GeneralpojoValidationService implements ISheetPojoValidator
{

	@Autowired
	private SectorsService		secSrv;

	@Autowired
	private MessagesFormatter	msgFormatter;

	@Autowired
	private TY_AllowancesBean	allowanceBean;

	private OB_Scrip_General		scGeneral;

	@SuppressWarnings("unchecked")
	@Override
	public void validatePojosfromSheetEntities(SheetEntities sheetEntities) throws EX_General
	{
		if (sheetEntities != null)
		{
			ArrayList<OB_Scrip_General> generalEnt = sheetEntities.getSheetEntityList();
			if (generalEnt != null)
			{
				scGeneral = generalEnt.get(0);
				executeChecks();
			}
		}

	}

	private void executeChecks() throws EX_General
	{
		if (scGeneral != null)
		{
			// Sector Validation
			if (scGeneral.getscSector() != null)
			{
				if (secSrv != null)
				{
					// Validate if Sector Exists or create if it is a new Sector
					secSrv.createSectorwithexistenceCheck(scGeneral.getscSector());
				}
			}

			// High should be > low

			if (scGeneral.getscHigh() < scGeneral.getscLow())
			{
				EX_General egen = new EX_General("ERRHIGHLOW", new Object[]
				{ this.scGeneral.getscCode()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;

			}

			// 200 DMA should be > 52wk low and less than 52wk High
			if (scGeneral.getsc200DMA() < scGeneral.getscLow() || scGeneral.getsc200DMA() > scGeneral.getscHigh())
			{
				EX_General egen = new EX_General("ERRHIGHLOWDMA", new Object[]
				{ this.scGeneral.getscCode()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}

			// Market Cap should at least be a value greater than allowance value for evan a smallcap
			if (allowanceBean != null)
			{

				if (scGeneral.getmCap() < allowanceBean.getMinMcap())
				{
					EX_General egen = new EX_General("ERRLOWMCAP", new Object[]
					{ this.scGeneral.getscCode(), allowanceBean.getMinMcap(), scGeneral.getmCap()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}

		}
	}

}
