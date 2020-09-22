package scriptsengine.uploadengineSC.scContainer.definitions;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengineSC.entities.EN_SC_General;

/**
 * POJO for Scrip Data Container - Scrip Data container holds complete data set information for the Scrip
 * 
 */
public class SCScripDataContainer
{
	private EN_SC_General			scRoot;

	/**
	 * List of Relations w.r.t Scrip Root with SheetName and the ArrayList of dependant Relation Entities
	 */
	@SuppressWarnings("rawtypes")
	private ArrayList<SheetEntities>	relatedSheetEntities;

	public EN_SC_General getScRoot()
	{
		return scRoot;
	}

	public void setScRoot(EN_SC_General scRoot)
	{
		this.scRoot = scRoot;
	}

	public ArrayList<SheetEntities> getRelatedSheetEntities()
	{
		return relatedSheetEntities;
	}

	public void setRelatedSheetEntities(ArrayList<SheetEntities> relatedSheetEntities)
	{
		this.relatedSheetEntities = relatedSheetEntities;
	}

	public SCScripDataContainer()
	{
		super();
		this.relatedSheetEntities = new ArrayList<SheetEntities>();
	}

	public SCScripDataContainer(EN_SC_General scRoot, ArrayList<SheetEntities> relatedSheetEntities)
	{
		super();
		this.scRoot = scRoot;
		this.relatedSheetEntities = relatedSheetEntities;
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
