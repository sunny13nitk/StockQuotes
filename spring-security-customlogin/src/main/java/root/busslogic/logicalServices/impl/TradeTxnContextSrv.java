package root.busslogic.logicalServices.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import root.busslogic.entity.FundLineItem;
import root.busslogic.entity.Holding;
import root.busslogic.entity.Trade;
import root.enums.EntityMode;

@Service
@Scope(
        value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS
)
public class TradeTxnContextSrv
{
	private Trade tradeItem;
	
	private Holding holdingItem;
	
	private EntityMode holdingMode;
	
	private FundLineItem flItem;
	
	private int flid;
	
	private int pid;
	
	public Trade getTradeItem(
	)
	{
		return tradeItem;
	}
	
	public void setTradeItem(
	        Trade tradeItem
	)
	{
		this.tradeItem = tradeItem;
	}
	
	public Holding getHoldingItem(
	)
	{
		return holdingItem;
	}
	
	public void setHoldingItem(
	        Holding holdingItem
	)
	{
		this.holdingItem = holdingItem;
	}
	
	public EntityMode getHoldingMode(
	)
	{
		return holdingMode;
	}
	
	public void setHoldingMode(
	        EntityMode holdingMode
	)
	{
		this.holdingMode = holdingMode;
	}
	
	public FundLineItem getFlItem(
	)
	{
		return flItem;
	}
	
	public void setFlItem(
	        FundLineItem flItem
	)
	{
		this.flItem = flItem;
	}
	
	public int getFlid(
	)
	{
		return flid;
	}
	
	public void setFlid(
	        int flid
	)
	{
		this.flid = flid;
	}
	
	public int getPid(
	)
	{
		return pid;
	}
	
	public void setPid(
	        int pid
	)
	{
		this.pid = pid;
	}
	
	public TradeTxnContextSrv(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
}
