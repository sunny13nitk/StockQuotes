package root.busslogic.Services.interfaces;

import java.util.List;

import root.busslogic.entity.Broker;
import root.busslogic.entity.ConfigFinTxn;
import root.busslogic.entity.ConfigTradeTxn;
import root.busslogic.entity.Scrip;

public interface IConfigSrv
{
	
	public void Initialize(
	);
	
	public List<ConfigFinTxn> getFinTxnConfig(
	);
	
	public List<ConfigTradeTxn> getTradeTxnConfig(
	);
	
	public List<Scrip> getScrips(
	);
	
	public List<Broker> getBrokers(
	);
	
	public Broker getBrokerByName(
	        String brokerName
	);
	
}
