package root.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.DAO.interfaces.ICustomerDAO;
import root.entity.Customer;
import root.services.interfaces.ICustomerSrv;

@Service
public class CustomerSrv implements ICustomerSrv
{
	@Autowired
	private ICustomerDAO custDAO;
	
	@Override
	@Transactional
	public List<Customer> getCustomers(
	)
	{
		List<Customer> customers = null;
		if (custDAO != null)
		{
			customers = custDAO.getCustomers();
		}
		
		return customers;
	}
	
	@Override
	@Transactional
	public void saveCustomer(
	        Customer customer
	)
	{
		custDAO.saveCustomer(customer);
		
	}
	
	@Override
	@Transactional
	public Customer getCustomerbyId(
	        int custId
	)
	{
		return custDAO.getCustomerbyId(custId);
	}
	
	@Override
	@Transactional
	public void deleteCustomer(
	        int custId
	)
	{
		custDAO.deleteCustomer(custId);
		
	}
	
	@Override
	@Transactional
	public Customer getCustomer(
	        String cQuery, String[] params, Object[] args
	)
	{
		return custDAO.getCustomer(cQuery, params, args);
	}
	
	@Override
	@Transactional
	public List<Customer> getCustomers(
	        String cQuery, String[] params, Object[] args
	)
	{
		return custDAO.getCustomers(cQuery, params, args);
	}
	
	@Override
	@Transactional
	public List<Customer> getCustomersbyName(
	        String nametoSearch
	)
	{
		return custDAO.getCustomersbyName(nametoSearch);
	}
	
}
