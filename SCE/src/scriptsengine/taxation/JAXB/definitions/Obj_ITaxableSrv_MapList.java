package scriptsengine.taxation.JAXB.definitions;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Map List of Reporting Service and XLS Service
 */
@XmlRootElement(name = "Obj_ITaxableSrv_MapList")
@XmlAccessorType(XmlAccessType.FIELD)
public class Obj_ITaxableSrv_MapList
{
	private ArrayList<Obj_ITaxableSrv_Mapping> objTaxSrvMappings;

	public ArrayList<Obj_ITaxableSrv_Mapping> getObjTaxSrvMappings()
	{
		return objTaxSrvMappings;
	}

	public void setObjTaxSrvMappings(ArrayList<Obj_ITaxableSrv_Mapping> objTaxSrvMappings)
	{
		this.objTaxSrvMappings = objTaxSrvMappings;
	}

	public Obj_ITaxableSrv_MapList()
	{
		super();
		this.objTaxSrvMappings = new ArrayList<Obj_ITaxableSrv_Mapping>();
	}

	public Obj_ITaxableSrv_MapList(ArrayList<Obj_ITaxableSrv_Mapping> objTaxSrvMappings)
	{
		super();
		this.objTaxSrvMappings = objTaxSrvMappings;
	}

}
