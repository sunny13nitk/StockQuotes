package root.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import root.entity.Customer;
import root.service.CustomerService;
import root.springdemo.Exceptions.CustomersException;

@RestController
@RequestMapping("/api")
public class CustomerRestController
{

	@Autowired
	private CustomerService custSrv;

	@GetMapping("/customers")
	public List<Customer> getCustomers() {
		List<Customer> customers = null;
		if (custSrv != null)
		{
			customers = custSrv.getCustomers();
		}
		return customers;
	}

	@GetMapping("/customers/{customerId}")
	public Customer getCustomerbyId(@PathVariable int customerId) {
		Customer cust = null;
		if (customerId > 0)
		{
			cust = custSrv.getCustomer(customerId);
		}

		if (cust == null)
		{
			throw new CustomersException("No customer found for Id - " + customerId);
		}
		return cust;
	}

	@PostMapping("/customers")
	public Customer createCustomer(@RequestBody Customer newCustomer) {

		if (newCustomer != null)
		{
			if (newCustomer.getFirstName() != null)
			{
				custSrv.saveCustomer(newCustomer);
			}
		} else
		{
			throw new CustomersException("No customer data - Failed Create!");
		}

		return newCustomer;
	}

	@PutMapping("/customers")
	public Customer updateCustomer(@RequestBody Customer theCustomer) {

		if (theCustomer != null)
		{
			if (theCustomer.getId() > 0)
			{
				custSrv.saveCustomer(theCustomer);
			}
		}
		return theCustomer;
	}

	@DeleteMapping("/customers/{custId}")
	public String deleteCustomer(@PathVariable int custId) {

		String msg = null;
		if (custId > 0)
		{
			custSrv.deleteCustomer(custId);
			msg = "Customer id -  " + custId + " deleted Successfully!";
		}

		return msg;
	}
}
