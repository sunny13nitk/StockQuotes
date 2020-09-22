package root.user.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import root.user.dao.interfaces.IUserDAO;
import root.user.model.User;

@Repository
public class UserDAO implements IUserDAO
{
	
	@Autowired
	private SessionFactory sFac;
	
	@Override
	public User findByUserName(
	        String userName
	)
	{
		User userFound = null;
		
		if (sFac != null && userName != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null && userName.trim().length() > 0)
			{
				String      lv_uname = userName.toUpperCase();
				Query<User> QUser    = sess.createQuery("from User where upper(userName) =:lv_uname", User.class);
				if (QUser != null)
				{
					QUser.setParameter("lv_uname", lv_uname);
					try
					{
						userFound = QUser.getSingleResult();
					} catch (Exception e)
					{
						// DO Nothing - Necessary to prevent SQL Error!
					}
				}
			}
		}
		
		return userFound;
	}
	
	@Override
	public void saveUser(
	        User newUser
	)
	{
		
		if (sFac != null && newUser != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null && newUser.getUserName().trim().length() > 0)
			{
				
				sess.saveOrUpdate(newUser);
			}
		}
	}
	
	@Override
	public List<User> getUsers(
	)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
