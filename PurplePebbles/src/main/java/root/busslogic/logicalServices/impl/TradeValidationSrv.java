package root.busslogic.logicalServices.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.busslogic.Services.interfaces.IBrokerTxnFeeSrv;
import root.busslogic.Services.interfaces.IFundLineSrv;
import root.busslogic.Services.interfaces.IPortfolioSrv;
import root.busslogic.entity.FundLine;
import root.busslogic.entity.FundLineItem;
import root.busslogic.entity.Holding;
import root.busslogic.entity.Portfolio;
import root.busslogic.entity.Trade;
import root.busslogic.logicalServices.interfaces.ITRadeValidationSrv;
import root.enums.EntityMode;

@Service
public class TradeValidationSrv implements ITRadeValidationSrv
{
	@Autowired
	private IPortfolioSrv pfSrv;
	
	@Autowired
	private IFundLineSrv flSrv;
	
	@Autowired
	private IBrokerTxnFeeSrv brkFeeSrv;
	
	@Autowired
	private TradeTxnContextSrv tradeCtxtSrv;
	
	@Override
	public void validateTradeforPF(
	        int pid, Trade tradeItem
	) throws Exception
	{
		if (pid > 0 && tradeItem != null && pfSrv != null && flSrv != null && brkFeeSrv != null)
		{
			Portfolio pf = pfSrv.getPorfolioById(pid);
			if (pf != null)
			{
				if (pf.getBroker() != null)
				{
					double txnAmnt = brkFeeSrv.getTxnCost(pf.getBroker(),
					        (tradeItem.getPpu() * tradeItem.getNumUnits()));
					tradeItem.setAmount(txnAmnt + (tradeItem.getPpu() * tradeItem.getNumUnits()));
					tradeItem.setPid(pid);
					
					switch (tradeItem.gettCode())
					{
						case 'B': // Buy
							FundLine fl = pfSrv.getAssignedFundLine(pid);
							Holding hlI = null;
							if (fl != null)
							{
								double flBalance = fl.getBalance();
								if (flBalance < tradeItem.getAmount()) // Negative Balance
								{
									throw new Exception("Insufficient Balance in Fund Line : Rs. " + flBalance
									        + " - for Transaction Amount : Rs. " + tradeItem.getAmount());
								} else // Set Trade & Prepare /Set Holding Item - New or Updated
								{
									tradeCtxtSrv.setTradeItem(tradeItem); // Set Trade Item Updates
									
									// Scan the Holdings for Scrip and Populate HLI in TradeTxn Context
									
									hlI = pfSrv.getHoldingforScrip(pid, tradeItem.getScCode());
									if (hlI != null)
									{
										tradeCtxtSrv.setHoldingMode(EntityMode.Update);
										int    numUnits = hlI.getNumUnits() + tradeItem.getNumUnits();
										double amntInv  = hlI.getTotalInvestment()
										        + (tradeItem.getNumUnits() * tradeItem.getPpu());
										double avgPPU   = amntInv / numUnits;
										amntInv = amntInv + txnAmnt;
										double adjPPU = amntInv / numUnits;
										hlI.setAvgPPU(avgPPU);
										hlI.setAdjPPU(adjPPU);
										hlI.setNumUnits(numUnits);
										hlI.setTotalInvestment(amntInv);
									}
									
									else
									{
										tradeCtxtSrv.setHoldingMode(EntityMode.Create);
										int    numUnits = tradeItem.getNumUnits();
										double amntInv  = (tradeItem.getNumUnits() * tradeItem.getPpu());
										double avgPPU   = amntInv / numUnits;
										amntInv = tradeItem.getAmount();
										double adjPPU = amntInv / numUnits;
										hlI = new Holding();
										
										hlI.setPid(pid); // Set Portfolio Id to Link
										hlI.setScCode(tradeItem.getScCode());
										hlI.setAvgPPU(avgPPU);
										hlI.setAdjPPU(adjPPU);
										hlI.setNumUnits(numUnits);
										hlI.setTotalInvestment(amntInv);
									}
									
									tradeCtxtSrv.setHoldingItem(hlI); // Set Holding Item in Context
									
									FundLineItem flI = new FundLineItem();
									
									flI.setFlid(fl.getFlid());
									flI.setDate(new Date()); // Txn Date - today
									flI.setType('C'); // Credit - Balance in FL Cash decreases
									flI.setAmount(tradeItem.getAmount());
									flI.setDesc("Buy " + tradeItem.getNumUnits() + " units of " + hlI.getScCode()
									        + " @ Rs. " + tradeItem.getPpu() + " /Unit all incl.");
									
									tradeCtxtSrv.setFlItem(flI);
									tradeCtxtSrv.setFlid(fl.getFlid());
									tradeCtxtSrv.setPid(pid);
									
								}
							} else
							{
								throw new Exception("No Transaction Can Be Carried for Portfolio - " + pf.getName()
								        + "as no Fund Line is Associated with the Same!");
							}
							break;
						
						case 'S': // Sell
							fl = pfSrv.getAssignedFundLine(pid);
							hlI = null;
							if (fl != null)
							{
								hlI = pfSrv.getHoldingforScrip(pid, tradeItem.getScCode());
								if (hlI != null)
								{
									
									// Sold Qty more than held Qty - Exception
									if (tradeItem.getNumUnits() > hlI.getNumUnits())
									{
										throw new Exception("Insufficient Qty to Sell. Qty Held - " + hlI.getNumUnits()
										        + " Qty requested to be Sold  - " + tradeItem.getNumUnits());
									}
									
									/*
									 * Calculate and Update P&L Balance for Holding Item
									 */
									
									tradeCtxtSrv.setTradeItem(tradeItem); // Set Trade Item Updates
									double plBal = tradeItem.getAmount() - (hlI.getAdjPPU() * tradeItem.getNumUnits());
									
									tradeCtxtSrv.setHoldingMode(EntityMode.Update);
									int    numUnits = hlI.getNumUnits() - tradeItem.getNumUnits();
									double amntInv  = numUnits * hlI.getAvgPPU();
									/*
									 * You cannot hae double impact, Alread adjusting for txn. cost in fund line
									 * balance, no need to alter Avg and Adj PPU
									 */
									/*
									 * double avgPPU = amntInv / numUnits; amntInv = amntInv + txnAmnt; double adjPPU =
									 * amntInv / numUnits; hlI.setAvgPPU(avgPPU); hlI.setAdjPPU(adjPPU);
									 */
									hlI.setNumUnits(numUnits);
									hlI.setTotalInvestment(amntInv);
									hlI.setPlBalance(hlI.getPlBalance() + plBal); // Update P& L in buffer Entity
									
									tradeCtxtSrv.setHoldingItem(hlI); // Set Holding Item in Context
									
									FundLineItem flI = new FundLineItem();
									
									flI.setFlid(fl.getFlid());
									flI.setDate(new Date()); // Txn Date - today
									flI.setType('D'); // Debit - Balance in FL Cash increases due to Sale
									flI.setAmount((tradeItem.getNumUnits() * tradeItem.getPpu()) - txnAmnt);
									flI.setDesc("Sell " + tradeItem.getNumUnits() + " units of " + hlI.getScCode()
									        + " @ Rs. " + tradeItem.getPpu() + " /Unit all incl.");
									
									tradeCtxtSrv.setFlItem(flI);
									tradeCtxtSrv.setFlid(fl.getFlid());
									tradeCtxtSrv.setPid(pid);
									
								} else
								{
									if (tradeItem.getNumUnits() > 0)
									{
										throw new Exception(
										        "Scrip Code -  " + tradeItem.getScCode()
										                + " not found in Portfolio to Execute Sell!");
									}
								}
							}
							break;
						
						default:
							break;
					}
					
				}
			}
			
		}
	}
	
}
