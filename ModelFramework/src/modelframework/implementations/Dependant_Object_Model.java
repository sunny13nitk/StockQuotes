package modelframework.implementations;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.logging.Level;

import modelframework.JAXB.definitions.objschemas.Dependant_Object_Defn;
import modelframework.exposed.FrameworkManager;

//Dependent Objects Model Class
//Contains Model Object Definitions and Utility Methods 

public class Dependant_Object_Model
	{
		private ArrayList<Dependant_Object_Defn> Dependant_Objects; // Only
		                                                            // Getter -
		
		public ArrayList<Dependant_Object_Defn> getDependant_Objects()
			{
				return Dependant_Objects;
			}
			
		// Set the Root Objects data from DB in case a connection from pool has
		// already been initiated
		public Dependant_Object_Model()
			{
				try
					{
						if (FrameworkManager.getConnectionPool().getConnection() != null)
							{
								this.Dependant_Objects = new ArrayList<Dependant_Object_Defn>();
								Statement Stat = null;
								String Query = "Select * from Dependant";
								try
									{
										
										Stat = FrameworkManager.getConnectionPool().getConnection().createStatement();
										ResultSet rs = Stat.executeQuery(Query);
										while (rs.next())
											{
												Dependant_Object_Defn Dependant_Object = new Dependant_Object_Defn(
												        rs.getString("DependantObject"), rs.getString("Root_Object"),
												        rs.getString("Parent_Dependant_Object"),
												        rs.getString("Relation_Name"), rs.getString("Table_Name"),
												        rs.getInt("Hierarchy"), rs.getString("Foreign_Key_Column"),
												        rs.getBoolean("Auto_Key"), rs.getString("Key_Obj_Field"),
												        rs.getString("Key_Table_Field"));
												
												this.Dependant_Objects.add(Dependant_Object);
												
											}
									}
								catch (Exception Ex)
									{
										FrameworkManager.getLogger().log(Level.SEVERE, Ex.getMessage(), Ex);
									}
									
							}
					}
				catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			
		// Get Dependant Object Metadata using Object Name
		public Dependant_Object_Defn Get_Dependant_Metadata(String Objname)
			{
				Dependant_Object_Defn Dep_Metadata = null;
				if (this.Dependant_Objects.size() > 0)
					{
						
						try
							{
								Dep_Metadata = this.getDependant_Objects().stream()
								        .filter(x -> x.getDepobjname().equals(Objname)).findFirst().get();
							}
						catch (NoSuchElementException NE)
							{
								// Do Nothing
							}
					}
					
				return Dep_Metadata;
			}
	}
