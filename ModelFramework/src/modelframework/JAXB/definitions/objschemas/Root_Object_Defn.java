/**
 * 
 */
package modelframework.JAXB.definitions.objschemas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Root Object Class Definition -- Defines the Root Object Meta Data information
 * To be used as a static variable in SYSTEM Initialization stage For Model
 * Initialization Cannot be extended without extending the corresponding root
 * Objects table in DB
 */

@XmlRootElement(name = "RootObjectMetadata")
@XmlAccessorType(XmlAccessType.FIELD)
public final class Root_Object_Defn
	{
		private String	Objectname;
		private String	Tablename;
		private Boolean	Autokey;
		private String	KeyObjField;
		private String	KeyTableField;
		
		public String getObjectname()
			{
				return Objectname;
			}
			
		public void setObjectname(String objectname)
			{
				Objectname = objectname;
			}
			
		public String getTablename()
			{
				return Tablename;
			}
			
		public void setTablename(String tablename)
			{
				Tablename = tablename;
			}
			
		public Boolean getAutokey()
			{
				return Autokey;
			}
			
		public void setAutokey(Boolean autokey)
			{
				Autokey = autokey;
			}
			
		public String getKeyObjField()
			{
				return KeyObjField;
			}
			
		public void setKeyObjField(String keyObjField)
			{
				KeyObjField = keyObjField;
			}
			
		public String getKeyTableField()
			{
				return KeyTableField;
			}
			
		public void setKeyTableField(String keyTableField)
			{
				KeyTableField = keyTableField;
			}
			
		public Root_Object_Defn(String objectname, String tablename, Boolean autokey, String keyObjField,
		        String keyTableField)
			{
				
				Objectname = objectname;
				Tablename = tablename;
				Autokey = autokey;
				KeyObjField = keyObjField;
				KeyTableField = keyTableField;
			}
			
		public Root_Object_Defn()
			{
				
			}
			
	}
