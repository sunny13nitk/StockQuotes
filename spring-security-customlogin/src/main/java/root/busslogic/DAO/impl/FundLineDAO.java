package root.busslogic.DAO.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import root.busslogic.DAO.interfaces.IFundLineDAO;
import root.busslogic.entity.FundLine;
import root.busslogic.entity.FundLineItem;
import root.user.model.User;

@Repository
public class FundLineDAO implements IFundLineDAO
{
	@Autowired
	private SessionFactory sFac;
	
	@Override
	public List<FundLine> getFundLinesforUser(
	        User user
	)
	{
		List<FundLine> fLList = null;
		if (sFac != null && user != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				Query<FundLine> Qfl = sess.createQuery("from FundLine where uid = :lv_uid", FundLine.class);
				if (Qfl != null)
				{
					Qfl.setParameter("lv_uid", user.getId());
					fLList = Qfl.getResultList();
				}
			}
		}
		return fLList;
	}
	
	@Override
	public int deleteFundLine(
	        int flID
	)
	{
		int retVaL = 0;
		
		if (sFac != null && flID > 0)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				@SuppressWarnings(
				    "unchecked"
				)
				// Modification Queries cannot be strongly typed - Exception
				Query<FundLine> Qfl = sess.createQuery("delete from FundLine where flid = :lv_flid");
				if (Qfl != null)
				{
					Qfl.setParameter("lv_flid", flID);
					retVaL = Qfl.executeUpdate();
				}
			}
		}
		
		return retVaL;
	}
	
	@Override
	public int updateFundLine(
	        FundLine updfl
	)
	{
		int retVaL = 0;
		if (sFac != null && updfl != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				sess.saveOrUpdate(updfl);
			}
		}
		
		return retVaL;
		
	}
	
	@Override
	public void addFundLineforUser(
	        FundLine newfl, User user
	) throws Exception
	{
		if (sFac != null && user != null && newfl != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				List<FundLine> flList = getFundLinesforUser(user);
				if (flList != null)
				{
					// Check if the User does not has the FL with the same Name already
					if (flList.stream().anyMatch(x -> x.getName().equals(newfl.getName())))
					{
						throw new Exception("A FundLine with the name - " + newfl.getName() + " alredy exists for "
						        + user.getUserName() + "!");
					} else
					{
						// Add with the Key
						newfl.setUid(user.getId()); // Link the entities for Hibernate
						sess.save(newfl);
					}
					
				} else // No PF Exists Yet - Simply Create Bag and Add
				{
					// Add with the Key
					newfl.setUid(user.getId()); // Link the entities for Hibernate
					sess.save(newfl);
				}
				
			}
		}
		
	}
	
	@Override
	public FundLine getFundLinebyId(
	        int flid
	)
	{
		FundLine fL = null;
		if (sFac != null && flid > 0)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				Query<FundLine> Qfl = sess.createQuery("from FundLine where flid = :lv_id", FundLine.class);
				if (Qfl != null)
				{
					Qfl.setParameter("lv_id", flid);
					fL = Qfl.getSingleResult();
				}
			}
		}
		
		return fL;
	}
	
	@Override
	public void updateFundLineBalance(
	        int flid, double Balance
	)
	{
		if (sFac != null && flid > 0 && Balance > 0)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				@SuppressWarnings(
				    "unchecked"
				)
				// Modification Queries cannot be strongly typed - Exception
				Query<FundLine> Qpf = sess.createQuery(
				        "update FundLine F set F.balance =:lv_balance where F.flid =:lv_flid");
				if (Qpf != null)
				{
					Qpf.setParameter("lv_balance", Balance);
					Qpf.setParameter("lv_flid", flid);
					
					Qpf.executeUpdate();
				}
			}
		}
		
	}
	
	@Override
	public void addFundLineItem(
	        FundLineItem flI
	)
	{
		if (sFac != null && flI != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				sess.save(flI);
			}
		}
	}
	
}
