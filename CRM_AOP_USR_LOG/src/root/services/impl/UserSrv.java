package root.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.DAO.interfaces.IUserDAO;
import root.entity.User;
import root.services.interfaces.IUserSrv;

@Service
public class UserSrv implements IUserSrv
{
	@Autowired
	private IUserDAO userDAO;
	
	@Override
	@Transactional
	public User getUserbyUserName(
	        String userName
	)
	{
		return userDAO.getUserbyUserName(userName);
	}
	
	@Override
	@Transactional
	public void addUser(
	        User newUser
	)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	@Transactional
	public void updateUser(
	        User exUser
	)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	@Transactional
	public List<User> getAllUsers(
	)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	@Transactional
	public User getUserbyUser(
	        User usertoFetch
	)
	{
		return userDAO.getUserbyUser(usertoFetch);
	}
	
}
