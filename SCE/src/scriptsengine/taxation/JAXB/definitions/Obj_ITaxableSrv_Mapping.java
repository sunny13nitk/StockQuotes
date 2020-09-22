package scriptsengine.taxation.JAXB.definitions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Mapping b/w Base Object and Taxable Interface Generation Service
 */
@XmlRootElement(name = "Obj_ITaxableSrv_Mapping")
@XmlAccessorType(XmlAccessType.FIELD)
public class Obj_ITaxableSrv_Mapping
{
	private String	objName;
	private String	ITaxableSrvName;

	public String getObjName()
	{
		return objName;
	}

	public Obj_ITaxableSrv_Mapping(String objName, String iTaxableSrvName)
	{
		super();
		this.objName = objName;
		ITaxableSrvName = iTaxableSrvName;
	}

	public Obj_ITaxableSrv_Mapping()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public void setObjName(String objName)
	{
		this.objName = objName;
	}

	public String getITaxableSrvName()
	{
		return ITaxableSrvName;
	}

	public void setITaxableSrvName(String iTaxableSrvName)
	{
		ITaxableSrvName = iTaxableSrvName;
	}

}
