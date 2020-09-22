package root.busslogic.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import root.busslogic.annotations.FundLineBalance;
import root.busslogic.logicalServices.interfaces.IFundLineItemProcessSrv;
import root.busslogic.utilities.UtilAnnotation;

@Aspect
@Component
public class FundLineBalanceAspect
{
	/*
	 * Put All Impl of IFundLineItemProcessSrv Autowired here as per Origin
	 */
	@Autowired
	private IFundLineItemProcessSrv flitemPSrv;
	
	/*
	 * ADVICE- Around Advice on FundLine Balance Annotation Marked Methods
	 */
	@Around(
	    "onFundLineBalanceAnnotation()"
	)
	public void updateFundLineBalance(
	        ProceedingJoinPoint pjp
	) throws Throwable
	{
		
		try
		{
			// Get the Annotation Origin
			FundLineBalance flbAnn = (FundLineBalance) UtilAnnotation.getAnnotationforObjbyAnnType(pjp.getTarget(),
			        FundLineBalance.class);
			if (flbAnn != null)
			{
				// Based on Annotation Origin Scan for expected PJP Args & take Action
				switch (flbAnn.origin())
				{
					case FundLine:
						{
							if (flitemPSrv != null)
							{
								// Process via Fund Line Item Process Service
								flitemPSrv.ProcessFundLineItem(pjp.getArgs());
							}
						}
				}
				
			}
		} catch (Exception e)
		{
			throw e;
		}
		
	}
	
	/*
	 * POINTCUt Expressions
	 */
	@Pointcut(
	    "@annotation(root.busslogic.annotations.FundLineBalance)"
	)
	public void onFundLineBalanceAnnotation(
	)
	{
		
	}
	
}
