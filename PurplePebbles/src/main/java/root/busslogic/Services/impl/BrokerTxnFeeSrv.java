package root.busslogic.Services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.busslogic.Services.interfaces.IBrokerTxnFeeSrv;
import root.busslogic.Services.interfaces.IConfigSrv;
import root.busslogic.entity.Broker;

@Service
public class BrokerTxnFeeSrv implements IBrokerTxnFeeSrv
{
	@Autowired
	private IConfigSrv configSrv;
	
	private double brokerage;
	private double stt;
	private double txnCharges;
	private double gst;
	private double sebiCharges;
	private double nettTxnCost;
	
	@Override
	public double getTxnCost(
	        String brokerName, double txnAmount
	) throws Exception
	{
		if (brokerName != null && txnAmount > 0 && configSrv != null)
		{
			initialize();
			
			Broker broker = configSrv.getBrokerByName(brokerName);
			if (broker != null)
			{
				if (broker.getBrokerage() > 0)
				{
					brokerage = txnAmount * (broker.getBrokerage() / 100);
					if (brokerage < broker.getBrokerageminm())
					{
						brokerage = broker.getBrokerageminm();
					}
				} else
				{
					brokerage = 0;
				}
				
				this.stt         = txnAmount * (broker.getSTTcharges() / 100);
				this.txnCharges  = txnAmount * (broker.getTxncharges() / 100);
				this.gst         = (broker.getGSTcharges() / 100) * (this.brokerage + this.txnCharges);
				this.sebiCharges = txnAmount * broker.getSEBIcharges();
				
				nettTxnCost = this.brokerage + this.stt + this.txnCharges + this.gst + this.sebiCharges;
			}
			
		}
		return this.nettTxnCost;
	}
	
	private void initialize(
	)
	{
		brokerage   = 0;
		stt         = 0;
		txnCharges  = 0;
		gst         = 0;
		sebiCharges = 0;
		nettTxnCost = 0;
	}
	
}
