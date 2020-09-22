package root.services.interfaces;

import java.util.List;

import root.entity.Customer;

public interface ICustomerSrv
{
	public List<Customer> getCustomers(
	);
	
	public Customer getCustomer(
	        String cQuery, String[] params, Object[] args
	);
	
	public List<Customer> getCustomers(
	        String cQuery, String[] params, Object[] args
	);
	
	public List<Customer> getCustomersbyName(
	        String nametoSearch
	);
	
	public void saveCustomer(
	        Customer customer
	);
	
	public Customer getCustomerbyId(
	        int custId
	);
	
	public void deleteCustomer(
	        int custId
	);
}
