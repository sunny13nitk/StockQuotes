package pp.springboot.dao.intf;

import org.springframework.data.jpa.repository.JpaRepository;

import pp.springboot.entity.Customer;

public interface ICustomerRep extends JpaRepository<Customer, Integer>
{
	
}
