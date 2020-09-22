package scriptsengine.uploadengine.JAXB.definitions;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "WorkbookMetadata")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkbookMetadata
{
	private ArrayList<SheetMetadata> sheetsMetadata;

	/**
	 * @return the sheetsMetadata
	 */
	public ArrayList<SheetMetadata> getSheetsMetadata()
	{
		return sheetsMetadata;
	}

	/**
	 * @param sheetsMetadata
	 *             the sheetsMetadata to set
	 */
	public void setSheetsMetadata(ArrayList<SheetMetadata> sheetsMetadata)
	{
		this.sheetsMetadata = sheetsMetadata;
	}

	/**
	 * @param sheetsMetadata
	 */
	public WorkbookMetadata(ArrayList<SheetMetadata> sheetsMetadata)
	{
		super();
		this.sheetsMetadata = sheetsMetadata;
	}

	/**
	 * 
	 */
	public WorkbookMetadata()
	{
		this.sheetsMetadata = new ArrayList<SheetMetadata>();
	}

}
