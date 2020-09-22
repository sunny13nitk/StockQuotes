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
import scriptsengine.utilities.types.PenultimateQYear;

@Service("QtrSheetUpdateEligibilitySrv")
public class QtrSheetUpdateEligibilitySrv implements IDetermineUpdateEligibility
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
			// Year and Quarter
			if (keyvals.size() == 2)
			{
				PenultimateQYear pQYear = new PenultimateQYear();
				/**
				 * year = penultimate qyear and quarter = penultimate quarter
				 * 
				 * else "qualify as updateNeeded"
				 */
				if (!(pQYear.getYear() == (int) keyvals.get(0).Value && pQYear.getQuarter() == (int) keyvals.get(1).Value))
					return true;
			}
			else
			{
				// If more than one key i.e. keys except year determined - throw Exception
				EX_General egen = new EX_General("NUMKEYMISMATCH", new Object[]
				{ SheetNamesConstants.BalanceSheet, shtMdtSrv.getIntKeyfieldsforSheet(SheetNamesConstants.QuarterlyDataSheet).size(),
				          keyvals.size()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;

			}

		}
		return false;
	}

}
