package root.services.interfaces;

import java.util.List;

import root.entity.User;

public interface IUserSrv
{
	public User getUserbyUserName(
	        String userName
	);
	
	public void addUser(
	        User newUser
	);
	
	public void updateUser(
	        User exUser
	);
	
	public List<User> getAllUsers(
	);
	
	public User getUserbyUser(
	        User usertoFetch
	);
}
