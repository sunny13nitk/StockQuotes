package modelframework.utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import modelframework.definitions.Object_Info;
import modelframework.exposed.FrameworkManager;
import modelframework.implementations.DependantObject;
import modelframework.implementations.RootObject;

@Service("PropertiesMapper")
public class PropertiesMapper implements ApplicationContextAware
{
	private static FrameworkManager frameworkManager;

	@SuppressWarnings(
	{ "unchecked", "static-access"
	})
	public static <T> T addPropertiestoRootProxyBean(RootObject rootBean, RootObject rootObj)
	          throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		if (rootBean != null && rootObj != null)
		{
			if (rootBean instanceof RootObject && rootObj instanceof RootObject)
			{
				// Get the Target Part of Root Bean
				CglibHelper cgHelper = new CglibHelper(rootBean);
				RootObject entrootObj = (RootObject) cgHelper.getTargetObject();
				if (entrootObj != null)
				{
					// Get ObjectInfo for Root Object
					Object_Info rootobjinfo = frameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byClass(entrootObj.getClass());
					if (rootobjinfo != null)
					{
						// For Each Setter
						for ( Method setter : rootobjinfo.getSetters() )
						{
							if (setter != null)
							{
								// Get ParameterName
								String paramName = setter.getName().substring(3);

								// Ignoring ApplicationContext
								if (!paramName.equals("ApplicationContext"))
								{
									// Get Getter Name from ParameterName
									Method getter = rootobjinfo.Get_Getter_for_FieldName(paramName);
									if (getter != null)
									{
										// Get the value for the parameter from POJO
										Object value = getter.invoke(rootObj, new Object[] {});
										if (value != null)
										{
											// Invoke Setter on Bean Target Entity
											setter.invoke(entrootObj, value);
										}
									}
								}

							}
						}

					}

				}

			}
		}

		return (T) rootBean;

	}

	public static <T> T addPropertiestoDependantProxyBean(DependantObject depBean, DependantObject depObj)
	          throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		if (depBean != null && depObj != null)
		{
			if (depBean instanceof DependantObject && depObj instanceof DependantObject)
			{
				// Get the Target Part of Root Bean
				CglibHelper cgHelper = new CglibHelper(depBean);
				DependantObject entdepObj = (DependantObject) cgHelper.getTargetObject();
				if (entdepObj != null)
				{
					// Get ObjectInfo for Root Object
					Object_Info depobjinfo = frameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byClass(entdepObj.getClass());
					if (depobjinfo != null)
					{
						// For Each Setter
						for ( Method setter : depobjinfo.getSetters() )
						{
							if (setter != null)
							{
								// Get ParameterName
								String paramName = setter.getName().substring(3);

								// Ignoring ApplicationContext
								if (!paramName.equals("ApplicationContext"))
								{
									// Get Getter Name from ParameterName
									Method getter = depobjinfo.Get_Getter_for_FieldName(paramName);
									if (getter != null)
									{
										// Get the value for the parameter from POJO
										Object value = getter.invoke(depObj, new Object[] {});
										if (value != null)
										{
											// Invoke Setter on Bean Target Entity
											setter.invoke(entdepObj, value);
										}
									}
								}

							}
						}

					}

				}

			}
		}

		return (T) depBean;

	}

	@SuppressWarnings(
	{ "unchecked", "static-access"
	})
	public static <T> T setPropertiesforRootProxyBean(RootObject rootBean, RootObject rootObj)
	          throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		if (rootBean != null && rootObj != null)
		{
			if (rootBean instanceof RootObject && rootObj instanceof RootObject)
			{
				// Get the Target Part of Root Bean
				CglibHelper cgHelper = new CglibHelper(rootBean);
				RootObject entrootObj = (RootObject) cgHelper.getTargetObject();
				if (entrootObj != null)
				{
					// Get ObjectInfo for Root Object
					Object_Info rootobjinfo = frameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byClass(entrootObj.getClass());
					if (rootobjinfo != null)
					{
						// For Each Setter
						for ( Method setter : rootobjinfo.getSetters() )
						{
							if (setter != null)
							{
								// Get ParameterName
								String paramName = setter.getName().substring(3);

								// Ignoring ApplicationContext
								if (!paramName.equals("ApplicationContext"))
								{
									// Get Getter Name from ParameterName
									Method getter = rootobjinfo.Get_Getter_for_FieldName(paramName);
									if (getter != null)
									{

										// Get the value for the parameter from POJO
										Object value = getter.invoke(rootObj, new Object[] {});
										if (value != null)
										{

											/**
											 * Pkey check from Object Info
											 */
											if (rootobjinfo.getPKey_Name().equals(paramName))
											{
												// Before Setting check if parameter is a primary or foreign
												// key
												// and is already set in Root bean
												// Do not invoke setter again on this property
												Object valuebean = getter.invoke(rootBean, new Object[] {});
												if (valuebean == null)
												{

													setter.invoke(rootBean, value);
												}

											}
											else
											{

												// Invoke Setter on Bean Target Entity
												setter.invoke(rootBean, value);
											}
										}
									}
								}

							}
						}

					}

				}

			}
		}

		return (T) rootBean;

	}

	public static <T> T setPropertiesforRootProxyBeanUpdateMode(RootObject rootBean, RootObject rootObj)
	          throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		if (rootBean != null && rootObj != null)
		{
			if (rootBean instanceof RootObject && rootObj instanceof RootObject)
			{
				// Get the Target Part of Root Bean
				CglibHelper cgHelper = new CglibHelper(rootBean);
				RootObject entrootObj = (RootObject) cgHelper.getTargetObject();
				if (entrootObj != null)
				{
					// Get ObjectInfo for Root Object
					Object_Info rootobjinfo = frameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byClass(entrootObj.getClass());
					if (rootobjinfo != null)
					{
						// For Each Setter
						for ( Method setter : rootobjinfo.getSetters() )
						{
							boolean noset = false;
							if (setter != null)
							{
								// Get ParameterName
								String paramName = setter.getName().substring(3);

								// Ignoring ApplicationContext
								if (!paramName.equals("ApplicationContext"))
								{
									// Get Getter Name from ParameterName
									Method getter = rootobjinfo.Get_Getter_for_FieldName(paramName);
									if (getter != null)
									{

										// Get the value for the parameter from POJO
										Object value = getter.invoke(rootObj, new Object[] {});
										if (value != null)
										{

											/**
											 * Pkey check from Object Info
											 */
											if (rootobjinfo.getPKey_Name().equals(paramName))
											{
												// Before Setting check if parameter is a primary or foreign
												// key
												// and is already set in Root bean
												// Do not invoke setter again on this property
												Object valuebean = getter.invoke(rootBean, new Object[] {});
												if (valuebean == null)
												{
													/**
													 * Not possible to Update a Numerical 0 value from
													 * Excel Sheet as the column might tbe accidently
													 * missed
													 * while maintaining
													 **/
													String setterparamType = setter.getParameterTypes()[0].getSimpleName();
													switch (setterparamType)
													{
													case "Int":
													case "Integer":
														if ((int) value == 0)
														{
															noset = true;
														}

														break;

													case "Double":
													case "double":
														if ((double) value == 0)
														{
															noset = true;
														}
														break;

													}

													if (noset == false)
													{

														setter.invoke(rootBean, value);
													}

												}

											}
											else
											{
												/**
												 * Not possible to Update a Numerical 0 value from
												 * Excel Sheet as the column might tbe accidently
												 * missed
												 * while maintaining
												 **/
												String setterparamType = setter.getParameterTypes()[0].getSimpleName();
												switch (setterparamType)
												{
												case "Int":
												case "Integer":
													if ((int) value == 0)
													{
														noset = true;
													}

													break;

												case "Double":
												case "double":
													if ((double) value == 0)
													{
														noset = true;
													}
													break;

												}

												if (noset == false)
												{

													setter.invoke(rootBean, value);
												}
											}
										}
									}
								}

							}
						}

					}

				}

			}
		}

		return (T) rootBean;

	}

	@SuppressWarnings("static-access")
	public static <T> T setPropertiesforDependantProxyBean(DependantObject depBean, DependantObject depObj)
	          throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		if (depBean != null && depObj != null)
		{
			if (depBean instanceof DependantObject && depObj instanceof DependantObject)
			{
				// Get the Target Part of Root Bean
				CglibHelper cgHelper = new CglibHelper(depBean);
				DependantObject entdepObj = (DependantObject) cgHelper.getTargetObject();
				if (entdepObj != null)
				{
					// Get ObjectInfo for Root Object
					Object_Info depobjinfo = frameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byClass(entdepObj.getClass());
					if (depobjinfo != null)
					{
						// For Each Setter
						for ( Method setter : depobjinfo.getSetters() )
						{
							if (setter != null)
							{
								// Get ParameterName
								String paramName = setter.getName().substring(3);

								// Ignoring ApplicationContext
								if (!paramName.equals("ApplicationContext"))
								{
									// Get Getter Name from ParameterName
									Method getter = depobjinfo.Get_Getter_for_FieldName(paramName);
									if (getter != null)
									{
										// Get the value for the parameter from POJO
										Object value = getter.invoke(depObj, new Object[] {});
										if (value != null)
										{

											/**
											 * Pkey check from Object Info
											 */
											if (depobjinfo.getDep_Metadata().getForeignkeyname().equals(paramName))
											{
												// Before Setting check if parameter is a primary key
												// and is already set in Dependant bean
												// Do not invoke setter again on this property
												Object valuebean = getter.invoke(depBean, new Object[] {});
												if (valuebean == null)
												{

													setter.invoke(depBean, value);
												}

											}
											else
											{

												// Invoke Setter on Bean Target Entity
												setter.invoke(depBean, value);
											}
										}

									}

								}

							}
						}

					}

				}

			}
		}

		return (T) depBean;

	}

	public static <T> T setPropertiesforDependantProxyBeanUpdateMode(DependantObject depBean, DependantObject depObj)
	          throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		if (depBean != null && depObj != null)
		{
			if (depBean instanceof DependantObject && depObj instanceof DependantObject)
			{
				// Get the Target Part of Root Bean
				CglibHelper cgHelper = new CglibHelper(depBean);
				DependantObject entdepObj = (DependantObject) cgHelper.getTargetObject();
				if (entdepObj != null)
				{
					// Get ObjectInfo for Root Object
					Object_Info depobjinfo = frameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byClass(entdepObj.getClass());
					if (depobjinfo != null)
					{
						// For Each Setter
						for ( Method setter : depobjinfo.getSetters() )
						{
							boolean noset = false;
							if (setter != null)
							{
								// Get ParameterName
								String paramName = setter.getName().substring(3);

								// Ignoring ApplicationContext
								if (!paramName.equals("ApplicationContext"))
								{
									// Get Getter Name from ParameterName
									Method getter = depobjinfo.Get_Getter_for_FieldName(paramName);
									if (getter != null)
									{
										// Get the value for the parameter from POJO
										Object value = getter.invoke(depObj, new Object[] {});
										if (value != null)
										{

											/**
											 * Pkey check from Object Info
											 */
											if (depobjinfo.getDep_Metadata().getForeignkeyname().equals(paramName))
											{
												// Before Setting check if parameter is a primary key
												// and is already set in Dependant bean
												// Do not invoke setter again on this property
												Object valuebean = getter.invoke(depBean, new Object[] {});
												if (valuebean == null)
												{
													/**
													 * Not possible to Update a Numerical 0 value from
													 * Excel Sheet as the column might tbe accidently
													 * missed
													 * while maintaining
													 **/
													String setterparamType = setter.getParameterTypes()[0].getSimpleName();
													switch (setterparamType)
													{
													case "Int":
													case "Integer":
														if ((int) value == 0)
														{
															noset = true;
														}

														break;

													case "Double":
													case "double":
														if ((double) value == 0)
														{
															noset = true;
														}
														break;

													}

													if (noset == false)
													{

														setter.invoke(depBean, value);
													}
												}

											}
											else
											{
												/**
												 * Not possible to Update a Numerical 0 value from
												 * Excel Sheet as the column might tbe accidently
												 * missed
												 * while maintaining
												 **/

												String setterparamType = setter.getParameterTypes()[0].getSimpleName();
												switch (setterparamType)
												{
												case "Int":
												case "Integer":
													if ((int) value == 0)
													{
														noset = true;
													}

													break;

												case "Double":
												case "double":
													if ((double) value == 0)
													{
														noset = true;
													}
													break;

												}

												if (noset == false)
												{

													setter.invoke(depBean, value);
												}
											}
										}

									}

								}

							}
						}

					}

				}

			}
		}

		return (T) depBean;

	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		frameworkManager = (FrameworkManager) ctxt.getBean("FrameworkManager");

	}
}
