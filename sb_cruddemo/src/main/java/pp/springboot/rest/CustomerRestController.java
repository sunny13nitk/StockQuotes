package pp.springboot.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pp.springboot.entity.Customer;
import pp.springboot.service.intf.ICustomerSrv;

@RestController
@RequestMapping(
    "/api"
)
public class CustomerRestController
{
	@Autowired
	private ICustomerSrv custSrv;
	
	@GetMapping(
	    "/customers"
	)
	public List<Customer> getAllCustomers(
	)
	{
		return custSrv.findAll();
	}
	
	@GetMapping(
	    "/customers/{customerId}"
	)
	public Customer getCustomerById(
	        @PathVariable int customerId
	)
	{
		Customer cust = null;
		
		cust = custSrv.findbyId(customerId);
		
		return cust;
	}
	
	@PostMapping(
	    "/customers"
	)
	public Customer addCustomer(
	        @RequestBody Customer newCustomer
	)
	{
		newCustomer.setId(0); // In case an y other value set by mistake
		custSrv.save(newCustomer);
		
		return newCustomer;
	}
	
	@PutMapping(
	    "/customers"
	)
	public Customer updateCustomer(
	        @RequestBody Customer customer
	)
	{
		
		if (customer.getId() > 0)
		{
			if (custSrv.findbyId(customer.getId()) == null)
			{
				throw new RuntimeException("No customer Found! with id -  " + customer.getId());
			} else
			{
				
				custSrv.save(customer);
			}
		} else
		{
			throw new RuntimeException("No customer Found!");
		}
		
		// custSrv.save(customer);
		return customer;
	}
	
	@DeleteMapping(
	    "/customers/{customerId}"
	)
	public String deleteCustomerById(
	        @PathVariable int customerId
	)
	{
		
		if (custSrv.findbyId(customerId) == null)
		{
			throw new RuntimeException("No customer Found! with id -  " + customerId);
		} else
		{
			custSrv.deleteById(customerId);
		}
		
		return "Customer Id -  " + customerId + " deleted Successfully!";
	}
	
}
