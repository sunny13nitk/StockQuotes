package scriptsengine.uploadengine.updateIdentifiers.implementations;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import modelframework.types.TY_NameValue;
import scriptsengine.constants.SheetNamesConstants;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.implementations.ScripSheetMetadataService;
import scriptsengine.uploadengine.updateIdentifiers.interfaces.IDetermineUpdateEligibility;
import scriptsengine.utilities.types.CurrentFinancialPeriod;

/**
 * 
 * Application Context Specific Service to determine eligibility for scrip update based on Balance Sheet Key vals pairs
 * for the Scrip
 *
 */
@Service("BalSheetUpdateEligibilitySrv")
public class BalSheetUpdateEligibilitySrv implements IDetermineUpdateEligibility
{
	@Autowired
	private MessagesFormatter		msgFormatter;
	@Autowired
	private ScripSheetMetadataService	shtMdtSrv;

	@Override
	public boolean isEligibleforUpdate(ArrayList<TY_NameValue> keyvals) throws EX_General
	{
		if (keyvals != null)
		{
			if (keyvals.size() == 1)
			{
				/**
				 * Get Current Financial period Instance
				 */
				CurrentFinancialPeriod cfp = new CurrentFinancialPeriod();

				/**
				 * Only key in balanceSheet is year - get the value in integer
				 */
				int balSheetMaxYear = (int) keyvals.get(0).Value;

				/**
				 * balancesheet year should be cfp year -2
				 * 
				 * if cfp.getquarter ==1
				 * 
				 * else "qualify as updateNeeded"
				 * 
				 * should be cfp year -1
				 * 
				 * if cfp.getquarter >1
				 * 
				 * else "qualify as updateNeeded"
				 */

				if (cfp.getQuarter() == 1)
				{
					if (!((cfp.getYear() - balSheetMaxYear) <= 2))
						return true;
				}

			}
			else
			{
				// If more than one key i.e. keys except year determined - throw Exception
				EX_General egen = new EX_General("NUMKEYMISMATCH", new Object[]
				{ SheetNamesConstants.BalanceSheet, shtMdtSrv.getIntKeyfieldsforSheet(SheetNamesConstants.BalanceSheet).size(), keyvals.size()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;

			}
		}
		return false;
	}

}
