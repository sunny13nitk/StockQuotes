package root.DAO.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import root.DAO.interfaces.IUserDAO;
import root.annotations.POJO;
import root.entity.User;

@Repository
@POJO(
        EntityName = "User"
)
public class UserDAO implements IUserDAO
{
	
	@Autowired
	private SessionFactory sFac;
	
	@Override
	public User getUserbyUserName(
	        String userName
	)
	{
		User user = null;
		
		if (sFac != null && userName != null)
		
		{
			if (userName.trim().length() > 0)
			{
				String  lv_uname = userName.toLowerCase();
				Session sess     = sFac.getCurrentSession();
				if (sess != null)
				{
					Query<User> usrQ = sess.createQuery("from User where lower(userName) =: lv_uname", User.class);
					usrQ.setParameter("lv_uname", lv_uname);
					
					user = usrQ.getSingleResult();
				}
			}
		}
		
		return user;
	}
	
	@Override
	public void addUser(
	        User newUser
	)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void updateUser(
	        User exUser
	)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<User> getAllUsers(
	)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public User getUserbyUser(
	        User usertoFetch
	)
	{
		User user = null;
		
		if (sFac != null && usertoFetch != null)
		
		{
			if (usertoFetch.getUserName().trim().length() > 0 && usertoFetch.getPassword().trim().length() > 0)
			{
				String  lv_uname = usertoFetch.getUserName().toLowerCase();
				String  lv_pwd   = usertoFetch.getPassword();
				Session sess     = sFac.getCurrentSession();
				if (sess != null)
				{
					Query<User> usrQ = sess.createQuery(
					        "from User where lower(userName) =: lv_uname AND password =: lv_pwd", User.class);
					usrQ.setParameter("lv_uname", lv_uname);
					usrQ.setParameter("lv_pwd", lv_pwd);
					
					user = usrQ.getSingleResult();
				}
			}
		}
		
		return user;
	}
	
}
