package scriptsengine.portfolio.services.implementations;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.implementations.MessagesFormatter;
import scriptsengine.enums.SCEenums;
import scriptsengine.enums.SCEenums.scripSellMode;
import scriptsengine.portfolio.definitions.TY_Scrip_PositionModel;
import scriptsengine.portfolio.pojos.OB_Positions_Header;
import scriptsengine.portfolio.pojos.OB_Positions_Item;
import scriptsengine.portfolio.services.interfaces.IPF_ScripSellSrv;
import scriptsengine.portfolio.services.interfaces.IPortfolioManager;
import scriptsengine.simulations.sell.definitions.TY_SCTxn_Summary;
import scriptsengine.simulations.sell.definitions.TY_SellProposalI;
import scriptsengine.simulations.sell.definitions.TY_Sell_Proposal;
import scriptsengine.simulations.sell.interfaces.IScTxnSummary;
import scriptsengine.simulations.sell.interfaces.IScripSellItemsGenerator;
import scriptsengine.uploadengine.exceptions.EX_General;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PF_ScripSellSrv implements IPF_ScripSellSrv
{
	@Autowired
	private MessagesFormatter		msgFormatter;
	@Autowired
	private IPortfolioManager		pfMgrSrv;
	@Autowired
	private IScripSellItemsGenerator	sellItemGenSrv;

	@Autowired
	private IScTxnSummary			txnSummSrv;

	private TY_Sell_Proposal			sellProposal;

	private SCEenums.scripSellMode	exeSellMode;

	/*
	 * ------------------------------------- GETTERS AND SETTERS
	 */

	/**
	 * @return the exeSellMode
	 */
	@Override
	public SCEenums.scripSellMode getExeSellMode()
	{
		return exeSellMode;
	}

	@Override
	public void setSellProposal(TY_Sell_Proposal sellProposal)
	{
		this.sellProposal = sellProposal;

	}

	@Override
	public TY_Sell_Proposal getSellProposal()
	{

		return this.sellProposal;
	}

	/*
	 * ------------------ INTERFACE IMPLEMENTATIONS ------------------------------------------
	 */

	@Override
	public void executeScripSell(scripSellMode sellMode) throws Exception
	{

		/*
		 * Sell PROPOSAL would already have been generated at this stage by Scrip Sell FI Aspect
		 */
		if (sellProposal != null)
		{
			// 1. Determine Sell mode to Execute
			determineExeMode(sellMode);

			switch (sellMode)
			{
				case PandL:
					processSell_PLMode();
					break;

				case FreeShares:
					processSell_FreeSharesMode();
					break;

				default:
					processSell_PLMode();
					break;
			}

		}

	}

	@Override
	public void preSellProcessTrigger(Object refData)
	{
		// DO NOT IMPLEMENT - WILL BE HANDLED IN SCRIP SELL FI ASPECT

	}

	@Override
	public void postSellProcessTrigger()
	{
		// DO NOT IMPLEMENT - WILL BE HANDLED IN SCRIP SELL FI ASPECT

	}

	/*******************************************************************************
	 * PRIVATE SECTION
	 *****************************************************************************/

	/**
	 * -----------------------------------------
	 * Determine the Sell Execution Mode
	 * 
	 * @param sellMode
	 *             ------------------------------------------
	 */
	private void determineExeMode(scripSellMode sellMode)
	{
		// TRy for Free Shares first
		if (sellProposal.getSellProposalHeader().getFreeSharesPespective() != null && sellMode == scripSellMode.FreeShares)
		{
			this.exeSellMode = scripSellMode.FreeShares;
		}
		else
		{
			this.exeSellMode = scripSellMode.PandL;
		}
	}

	/**
	 * -------------------------------------------------------------------------
	 * Process Scrip Sale in P&L Mode
	 * 
	 * @throws EX_General
	 * @throws EX_InvalidRelationException
	 *              ------------------------------------------------------------------
	 */
	private void processSell_PLMode() throws EX_General, EX_InvalidRelationException
	{

		if (pfMgrSrv != null)
		{
			TY_Scrip_PositionModel scPosModel = pfMgrSrv.getPositionsModelforScrip(sellProposal.getScTxnSummary().getScCode());
			if (scPosModel != null)
			{

				OB_Positions_Header posH = scPosModel.getScPosHeader();
				if (posH != null)
				{
					posH.lock();
					if (posH.switchtoChangeMode())
					{

						/**
						 * ******************************HEADER Changes**********************
						 */

						/*
						 * FIRST SET THE QUANTITIES CURRENT HOLDINGS AND FREE SHARES----------------
						 */

						// In Case Total Qty is less than equal to Current Holdings
						if (posH.getCurrHolding() >= sellProposal.getScTxnSummary().getTotalQty())
						{
							posH.setCurrHolding(posH.getCurrHolding() - sellProposal.getScTxnSummary().getTotalQty());

						}
						else // Free Shares Also Involved
						{
							int numFreeSharesSold = sellProposal.getScTxnSummary().getTotalQty() - posH.getCurrHolding();
							posH.setFreeHolding(posH.getFreeHolding() - numFreeSharesSold);
							posH.setCurrHolding(0);

						}

						/*
						 * CURRENT INVESTMENT - Based on Current Holdings
						 */
						if (posH.getCurrHolding() > 0)
						{
							posH.setCurrInvestment(posH.getCurrHolding() * posH.getPPU());
						}
						else
						{

							posH.setCurrInvestment(0);
							// If No Holdings REmain - Set the Per Unit Purchase Price to zero
							posH.setPPU(0);
						}

						/*
						 * Realizations = Previous + Current( +ve or -ve)
						 */

						double realization = posH.getAmntRealized() + sellProposal.getSellProposalHeader().getPandLPerspective().getTotalRealization();

						posH.setAmntRealized(realization);

						/**
						 * ******************************ITEM Changes - BUY Items ******************************
						 */
						if (sellProposal.getSellProposalItems() != null && scPosModel.getScPosItems() != null)
						{
							/*
							 * Loop through each of the existing positions -
							 * -- get their position ID - compare with SEll Proposal Items Position ID
							 * ---- in case of a match update the ETQ (Eligible Tax Qty) in positions DB from
							 * Sell Proposal Sell Qty by reducing the
							 */

							for ( OB_Positions_Item posItemDB : scPosModel.getScPosItems() )
							{
								int posIDDB = posItemDB.getPrimaryKey_Int();

								try
								{
									TY_SellProposalI currItem = sellProposal.getSellProposalItems().stream().filter(x -> x.getPosItemNo() == posIDDB).findFirst().get();
									if (currItem != null)
									{
										if (posItemDB.switchtoChangeMode())
										{
											posItemDB.setETQ(posItemDB.getETQ() - currItem.getSellQty());
										}
									}

								}

								catch (NoSuchElementException e)
								{
									// Do Nothing - The Current Item might not have qualified for Selling -
									// It's absolutely fine
								}
							}

						}

						/**
						 * ******************************ITEM Creation- SELL Items ******************************
						 */

						/*
						 * For Each of the Sell Proposal Items create the Position Items
						 */
						for ( TY_SellProposalI propItem : sellProposal.getSellProposalItems() )
						{
							OB_Positions_Item newSellItem = (OB_Positions_Item) posH.Create_RelatedEntity("OB_Positions_Items_Rel");
							if (newSellItem != null)
							{
								newSellItem.setTxnDate(sellProposal.getScTxnSummary().getTxnDate());
								newSellItem.setQtyTxn(propItem.getSellQty());
								newSellItem.setPPUTxn(sellProposal.getScTxnSummary().getAvgPPU());
								newSellItem.setTxnType(SCEenums.txnType.SELL);
								newSellItem.setDematAcNum(propItem.getDematAC());
								newSellItem.setETQ(0);

							}
						}

						/**
						 * FINALLY - TRIGGER SAVE
						 */

						try
						{
							posH.Save();
						}
						catch (Exception e)
						{
							// Error Executing Selling for Scrip
							EX_General msgChgErr = new EX_General("ERR_SELL_SCRIP", new Object[]
							{ sellProposal.getScTxnSummary().getScCode(), sellProposal.getScTxnSummary().getTotalQty(), e.getMessage()
							});
							msgFormatter.generate_message_snippet(msgChgErr);
							throw msgChgErr;
						}
					}
				}
			}
		}
	}

	/**
	 * --------------------------------------------------------------------------------------------
	 * Process Selling In Free Shares Mode - Will need recalibration of Proposal Items Generation
	 * --------------------------------------------------------------------------------------------
	 * 
	 * @throws Exception
	 */
	private void processSell_FreeSharesMode() throws Exception
	{
		if (pfMgrSrv != null)
		{
			TY_Scrip_PositionModel scPosModel = pfMgrSrv.getPositionsModelforScrip(sellProposal.getScTxnSummary().getScCode());
			if (scPosModel != null)
			{

				OB_Positions_Header posH = scPosModel.getScPosHeader();
				if (posH != null)
				{
					posH.lock();

					/**
					 * ******************************HEADER Changes**********************
					 */

					/*
					 * FIRST SET THE QUANTITIES CURRENT HOLDINGS AND FREE SHARES----------------
					 */

					// Only Possible In Case Total Qty to Sell is less than equal to Current Holdings and it
					// does not Involves free Shares
					if (posH.getCurrHolding() >= sellProposal.getScTxnSummary().getTotalQty())
					{
						if (posH.switchtoChangeMode())
						{

							posH.setCurrHolding(posH.getCurrHolding() - sellProposal.getScTxnSummary().getTotalQty());

							/*
							 * CURRENT INVESTMENT - Based on Current Holdings
							 */
							if (posH.getCurrHolding() > 0)
							{
								posH.setCurrInvestment(posH.getCurrHolding() * posH.getPPU());
							}
							else
							{
								posH.setCurrInvestment(0);
								// If No Holdings REmain - Set the Per Unit Purchase Price to zero
								posH.setPPU(0);
							}

							/*
							 * Realizations = Previous + Current( +ve or -ve) if any from P&L Perspective spill
							 */
							double realization = posH.getAmntRealized() + sellProposal.getSellProposalHeader().getFreeSharesPespective().getRealizationSpill();
							posH.setAmntRealized(realization);

							/*
							 * Number of FRee Shares
							 */

							int numFreeShares = posH.getFreeHolding() + sellProposal.getSellProposalHeader().getFreeSharesPespective().getNumFreeShares();
							posH.setFreeHolding(numFreeShares);

							/**
							 * Recalibrate Sell Items as per Actual Sell Qty from Proposal's Free Shares
							 * Perspective
							 */
							if (sellItemGenSrv != null && txnSummSrv != null)
							{
								TY_SCTxn_Summary recal_TxnSummary = txnSummSrv.prepareTxnSummary(sellProposal.getScTxnSummary().getScCode(),
								          sellProposal.getSellProposalHeader().getFreeSharesPespective().getNumActualSale(), sellProposal.getScTxnSummary().getAvgPPU(),
								          sellProposal.getScTxnSummary().getTxnDate());
								if (recal_TxnSummary != null)
								{

									ArrayList<TY_SellProposalI> recal_SellPropItems = sellItemGenSrv.generateSellPorposalItems(recal_TxnSummary);

									if (recal_SellPropItems != null)
									{
										/**
										 * ******************************ITEM Changes - BUY Items
										 * ******************************
										 */
										if (recal_SellPropItems.size() > 0 && scPosModel.getScPosItems() != null)
										{
											/*
											 * Loop through each of the existing positions -
											 * -- get their position ID - compare with SEll Proposal Items
											 * Position ID
											 * ---- in case of a match update the ETQ (Eligible Tax Qty) in
											 * positions DB from
											 * Sell Proposal Sell Qty by reducing the
											 */

											for ( OB_Positions_Item posItemDB : scPosModel.getScPosItems() )
											{
												int posIDDB = posItemDB.getPrimaryKey_Int();

												try
												{
													TY_SellProposalI currItem = recal_SellPropItems.stream().filter(x -> x.getPosItemNo() == posIDDB).findFirst()
													          .get();
													if (currItem != null)
													{
														if (posItemDB.switchtoChangeMode())
														{
															posItemDB.setETQ(posItemDB.getETQ() - currItem.getSellQty());
														}
													}

												}

												catch (NoSuchElementException e)
												{
													// Do Nothing - The Current Item might not have
													// qualified for Selling -
													// It's absolutely fine
												}
											}

											/**
											 * ******************************ITEM Creation- SELL Items
											 * ******************************
											 */

											/*
											 * For Each of the Sell Proposal Items create the Position Items
											 */
											for ( TY_SellProposalI propItem : recal_SellPropItems )
											{
												OB_Positions_Item newSellItem = (OB_Positions_Item) posH.Create_RelatedEntity("OB_Positions_Items_Rel");
												if (newSellItem != null)
												{
													newSellItem.setTxnDate(sellProposal.getScTxnSummary().getTxnDate());
													newSellItem.setQtyTxn(propItem.getSellQty());
													newSellItem.setPPUTxn(sellProposal.getScTxnSummary().getAvgPPU());
													newSellItem.setTxnType(SCEenums.txnType.SELL);
													newSellItem.setDematAcNum(propItem.getDematAC());
													newSellItem.setETQ(0);

												}
											}

											/**
											 * FINALLY - TRIGGER SAVE
											 */

											try
											{
												posH.Save();
											}
											catch (Exception e)
											{
												// Error Executing Selling for Scrip
												EX_General msgChgErr = new EX_General("ERR_SELL_SCRIP", new Object[]
												{ sellProposal.getScTxnSummary().getScCode(), sellProposal.getScTxnSummary().getTotalQty(), e.getMessage()
												});
												msgFormatter.generate_message_snippet(msgChgErr);
												throw msgChgErr;
											}

										}
									}
								}
							}
						}

					}

					else // Free Shares Also Involved - Normal P & L Scenario
					{
						processSell_PLMode();
					}

				}

			}
		}
	}

}
