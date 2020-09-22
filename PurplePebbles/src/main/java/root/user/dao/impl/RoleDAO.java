package root.user.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import root.user.dao.interfaces.IRoleDAO;
import root.user.model.Role;

@Repository // Use Caching Later once graduate to spring boot
public class RoleDAO implements IRoleDAO
{
	@Autowired
	private SessionFactory sFac;
	
	@Override
	public List<Role> getRoles(
	)
	{
		List<Role> roles = null;
		if (sFac != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				Query<Role> QRole = sess.createQuery("from Role", Role.class);
				roles = QRole.getResultList();
				
			}
		}
		
		return roles;
	}
	
	@Override
	public Role getRolebyName(
	        String roleToSearch
	)
	{
		Role role = null;
		if (sFac != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null && roleToSearch != null)
			{
				if (roleToSearch.length() > 0)
				{
					String      roletoSearchUC = roleToSearch.toUpperCase();
					Query<Role> QRole          = sess.createQuery("from Role where upper(name) =:lv_role", Role.class);
					if (QRole != null)
					{
						QRole.setParameter("lv_role", roletoSearchUC);
						role = QRole.getSingleResult();
					}
				}
			}
		}
		
		return role;
	}
	
	@Override
	public void addRole(
	        String roletoAdd
	)
	{
		if (sFac != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				if (roletoAdd != null)
				{
					if (roletoAdd.length() > 0)
					{
						String roletoAddUC = "ROLE_" + roletoAdd.toUpperCase();
						// Search to avoid duplicate Role
						Role roleFound = this.getRolebyName(roletoAddUC);
						if (roleFound == null)
						{
							sess.saveOrUpdate(roletoAddUC);
						}
					}
				}
			}
		}
	}
	
	@Override
	public List<Role> getDefaultRoles(
	)
	{
		List<Role> roles = null;
		if (sFac != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				Query<Role> QRole = sess.createQuery("from Role where is_default = 1", Role.class);
				roles = QRole.getResultList();
				
			}
		}
		
		return roles;
	}
	
}
