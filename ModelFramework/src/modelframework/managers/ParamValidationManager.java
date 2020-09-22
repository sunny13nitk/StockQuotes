package modelframework.managers;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import modelframework.interfaces.IQueryParamsValidator;

/**
 * Query Parameters Validation Service To get handle to ParamValidator Service
 * to validate parameters for a SQL Clause
 * 
 */
@Service("ParamValidationManager")
public class ParamValidationManager implements ApplicationContextAware
{
	private static ApplicationContext Context;

	/**
	 * Get a Handle to Params Validator Interface
	 * 
	 * @param validatorservicebeanName
	 *             - ParamValidatorService Bean Name - String
	 * @param objName
	 *             - Name of Object - String
	 * @return - Handle to Params Validator Interface
	 */
	public static IQueryParamsValidator getParamValidatorService(String validatorservicebeanName, String objName)
	{
		IQueryParamsValidator paramValidator = null;
		if (objName != null && Context != null)
		{
			paramValidator = (IQueryParamsValidator) Context.getBean(validatorservicebeanName);
			if (paramValidator != null)
			{
				paramValidator.Initialize(objName);
			}
		}
		return paramValidator;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		Context = ctxt;

	}
}
