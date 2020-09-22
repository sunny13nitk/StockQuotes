package scriptsengine.uploadengineSC.scContainer.cache;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Service;

import scriptsengine.uploadengineSC.scContainer.interfaces.ISC_Scrip_DataCont_Srv;

@Service("SCKeyGen")
public class SCKeyGen implements KeyGenerator
{

	@Override
	public Object generate(Object target, Method method, Object... params)
	{
		String keyString = "";

		if (target instanceof ISC_Scrip_DataCont_Srv)
		{
			for ( Object obj : params )
			{
				keyString += obj.toString();

			}
		}

		return keyString;

	}

}
