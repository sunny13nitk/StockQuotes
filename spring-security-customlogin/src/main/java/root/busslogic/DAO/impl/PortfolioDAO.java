package root.busslogic.DAO.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import root.busslogic.DAO.interfaces.IPortfolioDAO;
import root.busslogic.SQLProc.entities.Rep_Holdings;
import root.busslogic.SQLProc.entities.Rep_PFSS;
import root.busslogic.entity.FundLine;
import root.busslogic.entity.Holding;
import root.busslogic.entity.Portfolio;
import root.busslogic.entity.Trade;
import root.user.model.User;

@Repository
public class PortfolioDAO implements IPortfolioDAO
{
	@Autowired
	private SessionFactory sFac;
	
	@Override
	public List<Portfolio> getPortfoliosforUser(
	        User user
	)
	{
		List<Portfolio> pfList = null;
		if (sFac != null && user != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				Query<Portfolio> Qpf = sess.createQuery("from Portfolio where uid = :lv_uid", Portfolio.class);
				if (Qpf != null)
				{
					Qpf.setParameter("lv_uid", user.getId());
					pfList = Qpf.getResultList();
				}
			}
		}
		return pfList;
	}
	
	@Override
	public int deletePortfolio(
	        int pfID
	)
	{
		int retVaL = 0;
		
		if (sFac != null && pfID > 0)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				@SuppressWarnings(
				    "unchecked"
				)
				// Modification Queries cannot be strongly typed - Exception
				Query<Portfolio> Qpf = sess.createQuery("delete from Portfolio where pid =:lv_pid");
				if (Qpf != null)
				{
					Qpf.setParameter("lv_pid", pfID);
					retVaL = Qpf.executeUpdate();
				}
			}
		}
		
		return retVaL;
	}
	
	@Override
	public void updatePortfolio(
	        Portfolio updPF
	)
	{
		
		if (sFac != null && updPF != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				sess.saveOrUpdate(updPF);
			}
		}
		
	}
	
	@Override
	public void addPortfolioforUser(
	        Portfolio newPF, User user
	) throws Exception
	{
		if (sFac != null && user != null && newPF != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				List<Portfolio> pfList = getPortfoliosforUser(user);
				if (pfList != null)
				{
					// Check if the User does not has the PF with the same Name already
					if (pfList.stream().anyMatch(x -> x.getName().equals(newPF.getName())))
					{
						throw new Exception("A Portfolio with the name - " + newPF.getName() + " alredy exists for "
						        + user.getUserName() + "!");
					} else
					{
						// Add with the Key
						newPF.setUid(user.getId()); // Link the entities for Hibernate
						sess.save(newPF);
					}
					
				} else // No PF Exists Yet - Simply Create Bag and Add
				{
					// Add with the Key
					newPF.setUid(user.getId()); // Link the entities for Hibernate
					sess.save(newPF);
				}
				
			}
		}
		
	}
	
	@Override
	public FundLine getAssignedFundLine(
	        int pid
	) throws Exception
	{
		FundLine fl = null;
		if (sFac != null && pid > 0)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				
				Query<Portfolio> Qpf = sess.createQuery("from Portfolio where pid = :lv_pid", Portfolio.class);
				if (Qpf != null)
				{
					Qpf.setParameter("lv_pid", pid);
					Portfolio pf = Qpf.getSingleResult();
					
					if (pf != null)
					{
						fl = pf.getFundLine(); // Use JOIN FETCH if this fails
					} else
					{
						throw new Exception("Invalid Portfolio ID -  " + pid + " requested!");
					}
				}
			}
			
		}
		return fl;
	}
	
	@Override
	public void updatePortfolioHeader(
	        Portfolio updPF
	)
	{
		if (sFac != null && updPF != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				@SuppressWarnings(
				    "unchecked"
				)
				// Modification Queries cannot be strongly typed - Exception
				Query<Portfolio> Qpf = sess.createQuery(
				        "update Portfolio P set P.broker =:lv_broker, "
				                + "P.desc =:lv_desc, P.name=:lv_name where P.pid =:lv_pid");
				if (Qpf != null)
				{
					Qpf.setParameter("lv_broker", updPF.getBroker());
					Qpf.setParameter("lv_desc", updPF.getDesc());
					Qpf.setParameter("lv_name", updPF.getName());
					Qpf.setParameter("lv_pid", updPF.getPid());
					Qpf.executeUpdate();
				}
			}
		}
		
	}
	
	@Override
	public List<Holding> getHoldings(
	        int pid
	)
	{
		List<Holding> holdings = null;
		Session       sess     = sFac.getCurrentSession();
		if (sess != null && pid > 0)
		{
			Query<Holding> hQ = sess.createQuery("from Holding where pid =: lv_pid", Holding.class);
			if (hQ != null)
			{
				hQ.setParameter("lv_pid", pid);
				
				holdings = hQ.getResultList();
			}
		}
		
		return holdings;
	}
	
	@Override
	public Holding getHoldingforScrip(
	        int pid, String scCode
	)
	{
		Holding holding = null;
		Session sess    = sFac.getCurrentSession();
		if (sess != null && pid > 0)
		{
			Query<Holding> hQ = sess.createQuery("from Holding where pid =: lv_pid AND scCode =: lv_scCode",
			        Holding.class);
			if (hQ != null)
			{
				hQ.setParameter("lv_pid", pid);
				hQ.setParameter("lv_scCode", scCode);
				
				List<Holding> holdings = hQ.getResultList();
				if (holdings != null)
				{
					if (holdings.size() > 0)
					{
						holding = holdings.get(0);
					}
					
				}
				
			}
		}
		
		return holding;
	}
	
	@Override
	public void createHolding(
	        Holding hlI
	)
	{
		
		Session sess = sFac.getCurrentSession();
		if (sess != null && hlI != null)
		{
			sess.save(hlI);
		}
	}
	
	@SuppressWarnings(
	    "unchecked"
	)
	@Override
	public void updateHolding(
	        Holding hlI
	)
	{
		Session sess = sFac.getCurrentSession();
		if (sess != null && hlI != null)
		{
			Query<Portfolio> Qh = sess.createQuery(
			        "update Holding H set H.numUnits =:lv_numUnits, "
			                + "H.avgPPU =:lv_avgPPU, H.totalInvestment =:lv_totalInv, "
			                + "H.totalDiv =:lv_div, H.adjPPU =:lv_adjPPU, H.plBalance =:lv_plBalance"
			                + " where H.pid =:lv_pid AND H.scCode =:lv_scCode");
			
			if (Qh != null)
			{
				// To Set
				Qh.setParameter("lv_numUnits", hlI.getNumUnits());
				Qh.setParameter("lv_avgPPU", hlI.getAvgPPU());
				Qh.setParameter("lv_totalInv", hlI.getTotalInvestment());
				Qh.setParameter("lv_div", hlI.getTotalDiv());
				Qh.setParameter("lv_adjPPU", hlI.getAdjPPU());
				Qh.setParameter("lv_plBalance", hlI.getPlBalance());
				
				// ON
				Qh.setParameter("lv_pid", hlI.getPid());
				Qh.setParameter("lv_scCode", hlI.getScCode());
				
				// Update
				Qh.executeUpdate();
			}
		}
		
	}
	
	@Override
	public void createTrade(
	        Trade tradeItem
	)
	{
		Session sess = sFac.getCurrentSession();
		if (sess != null && tradeItem != null)
		{
			sess.save(tradeItem);
		}
		
	}
	
	@Override
	public List<Rep_Holdings> getHoldingsReport(
	        int pid
	)
	{
		List<Rep_Holdings> holdings = null;
		
		Session sess = sFac.getCurrentSession();
		if (sess != null && pid > 0)
		{
			@SuppressWarnings(
			    "rawtypes"
			)
			Query query = sess.createSQLQuery(
			        "{CALL GetHoldingsforPF(:pid)}").addEntity(Rep_Holdings.class);
			query.setParameter("pid", pid);
			
			@SuppressWarnings(
			    "rawtypes"
			)
			List result = query.getResultList();
			if (result != null)
			{
				if (result.size() > 0)
				{
					holdings = new ArrayList<Rep_Holdings>();
					for (int i = 0; i < result.size(); i++)
					{
						Rep_Holdings holding = (Rep_Holdings) result.get(i);
						holdings.add(holding);
					}
				}
			}
		}
		
		return holdings;
	}
	
	@Override
	public Rep_PFSS getPfStats(
	        int pid
	)
	{
		Rep_PFSS pfStats = null;
		
		Session sess = sFac.getCurrentSession();
		if (sess != null && pid > 0)
		{
			@SuppressWarnings(
			    "rawtypes"
			)
			Query query = sess.createSQLQuery(
			        "{CALL GETPFStats(:pid)}").addEntity(Rep_PFSS.class);
			query.setParameter("pid", pid);
			
			Object result = query.getSingleResult();
			if (result != null)
			{
				pfStats = (Rep_PFSS) result;
			}
		}
		
		return pfStats;
	}
	
}
