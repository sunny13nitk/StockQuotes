package root.DAO.interfaces;

import java.util.List;

import root.entity.User;

public interface IUserDAO
{
	public User getUserbyUserName(
	        String userName
	);
	
	public User getUserbyUser(
	        User usertoFetch
	);
	
	public void addUser(
	        User newUser
	);
	
	public void updateUser(
	        User exUser
	);
	
	public List<User> getAllUsers(
	);
	
}
