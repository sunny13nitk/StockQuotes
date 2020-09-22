package scriptsengine.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.customizing.interfaces.IBrokerageFeeSrv;
import scriptsengine.portfolio.definitions.TY_DematSrcAmnt;
import scriptsengine.portfolio.definitions.TY_ScripBuy;
import scriptsengine.portfolio.services.interfaces.IScripBuyService;
import scriptsengine.uploadengine.exceptions.EX_General;

@Aspect
@Component
public class ScripBuyFIAspect
{
	@Autowired
	private MessagesFormatter	msgFormatter;

	@Autowired
	private IBrokerageFeeSrv		brokFeeSrv;

	/**
	 * -------------------------------------------------------------------------------------
	 * Advice to Handle Pre Purchase Execution Process for the Scrip
	 * 
	 * Balance check for current purchase aggregate
	 * if balance not availablein Demat then hold funds from parent source A/C (hold in Demat AC session current
	 * balance property)
	 * if funds available in parent Source A/C
	 * let ptoceed
	 * else
	 * do not ALLOW
	 * 
	 * @param pjp
	 *             - ScripBuy Service Interface
	 * @throws EX_General
	 *              -----------------------------------------------------------------------------
	 */
	@Around("ScripBuyServiceInterface() && prePurchaseProcessTrigger() && args(scripPurchaseDetails)")
	public void executePrePurchaseProcess(ProceedingJoinPoint pjp, TY_ScripBuy scripPurchaseDetails) throws EX_General
	{
		if (pjp != null && scripPurchaseDetails != null)
		{
			if (pjp.getTarget() instanceof IScripBuyService)
			{
				IScripBuyService scBuySrv = (IScripBuyService) pjp.getTarget();
				if (scBuySrv != null && brokFeeSrv != null)
				{
					// Create new Helper Instance for FI Consolidation and Set in Scrip buy Service so it can be
					// referred to in Post Purchase Implmentation
					if (scBuySrv.getScripBuyHelper() == null)
					{
						TY_DematSrcAmnt scbuyhelper = new TY_DematSrcAmnt();
						scbuyhelper.setDematAC(scripPurchaseDetails.getDematAC());
						double totalInv = scripPurchaseDetails.getBuyItems().stream().mapToDouble(x -> x.getPrice() * x.getQty()).sum();
						// Include Brokerage and Taxes also
						double brokTaxes = brokFeeSrv.calculateBrokerageinclTaxes(scripPurchaseDetails.getDematAC(), totalInv);
						if (brokTaxes > 0)
						{
							totalInv += brokTaxes;
						}

						scbuyhelper.setTxnAmount(totalInv);

						/**
						 * total Inv to be validated from Source Account appropriate Exception Triggered and
						 * handled from Portfolio Manager to not Proceed Ahead in case of insufficient balance
						 */

						scBuySrv.setScripBuyHelper(scbuyhelper);

					}

					System.out.println("Pre Purchase triggered for Demat - Amnt :  Rs. " + scBuySrv.getScripBuyHelper().getTxnAmount()
					          + "incl. Brokerage and Taxes.");

				}
			}
		}
	}

	/**
	 * -------------------------------------------------------------------------------------
	 * Advice to Handle Post Purchase Execution Process for the Scrip
	 * Execute after COMMIT
	 * 
	 * Consume/Deduct funds from Demat/Parent source account as assigned in pre step
	 * and post a corresponding Investment Head Txn.
	 * Update Demat A/C current balance to be deducted for amount involved in current purchase
	 * 
	 * @param pjp
	 *             - ScripBuy Service Interface
	 * @throws EX_General
	 *              -----------------------------------------------------------------------------
	 */
	@Around("ScripBuyServiceInterface() && postPurchaseProcessTrigger()")
	public void executePostPurchaseProcess(ProceedingJoinPoint pjp) throws EX_General
	{
		if (pjp != null)
		{
			if (pjp.getTarget() instanceof IScripBuyService)
			{
				IScripBuyService scBuySrv = (IScripBuyService) pjp.getTarget();
				if (scBuySrv != null)
				{
					System.out.println("Post Purchase triggered for Demat - " + scBuySrv.getScripBuyHelper().getDematAC() + "for Amount : "
					          + scBuySrv.getScripBuyHelper().getTxnAmount());
				}
			}
		}
	}

	/********************************************************************************************************************
	 **************************************** POINTCUT DEFINITIONS SECTION ********************************************
	 ********************************************************************************************************************/

	/**
	 * Pointcut for Implementing Interface 'IScripBuyService'
	 */
	@Pointcut("target(scriptsengine.portfolio.services.interfaces.IScripBuyService)")
	public void ScripBuyServiceInterface()
	{

	}

	/**
	 * PointCut for prePurchaseProcessTrigger()
	 */
	@Pointcut("execution( public void *.prePurchaseProcessTrigger(..))")
	public void prePurchaseProcessTrigger()
	{

	}

	/**
	 * PointCut for postPurchaseProcessTrigger()
	 */
	@Pointcut("execution( public void *.postPurchaseProcessTrigger())")
	public void postPurchaseProcessTrigger()
	{

	}

}
