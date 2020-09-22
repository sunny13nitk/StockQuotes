package root.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MySpringMVCDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
{
	
	@Override
	protected Class<?>[] getRootConfigClasses(
	)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected Class<?>[] getServletConfigClasses(
	)
	{
		
		return new Class[]
		{ AppConfig.class }; // Our Application Config Class
	}
	
	@Override
	protected String[] getServletMappings(
	)
	{
		
		return new String[]
		{ "/" };
	}
	
}
