package scriptsengine.uploadengine.definitions;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import scriptsengine.pojos.OB_Scrip_General;

/**
 * POJO for Scrip Data Container - Scrip Data container holds complete data set information for the Scrip
 * Cache ENabled Type in the SCScripDataContainer Service Method getScripDataContainer
 * Cache will not be evicted once the session is loaded since no change to Scrip Master Data permitted in Session
 * Scrip Management would be done using a separate business role
 */
public class ScripDataContainer
{
	private OB_Scrip_General			scRoot;

	/**
	 * List of Relations w.r.t Scrip Root with SheetName and the ArrayList of dependant Relation Entities
	 */
	@SuppressWarnings("rawtypes")
	private ArrayList<SheetEntities>	relatedSheetEntities;

	/**
	 * @return the scRoot
	 */
	public OB_Scrip_General getScRoot()
	{
		return scRoot;
	}

	/**
	 * @param scRoot
	 *             the scRoot to set
	 */
	public void setScRoot(OB_Scrip_General scRoot)
	{
		this.scRoot = scRoot;
	}

	/**
	 * @return the relatedSheetEntities
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList<SheetEntities> getRelatedSheetEntities()
	{
		return relatedSheetEntities;
	}

	/**
	 * @param relatedSheetEntities
	 *             the relatedSheetEntities to set
	 */
	@SuppressWarnings("rawtypes")
	public void setRelatedSheetEntities(ArrayList<SheetEntities> relatedEntities)
	{
		this.relatedSheetEntities = relatedEntities;
	}

	/**
	 * @param scRoot
	 * @param relatedSheetEntities
	 */
	@SuppressWarnings("rawtypes")
	public ScripDataContainer(OB_Scrip_General scRoot, ArrayList<SheetEntities> relatedEntities)
	{
		super();
		this.scRoot = scRoot;
		this.relatedSheetEntities = relatedEntities;
	}

	/**
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public ScripDataContainer()
	{
		super();
		this.relatedSheetEntities = new ArrayList<SheetEntities>();
	}

	@SuppressWarnings(
	{ "rawtypes"
	})
	public SheetEntities getEntitiesforSheet(String sheetName)
	{
		SheetEntities shEnt = null;

		if (sheetName != null)
		{
			try
			{
				shEnt = this.relatedSheetEntities.stream().filter(x -> x.getSheetName().equals(sheetName)).findAny().get();
			}
			catch (NoSuchElementException e)
			{
				// Do Nothing
			}
		}

		return shEnt;
	}

}
