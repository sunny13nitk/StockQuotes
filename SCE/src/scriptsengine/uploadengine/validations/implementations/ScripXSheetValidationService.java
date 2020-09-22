package scriptsengine.uploadengine.validations.implementations;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.constants.SheetNamesConstants;
import scriptsengine.pojos.OB_Scrip_BalSheet;
import scriptsengine.statistics.definitions.StDevResult;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.interfaces.IXSheetValidator;

@Service("ScripXSheetValidationService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripXSheetValidationService implements IXSheetValidator
{

	@Autowired
	private MessagesFormatter		msgFormatter;

	@SuppressWarnings("rawtypes")
	private ArrayList<SheetEntities>	wbEntities;

	@SuppressWarnings(
	{ "rawtypes"
	})
	@Override
	public void validateXSheets(ArrayList<SheetEntities> wbEntities) throws EX_General
	{
		SheetEntities shEntBalSheet;

		SheetEntities shEntPrices;

		/**
		 * Align Prices to Balancesheet entities for Years if both sheets exists
		 */

		if (wbEntities != null)
		{
			this.wbEntities = wbEntities;
			try
			{
				shEntPrices = wbEntities.stream().filter(x -> x.getSheetName().equals(SheetNamesConstants.PricesSheet)).findFirst().get();
				if (shEntPrices != null)
				{
					shEntBalSheet = wbEntities.stream().filter(x -> x.getSheetName().equals(SheetNamesConstants.BalanceSheet)).findFirst().get();
					if (shEntBalSheet != null)
					{
						// Number of Rows must be same first across two relations
						if (shEntBalSheet.getSheetEntityList().size() == shEntPrices.getSheetEntityList().size())
						{
							validate_realignPriceBalSheet(shEntPrices, shEntBalSheet);
						}
						else
						{
							EX_General egen = new EX_General("ERRPRBAL", new Object[]
							{ shEntBalSheet.getSheetEntityList().size(), shEntPrices.getSheetEntityList().size()
							});
							msgFormatter.generate_message_snippet(egen);
							throw egen;
						}

					}
				}
			}
			catch (NoSuchElementException e)
			{
				// do Nothing if the sheets are not found - Can be an update Scenario w/o the sheets
			}

		}

	}

	@SuppressWarnings(
	{ "unchecked", "rawtypes"
	})
	private void validate_realignPriceBalSheet(SheetEntities prices, SheetEntities balSheetentities) throws EX_General
	{
		ArrayList<OB_Scrip_BalSheet> balSheetEnts = balSheetentities.getSheetEntityList();

		ArrayList<StDevResult> pricesEnts = prices.getSheetEntityList();

		// Years in Prices Sheet must match Years in Balance Sheet

		for ( StDevResult stDevResult : pricesEnts )
		{
			int year = Integer.parseInt(stDevResult.getTitle());
			// Search for same year in Balance Sheet
			try
			{
				OB_Scrip_BalSheet balEnt = balSheetEnts.stream().filter(x -> x.getyear() == year).findFirst().get();
				balEnt.setavgPrice(stDevResult.getSpread_high());
				balEnt.setpriceDeviation(stDevResult.getRsd());
			}
			catch (NoSuchElementException e)
			{
				EX_General egen = new EX_General("ERRPRBALYRNOTFOUND", new Object[]
				{ year
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}
		}
	}

}
