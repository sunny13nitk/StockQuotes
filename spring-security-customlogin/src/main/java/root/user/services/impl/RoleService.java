package root.user.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.user.dao.interfaces.IRoleDAO;
import root.user.model.Role;
import root.user.services.interfaces.IRoleService;

@Service
public class RoleService implements IRoleService
{
	@Autowired
	private IRoleDAO roleDAO;
	
	@Override
	@Transactional
	public List<Role> getRoles(
	)
	{
		return roleDAO.getRoles();
	}
	
	@Override
	@Transactional
	public Role getRolebyName(
	        String roleToSearch
	)
	{
		return roleDAO.getRolebyName(roleToSearch);
	}
	
	@Override
	@Transactional
	public void addRole(
	        String roletoAdd
	)
	{
		roleDAO.addRole(roletoAdd);
		
	}
	
	@Override
	@Transactional
	public List<Role> getDefaultRoles(
	)
	{
		return roleDAO.getDefaultRoles();
	}
	
}
