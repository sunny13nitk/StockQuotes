package modelframework.definitions;

import modelframework.exposed.FrameworkManager;

// Delete query Class - Holds the Delete Query String for a Object
// can be instantiated by passing the object class name
public class Query_Delete
	{
		private String Delete_Query_Text;
		
		public String getDeleteQuery_Text()
			{
				return Delete_Query_Text;
			}
			
		public void setDelete_Query_Text(String insert_Query_Text)
			{
				Delete_Query_Text = insert_Query_Text;
			}
			
		/********* Constructors ******************/
		
		// Blank constructor
		public Query_Delete()
			{
				
			}
			
		// Generate Query for Deleteion by passing Object Instance
		public Query_Delete(Object Instance) throws Exception
			{
				// First Get the Object Information
				Object_Info Obj_Info;
				
				// First try to get from Framework Manager
				Obj_Info = FrameworkManager.getObjectsInfoFactory()
				        .Get_ObjectInfo_byName(Instance.getClass().getSimpleName());
				
				if (Obj_Info == null)
					{
						// Create a new instance and add to Framework Manager
						// Otherwise
						Obj_Info = new Object_Info(Instance);
						FrameworkManager.getObjectsInfoFactory().Add_ObjectInfo_ToModel(Obj_Info);
					}
				if (Obj_Info != null)
					{
						Prepare_Delete_Query(Obj_Info);
					}
			}
			
		// Generate Query for Deleteion by Passing Object Info Instance
		public Query_Delete(Object_Info Obj_Info)
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
						Prepare_Delete_Query(Obj_Info);
					}
			}
			
		// Generate Query by passing Entity Metadata - To be Added
		
		// Other Methods
		
		private void Prepare_Delete_Query(Object_Info Obj_Info)
			{
				
				String Query_Start = "Delete FROM  ";
				String TableName = "";
				String QY_Where = " WHERE ";
				String space = " ";
				String Qy_Pkey_field = "";
				String QY_ParVal = " = ? ";
				
				if (Obj_Info.getRoot_Metadata() != null)
					{
						TableName = Obj_Info.getRoot_Metadata().getTablename().trim();
						Qy_Pkey_field = Obj_Info.getPKey_Name();
					}
				else if (Obj_Info.getDep_Metadata() != null)
					{
						TableName = Obj_Info.getDep_Metadata().getTablename().trim();
						Qy_Pkey_field = Obj_Info.getPKey_Name();
					}
					
				this.Delete_Query_Text = Query_Start + TableName + space + QY_Where + Qy_Pkey_field + QY_ParVal;
			}
	}
