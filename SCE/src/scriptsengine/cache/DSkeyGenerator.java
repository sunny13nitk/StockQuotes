package scriptsengine.cache;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Service;

import scriptsengine.reportsengine.repDS.interfaces.IReportDataSource;

/**
 * Data source Key Generator - Key wil comprise of DS Service name concatenated by the key String
 *
 */
@Service("DSkeyGenerator")
public class DSkeyGenerator implements KeyGenerator
{

	@Override
	public Object generate(Object target, Method method, Object... params)
	{

		String keyString = null;

		if (target instanceof IReportDataSource)
		{
			keyString = target.getClass().getSimpleName();
		}
		for ( Object param : params )
		{
			keyString += param.toString();
		}

		return keyString;
	}

}
