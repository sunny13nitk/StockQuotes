package root.user.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import root.user.dao.interfaces.IRoleDAO;
import root.user.dao.interfaces.IUserDAO;
import root.user.helperPOJO.RegUser;
import root.user.model.Role;
import root.user.model.User;
import root.user.services.interfaces.IUserService;

@Service
public class UserService implements IUserService
{
	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	private IRoleDAO roleDAO;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	// Our User Find Strategy based on our User Model DAO
	@Override
	@Transactional
	public User findByUserName(
	        String userName
	)
	{
		
		return userDAO.findByUserName(userName);
	}
	
	/*
	 * Must Return the User Instance (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	@Transactional
	public UserDetails loadUserByUsername(
	        String username
	) throws UsernameNotFoundException
	{
		User user = userDAO.findByUserName(username);
		if (user == null)
		{
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
		        mapRolesToAuthorities(user.getRoles()));
	}
	
	@Override
	@Transactional
	public void saveUser(
	        RegUser newUser
	) throws Exception
	{
		User userFound = this.findByUserName(newUser.getUserName());
		if (userFound == null)
		{
			
			User user = new User(newUser.getUserName(), newUser.getFirstName(), newUser.getLastName(),
			        newUser.getEmail());
			user.setPassword(passwordEncoder.encode(newUser.getPassword()));
			
			user.setRoles(new ArrayList<Role>());
			// Add the default role(s) to the User
			List<Role> defaultRoles = roleDAO.getDefaultRoles();
			for (Role role : defaultRoles)
			{
				user.getRoles().add(role);
			}
			
			userDAO.saveUser(user);
		} else
		{
			throw new Exception(
			        "Duplicate User Error - User with Name - " + newUser.getUserName() + " already Exists!");
		}
		
	}
	
	/*
	 * PRIVATE METHODS
	 */
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(
	        Collection<Role> roles
	)
	{
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
	
}
