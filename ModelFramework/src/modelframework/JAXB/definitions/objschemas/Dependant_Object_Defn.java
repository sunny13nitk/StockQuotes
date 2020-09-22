package modelframework.JAXB.definitions.objschemas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Dependent Object Class Definition -- Defines the Dependant Object Meta Data
 * information To be used as a static variable in SYSTEM Initialization stage
 * For Model Initialization Cannot be extended without extending the
 * corresponding root Objects table in DB
 */

// Model Assembly Class : Can have 1:N cardinality with a Model
@XmlRootElement(name = "Relations")
@XmlAccessorType(XmlAccessType.FIELD)
public final class Dependant_Object_Defn
{
	private String		Depobjname;
	private String		Rootobjname;
	private String		Parentdepobj;
	private String		Relationname;
	private String		Tablename;
	private Integer	Hierarchy;
	private String		Foreignkeyname;
	private Boolean	Autokey;
	private String		KeyObjField;
	private String		KeyTableField;

	public String getDepobjname()
	{
		return Depobjname;
	}

	public void setDepobjname(String depobjname)
	{
		Depobjname = depobjname;
	}

	public String getRootobjname()
	{
		return Rootobjname;
	}

	public void setRootobjname(String rootobjname)
	{
		Rootobjname = rootobjname;
	}

	public String getParentdepobj()
	{
		return Parentdepobj;
	}

	public void setParentdepobj(String parentdepobj)
	{
		Parentdepobj = parentdepobj;
	}

	public String getRelationname()
	{
		return Relationname;
	}

	public void setRelationname(String relationname)
	{
		Relationname = relationname;
	}

	public String getTablename()
	{
		return Tablename;
	}

	public void setTablename(String tablename)
	{
		Tablename = tablename;
	}

	public Integer getHierarchy()
	{
		return Hierarchy;
	}

	public void setHierarchy(Integer hierarchy)
	{
		Hierarchy = hierarchy;
	}

	public String getForeignkeyname()
	{
		return Foreignkeyname;
	}

	public void setForeignkeyname(String foreignkeyname)
	{
		Foreignkeyname = foreignkeyname;
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

	public Dependant_Object_Defn(String depobjname, String rootobjname, String parentdepobj, String relationname, String tablename,
	          Integer hierarchy, String foreignkeyname, Boolean autokey, String keyObjField, String keyTableField)
	{

		Depobjname = depobjname;
		Rootobjname = rootobjname;
		Parentdepobj = parentdepobj;
		Relationname = relationname;
		Tablename = tablename;
		Hierarchy = hierarchy;
		Foreignkeyname = foreignkeyname;
		Autokey = autokey;
		KeyObjField = keyObjField;
		KeyTableField = keyTableField;
	}

	public Dependant_Object_Defn()
	{

	}

}