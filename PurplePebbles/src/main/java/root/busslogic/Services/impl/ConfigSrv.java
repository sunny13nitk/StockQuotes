package root.busslogic.Services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.busslogic.Services.interfaces.IConfigSrv;
import root.busslogic.entity.Broker;
import root.busslogic.entity.ConfigDuration;
import root.busslogic.entity.ConfigFinTxn;
import root.busslogic.entity.ConfigTradeTxn;
import root.busslogic.entity.Scrip;

@Service
public class ConfigSrv implements IConfigSrv
{
	@Autowired
	private SessionFactory sFac;
	
	private List<ConfigFinTxn> ftxnConfig;
	
	private List<ConfigTradeTxn> TtxnConfig;
	
	private List<Scrip> scrips;
	
	private List<Broker> brokers;
	
	private List<ConfigDuration> durations;
	
	@Override
	@Transactional
	public void Initialize(
	)
	{
		if (sFac != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				Query<ConfigFinTxn> qF = sess.createQuery("from ConfigFinTxn", ConfigFinTxn.class);
				if (qF != null)
				{
					this.ftxnConfig = qF.getResultList();
				}
				
				Query<ConfigTradeTxn> qT = sess.createQuery("from ConfigTradeTxn", ConfigTradeTxn.class);
				if (qT != null)
				{
					this.TtxnConfig = qT.getResultList();
				}
				
				Query<Scrip> qS = sess.createQuery("from Scrip", Scrip.class);
				if (qS != null)
				{
					this.scrips = qS.getResultList();
				}
				
				Query<Broker> qB = sess.createQuery("from Broker", Broker.class);
				if (qB != null)
				{
					this.brokers = qB.getResultList();
				}
				
				Query<ConfigDuration> qD = sess.createQuery("from ConfigDuration", ConfigDuration.class);
				if (qD != null)
				{
					this.durations = qD.getResultList();
				}
			}
		}
		
	}
	
	@Override
	@Transactional
	public List<ConfigFinTxn> getFinTxnConfig(
	)
	{
		if (this.ftxnConfig == null)
		{
			Initialize();
		}
		
		return this.ftxnConfig;
	}
	
	@Override
	@Transactional
	public List<ConfigTradeTxn> getTradeTxnConfig(
	)
	{
		if (this.TtxnConfig == null)
		{
			Initialize();
		}
		
		return this.TtxnConfig;
	}
	
	@Override
	@Transactional
	public List<Scrip> getScrips(
	)
	{
		if (this.TtxnConfig == null)
		{
			Initialize();
		}
		
		return this.scrips;
	}
	
	@Override
	@Transactional
	public List<Broker> getBrokers(
	)
	{
		if (this.brokers == null)
		{
			Initialize();
		}
		
		return this.brokers;
	}
	
	@Override
	@Transactional
	public Broker getBrokerByName(
	        String brokerName
	)
	{
		Broker broker = null;
		
		if (brokerName != null)
		{
			if (brokerName.trim().length() > 0)
			{
				if (this.brokers == null)
				{
					Initialize();
				}
				broker = this.brokers.stream().filter(x -> x.getBrokerCode().equals(brokerName)).findFirst().get();
			}
		}
		
		return broker;
	}
	
	@Override
	public List<ConfigDuration> getDurations(
	)
	{
		if (this.durations == null)
		{
			Initialize();
		}
		
		return this.durations;
	}
	
}
