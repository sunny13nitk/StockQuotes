package root.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import root.entity.Customer;
import root.services.interfaces.ICustomerSrv;

@Controller
@RequestMapping(
    "/customer"
)
public class CustomerController
{
	@Autowired
	private ICustomerSrv custSrv;
	
	/*
	 * ----------------- GET MAPPINGS ----------------------
	 */
	@GetMapping(
	    "/list"
	)
	public String showlistCustomers(
	        Model theModel
	)
	{
		populateCustomers(theModel);
		return "list-customers";
	}
	
	@GetMapping(
	    "/addCustomer"
	)
	public String showaddCustomer(
	        Model theModel
	)
	{
		initiateNewCustomer(theModel);
		return "customer-form";
	}
	
	@GetMapping(
	    "/showupdateCustomer"
	)
	public String showUpdateCustomer(
	        @RequestParam(
	            "customerId"
	        ) int custId, Model theModel
	)
	{
		
		if (custId > 0)
		{
			theModel.addAttribute("customer", custSrv.getCustomerbyId(custId));
		}
		
		return "customer-form";
	}
	
	@GetMapping(
	    "/deleteCustomer"
	)
	public String deleteCustomer(
	        @RequestParam(
	            "customerId"
	        ) int custId
	)
	{
		custSrv.deleteCustomer(custId);
		
		return "redirect:/customer/list";
	}
	
	/*
	 * ----------------- POST MAPPINGS ----------------------
	 */
	@PostMapping(
	    "/saveCustomer"
	)
	public String saveCustomer(
	        @ModelAttribute(
	            "customer"
	        ) Customer customer
	)
	{
		if (custSrv != null)
		{
			custSrv.saveCustomer(customer);
		}
		return "redirect:/customer/list";
	}
	
	@PostMapping(
	    "searchbyName"
	)
	public String searchCustomers(
	        @ModelAttribute(
	            "theSearchName"
	        ) String searchName, Model theModel
	)
	{
		if (custSrv != null)
		{
			theModel.addAttribute("customers", custSrv.getCustomersbyName(searchName));
			theModel.addAttribute("theSearchName", searchName);
		}
		
		return "list-customers";
	}
	
	/*
	 * ---------------- MODELS INSTATIATION -------------------------
	 */
	
	// Populate Customers List
	private void populateCustomers(
	        Model theModel
	)
	{
		if (custSrv != null)
		{
			theModel.addAttribute("customers", custSrv.getCustomers());
			theModel.addAttribute("theSearchName", ""); // Add default blank Name Search Criteria
		}
	}
	
	// Initiate a Blank Customer
	private void initiateNewCustomer(
	        Model theModel
	)
	{
		Customer customer = new Customer();
		theModel.addAttribute("customer", customer);
	}
	
}
