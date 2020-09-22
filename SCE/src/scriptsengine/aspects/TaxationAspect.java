package scriptsengine.aspects;

import java.util.ArrayList;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import modelframework.implementations.MessagesFormatter;
import modelframework.interfaces.IQueryService;
import modelframework.managers.ModelObjectFactory;
import modelframework.managers.QueryManager;
import modelframework.types.TY_NameValue;
import scriptsengine.taxation.interfaces.ITaxable;
import scriptsengine.taxation.pojos.OB_TaxH;
import scriptsengine.taxation.pojos.OB_TaxI;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.utilities.implementations.FinancialYearUtility;

@Component
@Aspect
public class TaxationAspect
{

	@Autowired
	private static MessagesFormatter	msgFormatter;

	private final String			constRootObjName	= "OB_TaxH";
	private final String			constTaxItemRelName	= "OB_TaxI_Rel";

	@Around("ITaxableInterface() && adjustTaxes()")
	public void handleTaxation(ProceedingJoinPoint pjp) throws Exception
	{
		if (pjp != null)
		{
			if (pjp.getTarget() instanceof ITaxable)
			{
				ITaxable taxItem = (ITaxable) pjp.getTarget();
				if (taxItem != null)
				{
					if (taxItem.getTxnAmount() > 0)
					{
						try

						{
							processTax(taxItem);
						}
						catch (Exception e)
						{
							EX_General egen = new EX_General("ERR_TAX", new Object[]
							{ taxItem.getDesc(), e.getMessage()
							});
							msgFormatter.generate_message_snippet(egen);
							throw egen;
						}

					}

				}
			}
		}
	}

	/**
	 * -------------------------------------------------------------------------
	 * -----------------------------PRIVATE SECTION-----------------------------
	 * -------------------------------------------------------------------------
	 */

	/**
	 * Process Tax Item to Commit Taxes
	 * 
	 * @param taxItem
	 * @throws Exception
	 */
	private void processTax(ITaxable taxItem) throws Exception
	{
		// Get Financial Year for the taxItem Date
		String fyYear = FinancialYearUtility.getFinancialYearforDate(taxItem.getDate());
		if (fyYear != null)
		{
			OB_TaxH taxH = FYExists(fyYear);
			if (taxH == null)
			{
				processNewTaxH(taxItem, fyYear);
			}
			else
			{
				processUpdateTaxH(taxItem, fyYear, taxH);
			}

		}
	}

	/**
	 * REturns True if Financial Year in Question exists
	 * 
	 * @param fyYear
	 * @return
	 * @throws Exception
	 */
	private OB_TaxH FYExists(String fyYear) throws Exception
	{
		OB_TaxH taxH = null;

		IQueryService qs = (IQueryService) QueryManager.getQuerybyRootObjname(constRootObjName);
		if (qs != null)
		{
			ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();

			params.add(new TY_NameValue("FYAsmYear", fyYear));
			String condn = " FYAsmYear = ? ";

			ArrayList<OB_TaxH> taxHeaders = qs.executeQuery(condn, params);

			if (taxHeaders != null)
			{
				if (taxHeaders.size() > 0)
				{
					if (taxHeaders.get(0).getFYAsmYear() != null)
					{
						taxH = taxHeaders.get(0);
					}
				}
			}
		}
		return taxH;
	}

	/**
	 * Create new Tax Header and Item
	 * 
	 * @param taxItem
	 * @throws Exception
	 */
	private void processNewTaxH(ITaxable taxItem, String fYAsmYear) throws Exception
	{
		OB_TaxH taxH = ModelObjectFactory.createObjectbyName(constRootObjName);
		if (taxH != null)
		{
			taxH.setFYAsmYear(fYAsmYear);
			taxH.setAmntPayable(taxItem.getTxnAmount());
			taxH.setSettled(false);

			// Create Item
			OB_TaxI taxI = (OB_TaxI) taxH.Create_RelatedEntity(constTaxItemRelName);
			if (taxI != null)
			{
				taxI.setFYAsmYear(fYAsmYear);
				taxI.setAmount(taxItem.getTxnAmount());
				taxI.setDescription(taxItem.getDesc());
				taxI.setTaxType(taxItem.getTaxType());
			}

			taxH.Save();
		}

	}

	/**
	 * Update Tax Header and Create Tax Item
	 * 
	 * @param taxItem
	 * @throws Exception
	 */
	private void processUpdateTaxH(ITaxable taxItem, String fyAsmYear, OB_TaxH taxH) throws Exception
	{
		taxH.lock();
		if (taxH.switchtoChangeMode())
		{
			taxH.setAmntPayable(taxH.getAmntPayable() + taxItem.getTxnAmount());

			// Create Item
			OB_TaxI taxI = (OB_TaxI) taxH.Create_RelatedEntity(constTaxItemRelName);
			if (taxI != null)
			{
				taxI.setFYAsmYear(fyAsmYear);
				taxI.setAmount(taxItem.getTxnAmount());
				taxI.setDescription(taxItem.getDesc());
				taxI.setTaxType(taxItem.getTaxType());
			}

			taxH.Save();
		}

	}

	/********************************************************************************************************************
	 **************************************** POINTCUT DEFINITIONS SECTION ********************************************
	 ********************************************************************************************************************/

	/**
	 * Pointcut for Implementing Interface 'ITaxable'
	 */

	@Pointcut("target(scriptsengine.taxation.interfaces.ITaxable)")
	public void ITaxableInterface()
	{

	}

	/**
	 * PointCut for adjustTaxes
	 */
	@Pointcut("execution(public void *.adjustTaxes())")
	public void adjustTaxes()
	{

	}
}
