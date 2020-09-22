package root.busslogic.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import root.busslogic.annotations.PFTransaction;
import root.busslogic.logicalServices.interfaces.IBuySellTradeSrv;
import root.busslogic.utilities.UtilAnnotation;

@Aspect
@Component
public class PortfolioTxnAspect
{
	@Autowired
	private IBuySellTradeSrv buySellTradeSrv;
	
	/*
	 * ADVICE- Around Advice on @PFTransaction Annotation Marked Methods
	 */
	@Around(
	    "aroundPfTxnAspect()"
	)
	public void processPfTxn(
	        ProceedingJoinPoint pjp
	) throws Exception
	{
		if (pjp != null)
		{
			try
			{
				// get Annotation Origin to Delegate
				PFTransaction pfTxnAnn = (PFTransaction) UtilAnnotation.getAnnotationforObjbyAnnType(pjp.getTarget(),
				        PFTransaction.class);
				if (pfTxnAnn != null)
				{
					switch (pfTxnAnn.origin())
					{
						case Buy_Sell:
							if (buySellTradeSrv != null)
							{
								buySellTradeSrv.ProcessTrade(pjp.getArgs());
							}
							
							break;
						
						default:
							break;
					}
				}
			} catch (Exception e)
			{
				// Propogate Exception to Calling Client
				throw e;
			}
		}
	}
	
	/*
	 * POINTCUt Expressions
	 */
	@Pointcut(
	    "@annotation(root.busslogic.annotations.PFTransaction)"
	)
	public void aroundPfTxnAspect(
	)
	{
	}
	
}
