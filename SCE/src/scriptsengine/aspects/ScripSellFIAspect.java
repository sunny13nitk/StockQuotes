package scriptsengine.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.portfolio.services.interfaces.IPF_ScripSellSrv;
import scriptsengine.simulations.sell.definitions.TY_Sell_Proposal;
import scriptsengine.simulations.sell.definitions.TY_Sell_Quote;
import scriptsengine.simulations.sell.interfaces.IScripSellSimulation;
import scriptsengine.taxation.implementations.TaxManager;
import scriptsengine.taxation.interfaces.ITaxable;
import scriptsengine.uploadengine.exceptions.EX_General;

@Aspect
@Component
public class ScripSellFIAspect implements ApplicationContextAware
{

	@Autowired
	private MessagesFormatter	msgFormatter;

	@Autowired
	private TaxManager			taxManagerSrv;
	private ApplicationContext	appCtxt;

	/**
	 ** -------------------------------------------------------------------------------------
	 * Advice to Handle Pre Selling Execution Process for the Scrip
	 * Trigger Sell Proposal Generation if not yet generated/passed - assign to Sell Service if already passes
	 * 
	 * @param pjp
	 *             - Scrip Sell ServiceInterface
	 * @param refData
	 *             - Scrip Selling Details or Prposal Object
	 * @throws EX_General
	 *              -----------------------------------------------------------------------------
	 */
	@Around("ScripSellServiceInterface() && preSellProcessTrigger() && args(refData)")
	public void executePreSellingProcess(ProceedingJoinPoint pjp, Object refData) throws EX_General
	{
		if (pjp != null)
		{
			if (pjp.getTarget() instanceof IPF_ScripSellSrv)
			{
				IPF_ScripSellSrv scSellSrv = (IPF_ScripSellSrv) pjp.getTarget();
				if (scSellSrv != null)
				{
					// Assign/Trigger the Sell Proposal Generation

					if (scSellSrv.getSellProposal() == null)
					{
						if (refData instanceof TY_Sell_Quote)
						{
							if (appCtxt != null)
							{
								IScripSellSimulation sellSimlSrv = appCtxt.getBean(IScripSellSimulation.class);
								if (sellSimlSrv != null)
								{
									TY_Sell_Proposal sell_Proposal = sellSimlSrv.generateProposalforSellQuote((TY_Sell_Quote) refData);
									if (sell_Proposal != null)
									{
										scSellSrv.setSellProposal(sell_Proposal);

									}
								}
							}
						}

						else if (refData instanceof TY_Sell_Proposal)
						{

							// Validate Sell Proposal before Assigning

							TY_Sell_Proposal sellProposal = (TY_Sell_Proposal) refData;
							if (sellProposal.getSellProposalItems() != null && sellProposal.getSellProposalSummary() != null)
							{
								if (sellProposal.getSellProposalItems().size() > 0 && sellProposal.getSellProposalSummary().size() > 0)
								{
									scSellSrv.setSellProposal(sellProposal);
								}
							}
						}

					}

				}
			}
		}

	}

	/**
	 * ** -------------------------------------------------------------------------------------
	 * Advice to Handle Post Selling Execution Process for the Scrip
	 * 
	 * @param pjp
	 *             - Scrip Sell ServiceInterface
	 * @throws EX_General
	 *              * -----------------------------------------------------------------------------
	 */
	@Around("ScripSellServiceInterface() && postSellProcessTrigger()")
	public void executePostSellingProcess(ProceedingJoinPoint pjp) throws EX_General
	{

		// Handle Accounts Adjustment

		// Handle taxation
		handleTaxation(pjp);
	}

	/********************************************************************************************************************
	 **************************************** PRIVATE METHODS SECTION ********************************************
	 ********************************************************************************************************************/

	/**
	 * HANDLE TAXATION for the Scrip Selling
	 * 
	 * @param pjp
	 * @throws EX_General
	 */
	private void handleTaxation(ProceedingJoinPoint pjp) throws EX_General
	{
		if (pjp.getTarget() instanceof IPF_ScripSellSrv)
		{
			IPF_ScripSellSrv scSellSrv = (IPF_ScripSellSrv) pjp.getTarget();
			if (scSellSrv != null)
			{
				if (scSellSrv.getSellProposal() != null && taxManagerSrv != null)
				{
					// Only if there is a Tax Liability
					if (scSellSrv.getSellProposal().getSellProposalHeader().getPandLPerspective().getTotalTaxliability() > 0)
					{
						// Get the taxation Generic Object
						ITaxable taxObj = taxManagerSrv.getTaxableObjfromObject(scSellSrv.getSellProposal());
						if (taxObj != null)
						{
							taxObj.adjustTaxes(); // Handled via an TaxationAspect
						}
					}
				}
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		if (ctxt != null)
		{
			this.appCtxt = ctxt;
		}

	}

	/********************************************************************************************************************
	 **************************************** POINTCUT DEFINITIONS SECTION ********************************************
	 ********************************************************************************************************************/

	/**
	 * Pointcut for Implementing Interface 'IPF_ScripSellSrv'
	 */
	@Pointcut("target(scriptsengine.portfolio.services.interfaces.IPF_ScripSellSrv)")
	public void ScripSellServiceInterface()
	{

	}

	/**
	 * PointCut for preSellProcessTrigger()
	 */
	@Pointcut("execution( public void *.preSellProcessTrigger(..))")
	public void preSellProcessTrigger()
	{

	}

	/**
	 * PointCut for postSellProcessTrigger()
	 */
	@Pointcut("execution( public void *.postSellProcessTrigger())")
	public void postSellProcessTrigger()
	{

	}

}
