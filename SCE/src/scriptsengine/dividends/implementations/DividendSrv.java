package scriptsengine.dividends.implementations;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.implementations.MessagesFormatter;
import scriptsengine.dividends.interfaces.IDividendSrv;
import scriptsengine.dividends.pojos.OB_Dividend_Item;
import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.portfolio.pojos.OB_Positions_Header;
import scriptsengine.portfolio.services.implementations.PortfolioManager;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.implementations.ScripExistsService;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DividendSrv implements IDividendSrv
{
	@Autowired
	private MessagesFormatter	msgFormatter;
	@Autowired
	private DividendCalcSrv		divCalcSrv;
	@Autowired
	private ScripExistsService	scEXSrv;
	@Autowired
	private PortfolioManager		pfmSrv;

	/**
	 * Process Dividend For Scrip
	 */
	@Override
	public void processDividendforScrip(String scCode, Date date, double DPS) throws EX_General
	{
		if (scCode != null && date != null && DPS > 0)
		{
			if (pfmSrv != null && divCalcSrv != null)
			{
				// Get Scrip Positions from Portfolio Manager

				OB_Positions_Header posH = pfmSrv.getPositionsHeaderforScrip(scCode);
				if (posH == null)
				{
					EX_General egen = new EX_General("ERR_INV_SCRIP_PF", new Object[]
					{ scCode
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
				else
				{
					// Get Eligible Quantity for Dividend = Curr Holding + Free Shares
					int nettHoldings = posH.getCurrHolding() + posH.getFreeHolding();
					if (nettHoldings > 0)
					{
						// Determine Dividend w/o Dividend Distribution Tax
						double divAnnounced = nettHoldings * DPS;
						if (divAnnounced > 0)
						{
							// Invoke DDT to get div Credited
							double divCredited = divCalcSrv.getCreditedDividendforAmount(divAnnounced);
							if (divCredited > 0)
							{
								// Update Total in Dividend Header
								posH.lock();
								posH.switchtoChangeMode();

								posH.setDividendEarnings(posH.getDividendEarnings() + divCredited);

								// Create new Dividend Item
								try
								{
									OB_Dividend_Item divItem = (OB_Dividend_Item) posH.Create_RelatedEntity("OB_Positions_Dividend_Rel");
									if (divItem != null)
									{
										divItem.setDPS(DPS);
										divItem.setAmount(divCredited);
										divItem.setTxnDate(date);

										posH.Save();

									}
								}
								catch (EX_InvalidRelationException e)
								{
									EX_General egen = new EX_General("ERR_INVALIDREL", new Object[]
									{ "OB_Positions_Dividend_Rel", "OB_Positions_Header"
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

	}

	@Override
	public void processDividendforScripDesc(String scDesc, Date date, double DPS) throws Exception
	{
		if (scDesc != null)
		{
			if (scEXSrv != null)
			{
				OB_Scrip_General scRoot = scEXSrv.getScripRootbyDescStartingwith(scDesc);
				if (scRoot != null)
				{
					processDividendforScrip(scRoot.getscCode(), date, DPS);
				}
			}
		}

	}

}
