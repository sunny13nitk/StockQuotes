package modelframework.definitions;

import java.util.ArrayList;

import modelframework.exposed.FrameworkManager;
import modelframework.types.TY_NameValue;

// Update query Class - Holds the Update Query String for a Object
// and Changed Attribute Name Value Pair List
// can be instantiated by passing the object class name
public class Query_Update
{
	// Query Text
	private String					Update_Query_Text;

	// Name Value Pair for Changed Properties
	private ArrayList<TY_NameValue>	Changed_Properties;

	// ********* Getters & Setters *************
	public ArrayList<TY_NameValue> getChanged_Properties()
	{
		return Changed_Properties;
	}

	public void setChanged_Properties(ArrayList<TY_NameValue> changed_Properties)
	{
		Changed_Properties = changed_Properties;
	}

	public String getUpdate_Query_Text()
	{
		return Update_Query_Text;
	}

	// ************* Constructors *********************
	// Blank Constructor
	public Query_Update()
	{

	}

	// Create Update Query by Passing Object Instance and Changed Properties
	// Name Value Pair Collection
	// Assumption - Name Value Pair Already validated via SetProperty method
	// of Root/Dependant Object
	public Query_Update(Object instance, ArrayList<TY_NameValue> CH_Props) throws Exception
	{
		if (instance != null && CH_Props != null)
		{
			if (CH_Props.size() > 0)
			{
				// First Get the Object Information
				Object_Info Obj_Info;
				// First try to get from Framework Manager
				Obj_Info = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(instance.getClass().getSimpleName());

				if (Obj_Info == null)
				{
					// Create a new instance and add to
					// Framework Manager
					// Otherwise
					Obj_Info = new Object_Info(instance);
					FrameworkManager.getObjectsInfoFactory().Add_ObjectInfo_ToModel(Obj_Info);
				}

				if (Obj_Info != null)
				{
					Prepare_Update_Query(Obj_Info, CH_Props);
				}

			}
		}
	}

	// Create Update Query by Passing Object Info Instance and Changed
	// Properties
	// Name Value Pair Collection
	// Assumption - Name Value Pair Already validated via SetProperty method
	// of Root/Dependant Object
	public Query_Update(Object_Info Obj_Info, ArrayList<TY_NameValue> CH_Props)
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
			Prepare_Update_Query(Obj_Info, CH_Props);
		}
	}

	// Create Update Query by Passing Entity Metadata - To be Added

	// Other Methods

	private void Prepare_Update_Query(Object_Info Obj_Info, ArrayList<TY_NameValue> CH_Props)
	{
		String Qy_Update = "UPDATE ";
		String Qy_Tabname = "";
		String QY_Set = "SET ";
		String QY_ParVal = " = ? ";
		String QY_Where = " WHERE ";
		String space = " ";
		String QY_Dynamic_Set = "";
		String Qy_Pkey_field = "";
		String comma = ",";

		// For Each of the Values in Name Value Pairs that need not be
		// set
		for ( TY_NameValue NameValue : CH_Props )
		{
			QY_Dynamic_Set += NameValue.Name + QY_ParVal + comma;
		}

		// Primary Key in Where Condition and Table Name

		if (Obj_Info.getRoot_Metadata() != null)
		{
			Qy_Tabname = Obj_Info.getRoot_Metadata().getTablename().trim();
			Qy_Pkey_field = Obj_Info.getPKey_Name();
		}
		else if (Obj_Info.getDep_Metadata() != null)
		{
			Qy_Tabname = Obj_Info.getDep_Metadata().getTablename().trim();
			Qy_Pkey_field = Obj_Info.getPKey_Name();

		}

		// Remove Last Coma from list of variables to set
		String fQuery_Var = QY_Dynamic_Set.substring(0, (QY_Dynamic_Set.length() - 1));

		// Finally preparing Query String
		this.Update_Query_Text = Qy_Update + Qy_Tabname + space + QY_Set + fQuery_Var + QY_Where + Qy_Pkey_field + QY_ParVal;

	}
}
