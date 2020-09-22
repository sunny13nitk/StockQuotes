package modelframework.definitions;

import java.lang.reflect.Method;
import java.util.ArrayList;

import modelframework.exposed.FrameworkManager;
import modelframework.types.TY_NameValue;

// Insert query Class - Holds the Insert Query String for a Object
// can be instantiated by passing the object class name
public class Query_Insert
	{
		private String Insert_Query_Text;
		
		public String getInsert_Query_Text()
			{
				return Insert_Query_Text;
			}
			
		public void setInsert_Query_Text(String insert_Query_Text)
			{
				Insert_Query_Text = insert_Query_Text;
			}
			
		/********* Constructors ******************/
		
		// Blank constructor
		public Query_Insert()
			{
				
			}
			
		// Generate Query for Insertion by passing Object Instance
		public Query_Insert(Object Instance) throws Exception
			{
				// First Get the Object Information
				Object_Info Obj_Info;
				
				// First try to get from Framework Manager
				Obj_Info = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(Instance.getClass().getSimpleName());
				
				if (Obj_Info == null)
					{
						// Create a new instance and add to Framework Manager
						// Otherwise
						Obj_Info = new Object_Info(Instance);
						FrameworkManager.getObjectsInfoFactory().Add_ObjectInfo_ToModel(Obj_Info);
					}
				if (Obj_Info != null)
					{
						Prepare_Insert_Query(Obj_Info);
					}
			}
			
		// Generate Query for Insertion by Passing Object Info Instance
		public Query_Insert(Object_Info Obj_Info)
			{
				
				// First try to check if loaded in Framework Manager
				boolean loaded = FrameworkManager.getObjectsInfoFactory().IS_Object_Present(Obj_Info.getObject_Name());
				
				if (loaded == false)
					{
						// Add to Framework Manager
						FrameworkManager.getObjectsInfoFactory().Add_ObjectInfo_ToModel(Obj_Info);
					}
				if (Obj_Info != null)
					{
						Prepare_Insert_Query(Obj_Info);
					}
			}
			
		// Generate Query for Insertion by Passing Object Info Instance
		public Query_Insert(Object_Info Obj_Info, ArrayList<TY_NameValue> CH_Props, EntityMetadata entMdt)
			{
				
				// First try to check if loaded in Framework Manager
				boolean loaded = FrameworkManager.getObjectsInfoFactory().IS_Object_Present(Obj_Info.getObject_Name());
				
				if (loaded == false)
					{
						// Add to Framework Manager
						FrameworkManager.getObjectsInfoFactory().Add_ObjectInfo_ToModel(Obj_Info);
					}
				if (Obj_Info != null)
					{
						Prepare_Insert_Query(Obj_Info, CH_Props, entMdt);
					}
			}
			
		// Generate Query by passing Entity Metadata - To be Added
		
		// Other Methods
		
		private void Prepare_Insert_Query(Object_Info Obj_Info)
			{
				ArrayList<Method> Setters = Obj_Info.getSetters();
				int Num_Setters = Setters.size();
				if (Num_Setters > 0)
					{
						String Query_Start = "INSERT INTO  ";
						String TableName = "";
						String Bracket_Start = " ( ";
						String Bracker_End = " )";
						String Query_Var = "";
						String Query_Fnames = "";
						String space = " ";
						String comma = ",";
						String values = " VALUES ";
						
						for (int i = 1; i < Num_Setters; i++)
							{
								Query_Var += " ?,";
							}
						Query_Var += " ?";
						
						int i = 1;
						for (Method Setter : Setters)
							{
								
								String Setter_Name = Setter.getName();
								String prop_name = Setter_Name.substring(3);
								Query_Fnames += prop_name;
								if (i < Num_Setters)
									{
										Query_Fnames += comma;
									}
								i++;
							}
							
						if (Obj_Info.getRoot_Metadata() != null)
							{
								TableName = Obj_Info.getRoot_Metadata().getTablename().trim();
							}
						else if (Obj_Info.getDep_Metadata() != null)
							{
								TableName = Obj_Info.getDep_Metadata().getTablename().trim();
							}
							
						this.Insert_Query_Text = Query_Start + TableName + space + Bracket_Start + Query_Fnames + Bracker_End + values + space
						      + Bracket_Start + Query_Var + Bracker_End;
					}
			}
			
		private void Prepare_Insert_Query(Object_Info Obj_Info, ArrayList<TY_NameValue> CH_Props, EntityMetadata entMdt)
			{
				if (CH_Props != null)
					{
						if (CH_Props.size() > 0)
							{
								String fQuery_Var = null;
								
								int Num_Prop = CH_Props.size();
								if (Num_Prop > 0)
									{
										String Query_Start = "INSERT INTO  ";
										String TableName = "";
										String Bracket_Start = " ( ";
										String Bracker_End = " )";
										String Query_Var = "";
										String Query_Fnames = "";
										String space = " ";
										String comma = ",";
										String values = " VALUES ";
										
										int i = 1;
										for (TY_NameValue name_val : CH_Props)
											{
												String prop_name = name_val.Name;
												Query_Var += " ?,";
												Query_Fnames += prop_name;
												if (i < Num_Prop)
													{
														Query_Fnames += comma;
													}
												i++;
												
											}
											
										/**
										 * Check for UserAware - If True than add to Change Properties
										 * If Object is Root Refer it's
										 */
										if (entMdt.isUserAware() == true)
											{
												Query_Fnames += ", SYSUSER ";
												
												fQuery_Var = Query_Var + " ?";
											}
										else
											{
												// final formatting
												
												fQuery_Var = Query_Var.substring(0, (Query_Var.length() - 1));
											}
											
										if (Obj_Info.getRoot_Metadata() != null)
											{
												TableName = Obj_Info.getRoot_Metadata().getTablename().trim();
											}
										else if (Obj_Info.getDep_Metadata() != null)
											{
												TableName = Obj_Info.getDep_Metadata().getTablename().trim();
											}
											
										this.Insert_Query_Text = Query_Start + TableName + space + Bracket_Start + Query_Fnames + Bracker_End + values + space
										      + Bracket_Start + fQuery_Var + Bracker_End;
									}
							}
					}
			}
			
	}
