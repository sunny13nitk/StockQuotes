package root.user.dao.interfaces;

import java.util.List;

import root.user.model.User;

public interface IUserDAO
{
	/*
	 * Load User Details by Username - Used in AutBuilderManager Service in Spring Security Config
	 */
	User findByUserName(
	        String userName
	);
	
	/*
	 * SAve the User and assign the default Role (as per DB specification)
	 */
	public void saveUser(
	        User newUser
	);
	
	public List<User> getUsers(
	);
	
}
