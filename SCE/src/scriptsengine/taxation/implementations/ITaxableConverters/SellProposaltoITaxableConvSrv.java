package scriptsengine.taxation.implementations.ITaxableConverters;

import java.text.DecimalFormat;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import scriptsengine.enums.SCEenums;
import scriptsengine.simulations.sell.definitions.TY_Sell_Proposal;
import scriptsengine.taxation.interfaces.IObjITaxableConvSrv;
import scriptsengine.taxation.interfaces.ITaxable;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Sell Proposal to ITaxable Conversion Service
 *
 */
@Service("SellProposaltoITaxableConvSrv")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SellProposaltoITaxableConvSrv implements IObjITaxableConvSrv, ApplicationContextAware
{

	private ApplicationContext appctxt;

	@Override
	public ITaxable getTaxableObjfromObject(Object pojo) throws EX_General
	{
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		ITaxable taxableObj = null;
		if (pojo != null)
		{
			if (pojo instanceof TY_Sell_Proposal && appctxt != null)
			{
				TY_Sell_Proposal sellProposal = (TY_Sell_Proposal) pojo;
				ITaxable taxObjBean = appctxt.getBean(ITaxable.class);
				if (taxObjBean != null)
				{

					taxObjBean.setDate(sellProposal.getScTxnSummary().getTxnDate());

					taxObjBean.setTaxType(SCEenums.taxType.stockTrade);
					taxObjBean.setTxnAmount(sellProposal.getSellProposalHeader().getPandLPerspective().getTotalTaxliability());
					taxObjBean.setDesc("Scrip Sell - " + sellProposal.getScTxnSummary().getScCode() + " : "
					          + Integer.toString(sellProposal.getScTxnSummary().getTotalQty()) + " units @ Rs. "
					          + df.format(sellProposal.getScTxnSummary().getAvgPPU()) + " per unit");

					return taxObjBean;
				}
			}
		}
		return taxableObj;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		if (ctxt != null)
		{
			this.appctxt = ctxt;
		}

	}

}
