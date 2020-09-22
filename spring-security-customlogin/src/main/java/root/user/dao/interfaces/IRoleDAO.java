package root.user.dao.interfaces;

import java.util.List;

import root.user.model.Role;

public interface IRoleDAO
{
	public List<Role> getRoles(
	);
	
	public Role getRolebyName(
	        String roleToSearch
	);
	
	public void addRole(
	        String roletoAdd
	);
	
	public List<Role> getDefaultRoles(
	);
	
}
