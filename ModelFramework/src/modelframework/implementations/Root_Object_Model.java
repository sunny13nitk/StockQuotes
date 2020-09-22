package modelframework.implementations;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.logging.Level;

import modelframework.JAXB.definitions.objschemas.Root_Object_Defn;
import modelframework.exposed.FrameworkManager;

//Root Objects Model Class
//Contains Model Object Definitions and Utility Methods 
public class Root_Object_Model
	{
		private ArrayList<Root_Object_Defn> Root_Objects; // Only Getter - No
		                                                  // Setter
		
		public ArrayList<Root_Object_Defn> getRoot_Objects()
			{
				return Root_Objects;
			}
			
		// Set the Root Objects data from DB in case a connection from pool has
		// already been initiated
		public Root_Object_Model()
			{
				try
					{
						if (FrameworkManager.getConnectionPool().getConnection() != null)
							{
								this.Root_Objects = new ArrayList<Root_Object_Defn>();
								java.sql.Statement Stat = null;
								String Query = "Select * from Root";
								try
									{
										
										Stat = FrameworkManager.getConnectionPool().getConnection().createStatement();
										ResultSet rs = Stat.executeQuery(Query);
										while (rs.next())
											{
												Root_Object_Defn Root_Object = new Root_Object_Defn(
												        rs.getString("Object_Type"), rs.getString("Table_Name"),
												        rs.getBoolean("Auto_Key"), rs.getString("Key_Obj_Field"),
												        rs.getString("Key_Table_Field"));
												this.Root_Objects.add(Root_Object);
												
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
			
		// Get Root Object MetaData using Object Name
		public Root_Object_Defn Get_Root_Metadata(String Objname)
			{
				Root_Object_Defn Root_Metadata = null;
				if (this.Root_Objects.size() > 0)
					{
						
						try
							{
								Root_Metadata = this.getRoot_Objects().stream()
								        .filter(x -> x.getObjectname().equals(Objname)).findFirst().get();
							}
						catch (NoSuchElementException NE)
							{
								// Do Nothing
							}
					}
					
				return Root_Metadata;
			}
			
	}
