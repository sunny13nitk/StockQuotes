package modelframework.implementations;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import modelframework.JAXB.definitions.models.Model;
import modelframework.JAXB.definitions.models.Model_Assembly;
import modelframework.definitions.Object_Info;
import modelframework.exposed.FrameworkManager;

/*
 * Model for Object Information TO be Used in Framework Manager as static field to store object Information
 * Properties List of Object Information
 */
public class Object_Info_Model
{
	private ArrayList<Object_Info> Objects_Data;

	public ArrayList<Object_Info> getObjects_Data()
	{
		return Objects_Data;
	}

	// *************************************Constructors
	// ********************************
	public Object_Info_Model()
	{
		this.Objects_Data = new ArrayList<Object_Info>();
	}

	// Create Object Infor Model Using Model Name
	// Upload Model from XML for relevant Objects
	public Object_Info_Model(boolean Model_Loaded) throws Exception
	{
		this.Objects_Data = new ArrayList<Object_Info>();
		if (Model_Loaded == true)
		{
			Model Sel_Model = FrameworkManager.getModelLoaded();
			if (FrameworkManager.getModelLoaded() != null)
			{
				// Looping Over Individual Assemblies
				for ( Model_Assembly assm : Sel_Model.getAssemblies() )
				{
					// Get Object Info for Each Assembly
					Object_Info new_obj_info = new Object_Info(assm.getAssembly());
					this.Add_ObjectInfo_ToModel(new_obj_info);

				}

			}
		}
	}

	// *************************************Other Methods
	// ********************************
	public boolean Add_ObjectInfo_ToModel(Object_Info obj_info)
	{

		boolean added = false;

		// Check for Duplicate before adding
		boolean already_exists = this.Objects_Data.stream().anyMatch(x -> x.getObject_Name().equals(obj_info.getObject_Name()));
		if (already_exists == true)
		{
			added = false;
		}
		else
		{
			this.Objects_Data.add(obj_info);
			added = true;
		}

		return added;
	}

	public Object_Info Get_ObjectInfo_byName(String Name)
	{
		Object_Info obj_info = null;
		if (Name != "" && Name != null)
		{
			try
			{
				// Find using Object Simple Name
				obj_info = this.Objects_Data.stream().filter(x -> x.getObject_Name().equals(Name)).findFirst().get();
			}
			catch (NoSuchElementException NE)
			{
				// Do Nothing
			}
		}

		return obj_info;
	}

	public <T> Object_Info Get_ObjectInfo_byClass(Class<T> cls)
	{
		Object_Info obj_info = null;
		if (cls != null)
		{
			try
			{
				// Find using Object Simple Name
				obj_info = this.Objects_Data.stream().filter(x -> x.getCurr_Obj_Class().equals(cls)).findFirst().get();
			}
			catch (NoSuchElementException NE)
			{
				// Do Nothing
			}
		}

		return obj_info;
	}

	public boolean IS_Object_Present(String Name)
	{
		boolean present = false;
		if (Name != "" && Name != null)
		{
			try
			{
				// Find using Object Simple Name
				present = this.Objects_Data.stream().anyMatch(x -> x.getObject_Name().equals(Name));
			}
			catch (NoSuchElementException NE)
			{
				// Do Nothing
			}
		}

		return present;
	}

}
