package modelframework.managers;

import java.sql.SQLException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.interfaces.IRelQueryService;

@Service
public class RelationsQueryManager implements ApplicationContextAware
{

	private static ApplicationContext Context;

	public static IRelQueryService getRelQuerybyRelation(Object invoker, String relName) throws EX_InvalidRelationException, SQLException
	{
		IRelQueryService relqsrv = null;

		if (Context != null && invoker != null && relName != null)
		{
			relqsrv = (IRelQueryService) Context.getBean("RelationsQueryService");
			if (relqsrv != null)
			{
				relqsrv.Initialize(invoker, relName, false);
			}
		}

		return relqsrv;
	}

	public static IRelQueryService getRelQuerybyRelationwithFilter(Object invoker, String relName) throws EX_InvalidRelationException, SQLException
	{
		IRelQueryService relqsrv = null;

		if (Context != null && invoker != null && relName != null)
		{
			relqsrv = (IRelQueryService) Context.getBean("RelationsQueryService");
			if (relqsrv != null)
			{
				relqsrv.Initialize(invoker, relName, true);
			}
		}

		return relqsrv;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException
	{

		Context = context;
	}

}
