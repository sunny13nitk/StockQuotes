package modelframework.definitions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

import modelframework.JAXB.definitions.objschemas.Dependant_Object_Defn;
import modelframework.JAXB.definitions.objschemas.Root_Object_Defn;
import modelframework.exposed.FrameworkManager;
import modelframework.implementations.DependantObject;
import modelframework.implementations.RootObject;
import modelframework.types.TY_NameValue;

/*
 * Class to have Complete Object Details for an Object Instance In terms of It's Getters/Setters/Properties
 * Query Strings --> Insert/Update/Delete/Select
 */
public class Object_Info
	{
		
		/********************************************** Attributes ****************************************/
		/**
		 * Object Name
		 */
		private String						Object_Name;
		
		/**
		 * Root Metadata in Case of Root Object
		 */
		private Root_Object_Defn		Root_Metadata;
		
		/**
		 * Dependant Metadata in Case of Dependant Object
		 */
		private Dependant_Object_Defn	Dep_Metadata;
		
		/**
		 * Current Object Class
		 */
		@SuppressWarnings("rawtypes")
		private Class						Curr_Obj_Class;
		
		/**
		 * List of Getters
		 */
		private ArrayList<Method>		Getters;
		
		/**
		 * List of Setters
		 */
		private ArrayList<Method>		Setters;
		
		/**
		 * List of Properties
		 */
		private ArrayList<String>		Properties;
		
		/**
		 * Queries
		 */
		private Query_Insert				Insert_Query;
		
		private Query_Update				Update_Query;
		
		private Query_Delete				Delete_Query;
		
		// Primary Key
		private String						PKey_Name;
		
		/**********************************************
		 * Getters and Setters
		 *********************************************/
		public ArrayList<Method> getGetters()
			{
				return Getters;
			}
			
		public ArrayList<Method> getSetters()
			{
				return Setters;
			}
			
		public ArrayList<String> getProperties()
			{
				return Properties;
			}
			
		public void setProperties(ArrayList<String> properties)
			{
				Properties = properties;
			}
			
		@SuppressWarnings("rawtypes")
		public Class getCurr_Obj_Class()
			{
				return Curr_Obj_Class;
			}
			
		@SuppressWarnings("rawtypes")
		public void setCurr_Obj_Class(Class curr_Obj_Class)
			{
				Curr_Obj_Class = curr_Obj_Class;
			}
			
		public String getObject_Name()
			{
				return Object_Name;
			}
			
		public Query_Insert getInsert_Query()
			{
				return Insert_Query;
			}
			
		public Query_Update getUpdate_Query()
			{
				return Update_Query;
			}
			
		public Root_Object_Defn getRoot_Metadata()
			{
				return Root_Metadata;
			}
			
		public Dependant_Object_Defn getDep_Metadata()
			{
				return Dep_Metadata;
			}
			
		public String getPKey_Name()
			{
				return PKey_Name;
			}
			
		public Query_Delete getDelete_Query()
			{
				return Delete_Query;
			}
			
		/************************************** Constructors ***********************************************/
		// Blank constructor
		public Object_Info()
			{
				this.Getters = new ArrayList<Method>();
				this.Setters = new ArrayList<Method>();
				this.Properties = new ArrayList<String>();
			}
			
		// Constructor with Object
		public Object_Info(Object Obj) throws Exception
			{
				if (Obj != null)
					{
						try
							{
								@SuppressWarnings("rawtypes")
								Class myObjClass = Obj.getClass();
								this.Object_Name = myObjClass.getSimpleName();
								this.setCurr_Obj_Class(myObjClass);
								
								this.Getters = new ArrayList<Method>();
								this.Setters = new ArrayList<Method>();
								this.Properties = new ArrayList<String>();
								
								// Blank Insert Query for Now- Call Upload
								// Insert Query to Upload Insert Query On Demand
								this.Insert_Query = new Query_Insert();
								
								// Blank Update Query for Now- Call Upload
								// Update Query to Upload Update Query On Demand
								this.Update_Query = new Query_Update();
								
								// Blank Delete Query for Now- Call Upload
								// Delete Query to Upload Delete Query On Demand
								this.Delete_Query = new Query_Delete();
								
								// Initialize the Getters and Setters though
								// since they would be used in getting Queries
								Initialize_Getters_Setters();
								
								// Get The MetaData
								if (Obj instanceof RootObject)
									{
										Dep_Metadata = null;
										// Get from Root Object Model by Object
										// Name from ObjectSchemaFactory
										this.Root_Metadata = FrameworkManager.getObjectSchemaFactory().get_Root_Metadata_byObjName(Object_Name);
										// If Auto Key Set True i.e. Key Not
										// part of Object :
										// Get from Table Field
										if (this.Root_Metadata.getAutokey() == true)
											{
												this.PKey_Name = this.Root_Metadata.getKeyTableField().trim();
											}
										else // Key Part of Object Get from
										     // Object
											{
												this.PKey_Name = this.Root_Metadata.getKeyObjField().trim();
											}
											
									}
								else if (Obj instanceof DependantObject)
									{
										Root_Metadata = null;
										// Get from Dependant Object Model by
										// Object Name
										this.Dep_Metadata = FrameworkManager.getObjectSchemaFactory().get_Dependant_Metadata_byObjName(Object_Name);
										// If Auto Key Set True i.e. Key Not
										// part of Object :
										// Get from Table Field
										if (this.Dep_Metadata.getAutokey() == true)
											{
												this.PKey_Name = this.Dep_Metadata.getKeyTableField();
											}
										else // Key Part of Object Get from
										     // Object
											{
												this.PKey_Name = this.Dep_Metadata.getKeyObjField();
											}
									}
									
							}
						catch (Exception Ex)
							{
								throw new Exception("Error Getting Class Relection for Object! - " + Obj.getClass().getSimpleName());
							}
					}
			}
			
		public Object_Info(String ClassName) throws Exception
			{
				if (ClassName != null)
					{
						try
							{
								
								@SuppressWarnings("rawtypes")
								Class myObjClass = Class.forName(ClassName);
								this.Object_Name = myObjClass.getSimpleName();
								this.setCurr_Obj_Class(myObjClass);
								
								this.Getters = new ArrayList<Method>();
								this.Setters = new ArrayList<Method>();
								this.Properties = new ArrayList<String>();
								
								// Blank Insert Query for Now- Call Upload
								// Insert Query to Upload Insert Query On Demand
								this.Insert_Query = new Query_Insert();
								
								// Blank Update Query for Now- Call Upload
								// Update Query to Upload Update Query On Demand
								this.Update_Query = new Query_Update();
								
								// Blank Delete Query for Now- Call Upload
								// Delete Query to Upload Delete Query On Demand
								this.Delete_Query = new Query_Delete();
								
								// Initialize the Getters and Setters though
								// since they would be used in getting Queries
								Initialize_Getters_Setters();
								
								Object Obj = myObjClass.newInstance();
								
								// Get The MetaData
								if (Obj instanceof RootObject)
									{
										Dep_Metadata = null;
										// Get from Root Object Model by Object
										// Name
										this.Root_Metadata = FrameworkManager.getObjectSchemaFactory().get_Root_Metadata_byObjName(Object_Name);
										// If Auto Key Set True i.e. Key Not
										// part of Object :
										// Get from Table Field
										if (this.Root_Metadata.getAutokey() == true)
											{
												this.PKey_Name = this.Root_Metadata.getKeyTableField().trim();
											}
										else // Key Part of Object Get from
										     // Object
											{
												this.PKey_Name = this.Root_Metadata.getKeyObjField().trim();
											}
											
									}
								else if (Obj instanceof DependantObject)
									{
										Root_Metadata = null;
										// Get from Dependant Object Model by
										// Object Name
										this.Dep_Metadata = FrameworkManager.getObjectSchemaFactory().get_Dependant_Metadata_byObjName(Object_Name);
										// If Auto Key Set True i.e. Key Not
										// part of Object :
										// Get from Table Field
										if (this.Dep_Metadata.getAutokey() == true)
											{
												this.PKey_Name = this.Dep_Metadata.getKeyTableField();
											}
										else // Key Part of Object Get from
										     // Object
											{
												this.PKey_Name = this.Dep_Metadata.getKeyObjField();
											}
									}
									
							}
						catch (Exception Ex)
							{
								throw new Exception("Error Getting Class Relection for Object!");
							}
					}
			}
			
		/*************************************
		 * Other Methods
		 *********************************************/
		// Check Whether property has a Getter in the Class
		// To Check usually before Invoking Query on the Object Model of class
		public boolean IsFieldQuerable(String fname)
			{
				boolean querable = false;
				
				String Getter_Name = ("get" + fname);
				
				if (this.Getters.size() > 0)
					
					{
						for (Method method : Getters)
							{
								if (method.getName() == Getter_Name)
									{
										querable = true;
										return querable;
									}
							}
					}
					
				else
					{
						// Scan methods using Reflection only if Not found in
						// Compiled List
						Method[] Methods = Curr_Obj_Class.getMethods();
						for (Method method : Methods)
							{
								// Method Found
								if (Objects.equals(method.getName(), Getter_Name))
									{
										// Return type of method cannot be void
										// since getter is supposed to always
										// return something
										if (!Void.class.equals(method.getReturnType()))
											{
												querable = true;
												this.Getters.add(method);
												return querable;
											}
									}
							}
							
					}
				return querable;
			}
			
		// Get Getter for Object Property by Name
		public Method Get_Getter_for_FieldName(String fname)
			{
				Method method = null;
				
				String Getter_Name = ("get" + fname);
				
				if (this.Getters.size() > 0)
					
					{
						for (Method method1 : Getters)
							{
								if (Objects.equals(method1.getName(), Getter_Name))
									{
										method = method1;
										return method;
									}
							}
					}
					
				else
					{
						// Scan methods using Reflection only if Not found in
						// Compiled List
						Method[] Methods = Curr_Obj_Class.getMethods();
						for (Method method1 : Methods)
							{
								if (Objects.equals(method1.getName(), Getter_Name))
									{
										
										// Method Found
										if (!Void.class.equals(method1.getReturnType()))
											{
												// Return type of method cannot
												// be void
												// since getter is supposed to
												// always
												// return something
												if (method1.getParameterTypes().length == 0)
													{
														method = method1;
														this.Getters.add(method1);
														return method;
													}
											}
									}
							}
							
					}
					
				return method;
			}
			
		// Setter for Object Property by Field Name
		public Method Get_Setter_for_FieldName(String fname)
			{
				Method method = null;
				String Setter_Name = ("set" + fname);
				
				if (this.Setters.size() > 0)
					
					{
						for (Method method1 : Setters)
							{
								if (Objects.equals(method1.getName(), Setter_Name))
									{
										method = method1;
										return method;
									}
							}
					}
					
				else
					{
						// Scan methods using Reflection only if Not found in
						// Compiled List
						Method[] Methods = Curr_Obj_Class.getMethods();
						for (Method method1 : Methods)
							{
								
								// Method Found
								if (Objects.equals(method1.getName(), Setter_Name))
									{
										// A Setter should always take one
										// parameter of the value to be set
										if (method1.getParameterTypes().length == 1)
											{
												method = method1;
												this.Setters.add(method1);
												return method;
											}
									}
							}
							
					}
					
				return method;
			}
			
		// Initialize Getters and Setters - Call in Constructor
		private void Initialize_Getters_Setters()
			{
				this.Getters.clear();
				this.Setters.clear();
				this.Properties.clear();
				
				Method[] Methods = Curr_Obj_Class.getMethods();
				for (Method method : Methods)
					{
						// Setters
						// Name Starts with set
						if (method.getName().startsWith("set"))
							{
								// Exactly one parameter to be passed to set the
								// member value
								if (method.getParameterTypes().length == 1)
									{
										this.Setters.add(method);
										String prop_name = method.getName().substring(3);
										this.Properties.add(prop_name);
									}
							}
							
						// Getters
						// Name Starts with get or is in case of Boolean suto generated Getter
						if (method.getName().startsWith("get") || method.getName().startsWith("is"))
							{
								// No Parameter passed as intent of getter is to
								// get the value back
								if (method.getParameterTypes().length == 0)
									{
										// Return type in this case should not
										// be void
										if (!void.class.equals(method.getReturnType()))
											{
												this.Getters.add(method);
											}
									}
							}
					}
			}
			
		// Prepare Insert Query Text for the Object by Passing Object Info
		public void Upload_Insert_Query()
			{
				this.Insert_Query = new Query_Insert(this);
			}
			
		// Prepare Update Query Text for the Object by Passing Object Info
		public void Upload_Update_Query(ArrayList<TY_NameValue> Changed_Properties)
			{
				this.Update_Query = new Query_Update(this, Changed_Properties);
			}
			
		// Prepare Delete Query Text for the Object by Passing Object Info
		public void Upload_Delete_Query()
			{
				this.Delete_Query = new Query_Delete(this);
			}
			
		// Determine if the Object has UserAware Annotation - Only Relavant for Root Object
		public Boolean IsUserAware()
			{
				boolean useraware = false;
				
				@SuppressWarnings(
					{ "unchecked" })
					
				Annotation annotation = this.getCurr_Obj_Class().getAnnotation(modelframework.annotations.UserAware.class);
				if (annotation != null)
					{
						useraware = true;
					}
					
				return useraware;
			}
			
	}
