package pp.springboot.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pp.springboot.dao.intf.ICustomerRep;
import pp.springboot.entity.Customer;

@RestController
@RequestMapping(
    "/apij"
)
public class CustomerRestSpDataJPAController
{
	@Autowired
	private ICustomerRep custRep;
	
	@GetMapping(
	    "/customers"
	)
	public List<Customer> getAllCustomers(
	)
	{
		return custRep.findAll();
	}
	
	@GetMapping(
	    "/customers/{customerId}"
	)
	public Customer getCustomerById(
	        @PathVariable int customerId
	)
	{
		Customer cust = null;
		
		Optional<Customer> custO = custRep.findById(customerId);
		if (custO.isPresent())
		{
			cust = custO.get();
		} else
		{
			throw new RuntimeException("Customer with Id - " + customerId + " not found!");
		}
		
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
		custRep.save(newCustomer);
		
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
			if (custRep.findById(customer.getId()) == null)
			{
				throw new RuntimeException("No customer Found! with id -  " + customer.getId());
			} else
			{
				
				custRep.save(customer);
			}
		} else
		{
			throw new RuntimeException("No customer Found!");
		}
		
		// custRep.save(customer);
		return customer;
	}
	
	@DeleteMapping(
	    "/customers/{customerId}"
	)
	public String deleteCustomerById(
	        @PathVariable int customerId
	)
	{
		
		if (custRep.findById(customerId) == null)
		{
			throw new RuntimeException("No customer Found! with id -  " + customerId);
		} else
		{
			custRep.deleteById(customerId);
		}
		
		return "Customer Id -  " + customerId + " deleted Successfully!";
	}
}
