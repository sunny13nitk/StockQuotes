package root.busslogic.logicalServices.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.busslogic.Services.interfaces.IFundLineSrv;
import root.busslogic.entity.FundLine;
import root.busslogic.entity.FundLineItem;
import root.busslogic.logicalServices.interfaces.IFundLineItemProcessSrv;

@Service
public class FLIProcess4mFLISrv implements IFundLineItemProcessSrv
{
	@Autowired
	private IFundLineSrv flSrv;
	
	private Object[]     args;
	private int          flid;
	private FundLineItem flItem;
	private FundLine     fl;
	private double       updatedBal = 0;
	
	@Override
	public void ProcessFundLineItem(
	        Object[] jpArgs
	) throws Exception
	{
		
		try
		{
			// Initialize
			if (jpArgs != null)
			{
				args = jpArgs;
				Initialize();
			}
			
			// Get Fund Line
			getFundLine();
			
			// DO Pre checks
			doPreChecks();
			
			// Update Fund Line
			updateFundLine();
		} catch (Exception e)
		{
			throw new Exception(e);
		}
		
	}
	
	@Override
	public void Initialize(
	)
	{
		/*
		 * Arg 1- Fund Line Id Arg 2- Fund Line Item
		 */
		
		this.flid   = (int) args[0];
		this.flItem = (FundLineItem) args[1];
		
	}
	
	@Override
	public void getFundLine(
	)
	{
		if (this.flid > 0 && flSrv != null)
		{
			this.fl = flSrv.getFundLinebyId(flid);
		}
		
	}
	
	@Override
	public void doPreChecks(
	) throws Exception
	{
		if (this.fl != null && this.flItem != null)
		{
			if (flItem.getType() == 'D')
			{
				updatedBal = fl.getBalance() + flItem.getAmount();
			} else if (flItem.getType() == 'C')
			{
				updatedBal = fl.getBalance() - flItem.getAmount();
			}
			
			if (updatedBal < 0)
			{
				throw new Exception("Negative Balance Error for Fund Line - " + fl.getName() + "> Before Balance : "
				        + fl.getBalance() + " Txn Balance : " + flItem.getAmount() + " for type :" + flItem.getType());
			}
		}
		
	}
	
	@Override
	public void updateFundLine(
	)
	{
		if (flSrv != null)
		{
			this.fl.setBalance(updatedBal);
			this.fl.addFundLineItem(flItem);
			flSrv.updateFundLine(this.fl);
		}
	}
	
}
