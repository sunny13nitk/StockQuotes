package root.DAO.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import root.DAO.interfaces.ISessionDAO;
import root.entity.SessionH;
import root.entity.SessionI;
import root.services.interfaces.ISessionSrv;

@Repository
public class SessionDAO implements ISessionDAO
{
	@Autowired
	private SessionFactory sFac;
	
	@Autowired
	private ISessionSrv sessSrv;
	
	@Override
	public void saveSession(
	        SessionH session
	)
	{
		if (sFac != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null)
			{
				sess.save(session);
			}
		}
	}
	
	@Override
	public void saveUpdateSession(
	)
	{
		if (sFac != null)
		{
			Session sess = sFac.getCurrentSession();
			if (sess != null && sessSrv != null)
			{
				if (sessSrv.getSessionHeader() != null)
				{
					if (sessSrv.getSessionHeader().getSessItems() != null)
					{
						if (sessSrv.getSessionHeader().getSessItems().size() > 0)
						{
							for (SessionI sessI : sessSrv.getSessionHeader().getSessItems())
							{
								sess.save(sessI);
							}
						}
					}
				}
			}
		}
	}
	
}
