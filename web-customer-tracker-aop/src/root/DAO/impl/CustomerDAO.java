package root.DAO.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import root.DAO.interfaces.ICustomerDAO;
import root.annotations.DBMark;
import root.annotations.POJO;
import root.entity.Customer;
import root.enums.Enums.dbOperation;

@Repository
@POJO(
        EntityName = "Customer"
)
public class CustomerDAO implements ICustomerDAO
{
	@Autowired
	private SessionFactory sFac;
	
	@Override
	
	public List<Customer> getCustomers(
	)
	{
		List<Customer> customers = null;
		if (sFac != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				Query<Customer> cQuery = sess.createQuery("from Customer order by lastName", Customer.class);
				if (cQuery != null)
				{
					customers = cQuery.getResultList();
				}
			}
			
		}
		
		return customers;
	}
	
	@Override
	@DBMark(
	        dbOperation = dbOperation.Save, msg = "Customer with details ? saved/Updated!"
	)
	public void saveCustomer(
	        Customer customer
	)
	{
		if (sFac != null && customer != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				sess.saveOrUpdate(customer);
			}
		}
		
	}
	
	@Override
	public Customer getCustomerbyId(
	        int custId
	)
	{
		Customer customer = null;
		if (sFac != null && custId > 0)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				customer = sess.get(Customer.class, custId);
			}
		}
		return customer;
	}
	
	@Override
	@DBMark(
	        dbOperation = dbOperation.Delete, msg = "Customer with Id ? deleted!"
	)
	public void deleteCustomer(
	        int custId
	)
	{
		if (sFac != null && custId > 0)
		{
			
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				Query<Customer> cusQ = sess.createQuery("delete from Customer where id =:lv_custid");
				cusQ.setParameter("lv_custid", custId);
				
				cusQ.executeUpdate();
			}
			
		}
		
	}
	
	@Override
	@Transactional
	public Customer getCustomer(
	        String cQuery, String[] params, Object[] args
	)
	{
		Customer customer = null;
		if (sFac != null && cQuery != null && params.length > 0 && args.length > 0)
		{
			if (params.length == args.length) // # Parameters and Args should Match
			{
				
				Session sess = sFac.getCurrentSession();
				if (sess != null)
				{
					Query<Customer> cusQ = sess.createQuery(cQuery, Customer.class);
					for (int i = 0; i < params.length; i++)
					{
						cusQ.setParameter(params[i], args[i]);
					}
					
					customer = cusQ.getSingleResult();
				}
			}
			
		}
		return customer;
	}
	
	@Override
	public List<Customer> getCustomers(
	        String cQuery, String[] params, Object[] args
	)
	{
		List<Customer> customers = null;
		if (sFac != null && cQuery != null && params.length > 0 && args.length > 0)
		{
			if (params.length == args.length) // # Parameters and Args should Match
			{
				
				Session sess = sFac.getCurrentSession();
				if (sess != null)
				{
					Query<Customer> cusQ = sess.createQuery(cQuery, Customer.class);
					for (int i = 0; i < params.length; i++)
					{
						cusQ.setParameter(params[i], args[i]);
					}
					
					customers = cusQ.getResultList();
				}
			}
			
		}
		return customers;
	}
	
	@Override
	public List<Customer> getCustomersbyName(
	        String nametoSearch
	)
	{
		List<Customer> customers = null;
		
		if (nametoSearch != null && nametoSearch.trim().length() > 0)
		{
			
			String cCusQueryStm = "from Customer where lower(firstName) LIKE :fname OR lower(lastName) LIKE :lname";
			
			String[] params = new String[]
			{ "fname", "lname" };
			
			Object[] args = new Object[]
			{ nametoSearch, nametoSearch };
			
			customers = this.getCustomers(cCusQueryStm, params, args);
		}
		
		else
		{
			customers = getCustomers();
		}
		
		return customers;
	}
	
}
