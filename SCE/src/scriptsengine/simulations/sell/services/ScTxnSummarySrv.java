package scriptsengine.simulations.sell.services;

import java.util.Date;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import scriptsengine.simulations.sell.definitions.TY_Qty_Price;
import scriptsengine.simulations.sell.definitions.TY_SCTxn_Summary;
import scriptsengine.simulations.sell.definitions.TY_Sell_Quote;
import scriptsengine.simulations.sell.interfaces.IScTxnSummary;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScTxnSummarySrv implements IScTxnSummary
{

	/**
	 * Generate Summary Object for Sell Quote for a Scrip
	 */
	@Override
	public TY_SCTxn_Summary prepareTxnSummary(TY_Sell_Quote sellQuote)
	{
		TY_SCTxn_Summary scTxnSumm = null;
		if (sellQuote != null)
		{
			if (sellQuote.getSellItems() != null)
			{
				if (sellQuote.getSellItems().size() > 0)
				{
					scTxnSumm = new TY_SCTxn_Summary();
					scTxnSumm.setTotalQty(sellQuote.getSellItems().stream().mapToInt(TY_Qty_Price::getQty).sum());
					scTxnSumm.setTotalAmount(sellQuote.getSellItems().stream().mapToDouble(x -> x.getPrice() * x.getQty()).sum());
					scTxnSumm.setAvgPPU(scTxnSumm.getTotalAmount() / scTxnSumm.getTotalQty());
					if (sellQuote.getScCode() != null)
					{
						scTxnSumm.setScCode(sellQuote.getScCode());
					}
					scTxnSumm.setTxnDate(sellQuote.getSellDate());

				}
			}
		}

		return scTxnSumm;
	}

	@Override
	public TY_SCTxn_Summary prepareTxnSummary(String scCode, int Qty, double sppu, Date date)
	{
		TY_SCTxn_Summary scTxnSumm = null;

		if (Qty > 0 && sppu > 0 && date != null)
		{
			scTxnSumm = new TY_SCTxn_Summary();
			scTxnSumm.setTotalQty(Qty);
			scTxnSumm.setTotalAmount(Qty * sppu);
			scTxnSumm.setAvgPPU(sppu);

			scTxnSumm.setScCode(scCode);

			scTxnSumm.setTxnDate(date);
		}

		return scTxnSumm;
	}

}
