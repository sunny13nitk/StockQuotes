package modelframework.cache;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import modelframework.types.TY_NameValue;
import modelframework.usermanager.managers.UserManager;

/**
 * 
 * Set the Key in Cache for
 ** Object Invoking Query - Simple Name of Invoking Object Class
 * ------ For Simple Query Service--> rootObjName
 * ------ For Relations Query Service-->invoker.getsimpleClassName
 ** Sql Where Condition
 * ------- Contains User Name for User Aware Objects (Cascaded through Root)
 ** Parameters -> Name, Value Arraylist
 */
@Component("QuerykeyGenerator")
public class QuerykeyGenerator implements KeyGenerator
{

	/**
	 * User Manager Session Been wired for User Name addition in Query Cache Key
	 */
	@Autowired
	private UserManager userManager;

	@Override
	public Object generate(Object target, Method method, Object... params)
	{
		CacheKeyMF cacheKey = new CacheKeyMF();
		String keyString = null;

		if (target instanceof CacheQueryManager)
		{

			/**
			 * Key Genration for Simple Query
			 */
			if (((CacheQueryManager) target).getSimpleQ() != null)
			{
				String objName = (((CacheQueryManager) target).getSimpleQ().getRootObjName());

				/**
				 * Add UserName Also to the Key in case the Root Object is UserAware
				 * Refer @UserAware Annotation
				 */
				if (((CacheQueryManager) target).getSimpleQ().getRootObjInfo() != null)
				{
					if (((CacheQueryManager) target).getSimpleQ().getRootObjInfo().IsUserAware())
					{
						if (userManager != null)
						{
							if (userManager.Get_LoggedUser() != null)
							{
								objName += " - " + userManager.Get_LoggedUser();
							}
						}
					}

				}

				cacheKey.setObjName(objName);

				if (((CacheQueryManager) target).getSimpleQ().getDynpart() != null)
				{

					cacheKey.setWhereCondn(((CacheQueryManager) target).getSimpleQ().getDynpart());

					if (((CacheQueryManager) target).getSimpleQ().getQueryParams() != null)
					{
						cacheKey.setParamsValues(((CacheQueryManager) target).getSimpleQ().getQueryParams());
					}

					keyString = cacheKey.objName.toString() + "-" + cacheKey.getWhereCondn().toString();
				}
				else
				{
					keyString = cacheKey.objName.toString();
				}

			}

			/**
			 * Key Generation for Related Entities Query
			 */
			else if (((CacheQueryManager) target).getRelQ() != null)
			{
				/*
				 * Object Name = Relation Name + PKey of Invoker + UserName if UserAware
				 */
				String objName = (((CacheQueryManager) target).getRelQ().getRelDetails().getRelationname() + "-"
				          + ((CacheQueryManager) target).getRelQ().getPkeyValue().toString());

				if (((CacheQueryManager) target).getRelQ().isUserAware() == true)
				{
					if (userManager != null)
					{
						if (userManager.Get_LoggedUser() != null)
						{
							objName += userManager.Get_LoggedUser();
						}
					}

				}

				cacheKey.setObjName(objName);

				/*
				 * Dynamic Query Part- where Condition & Parameters if any
				 */

				if (((CacheQueryManager) target).getRelQ().getDynqpart() != null)
				{
					cacheKey.setWhereCondn(((CacheQueryManager) target).getRelQ().getDynqpart());

					keyString = cacheKey.objName.toString() + "-" + cacheKey.getWhereCondn().toString();
				}
				else
				{
					keyString = cacheKey.objName.toString();
				}

				if (((CacheQueryManager) target).getRelQ().getParams() != null)
				{
					cacheKey.setParamsValues(((CacheQueryManager) target).getRelQ().getParams());
				}

			}

			if (cacheKey.getParamsValues() != null)
			{
				for ( TY_NameValue nameval : cacheKey.getParamsValues() )
				{
					keyString += " - " + nameval.Name.toString() + nameval.Value.toString();
				}
			}
		}

		return keyString;
	}

}
