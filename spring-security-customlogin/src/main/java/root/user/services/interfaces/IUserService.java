package root.user.services.interfaces;

import org.springframework.security.core.userdetails.UserDetailsService;

import root.user.helperPOJO.RegUser;
import root.user.model.User;

public interface IUserService extends UserDetailsService
{
	// Our User Find Strategy based on our User Model DAO
	User findByUserName(
	        String userName
	);
	
	public void saveUser(
	        RegUser newUser
	) throws Exception;
}
