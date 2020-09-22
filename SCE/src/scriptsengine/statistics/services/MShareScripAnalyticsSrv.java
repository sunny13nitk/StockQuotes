package scriptsengine.statistics.services;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.reportsengine.repDS.definitions.TY_Attr_Container;
import scriptsengine.reportsengine.repDS.definitions.TY_ScRoot_AttrContainers;
import scriptsengine.statistics.JAXB.interfaces.IStatsSrvConfigMetadata;
import scriptsengine.statistics.definitions.TY_Attr_ScKeyFigsContainer;
import scriptsengine.statistics.definitions.TY_KeyFigure;
import scriptsengine.statistics.definitions.TY_Sector_AttrContainers;
import scriptsengine.statistics.definitions.TY_scCodeKeyFigure;
import scriptsengine.statistics.interfaces.IMShareScripAnalytics;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * ---------------------------------------------------------------------------------------------------------------------
 * ----
 * Prototype Scoped Market Share Scrips Analytics Specific Service
 * Provide Sector Specific Attribute Container List as import for Desired Sector
 * Get Sector specific Market Share Analytics Enabled Attributes Containers (1...N) s.that N = ScCode Key Figures List
 * for each of the Attributes
 * ---------------------------------------------------------------------------------------------------------------------
 * -
 */
@Service
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MShareScripAnalyticsSrv implements IMShareScripAnalytics
{

	// ---------------------------- AUTOWIRING - HOOK US UP -----------------------------------------------

	@Autowired
	private IStatsSrvConfigMetadata	dsMdtSrv;
	@Autowired
	private MessagesFormatter		msgFormatter;

	// --------------------------------- MY PROPERTIES -----------------------------------------------------
	private ArrayList<String>		mShareAttrs;
	private String					sectorName;
	private ArrayList<String>		scCodes;
	private int					MinYear;
	private int					MaxYear;

	/**
	 * @return the scCodes
	 */
	@Override
	public ArrayList<String> getScCodes()
	{
		return scCodes;
	}

	/**
	 * @param scCodes
	 *             the scCodes to set
	 */
	public void setScCodes(ArrayList<String> scCodes)
	{
		this.scCodes = scCodes;
	}

	/**
	 * @return the minYear
	 */
	@Override
	public int getMinYear()
	{
		return MinYear;
	}

	/**
	 * @param minYear
	 *             the minYear to set
	 */
	public void setMinYear(int minYear)
	{
		MinYear = minYear;
	}

	/**
	 * @return the maxYear
	 */
	@Override
	public int getMaxYear()
	{
		return MaxYear;
	}

	/**
	 * @param maxYear
	 *             the maxYear to set
	 */
	public void setMaxYear(int maxYear)
	{
		MaxYear = maxYear;
	}

	/**
	 * ----------------------------------------------------------------------------------------------------------------
	 * ---
	 * Get Sector specific Market Share Analytics Enabled Attributes Containers (1...N) s.that N = ScCode Key Figures
	 * List for each of the Attributes
	 * 
	 * @param scRootContainersList
	 *             - Sector Specific Attribute Container List to be provide as import Parameter
	 * @return - Attribute specific - Scrip Code Key Figures containers
	 * @throws EX_General
	 *              ---------------------------------------------------------------------------------------------------
	 * 
	 */
	@Override
	public TY_Sector_AttrContainers generateMarketShareAnalytics(ArrayList<TY_ScRoot_AttrContainers> scRootContainersList) throws EX_General
	{
		TY_Sector_AttrContainers sec_AttrContainers = null;
		/*
		 * Populate Market Share Attributes
		 */
		if (dsMdtSrv != null)
		{
			/*
			 * Validate the data First - Prepare Scrip Code List and Sector Name while doing so
			 */
			validateData(scRootContainersList);
			mShareAttrs = dsMdtSrv.getmShareRelevant_AttrNames();
			if (mShareAttrs.size() > 0)
			{
				/*
				 * Get Min'm and Max'm Years for Comparison
				 */
				TY_ScRoot_AttrContainers scRootAttrCont = scRootContainersList.get(0);
				populateTimeRange(scRootAttrCont);
				/*
				 * Process Sc Root Attribute Containers to get SectorAttributeContainres
				 */
				sec_AttrContainers = processScRootContainers(scRootContainersList);
			}
		}

		// TODO Auto-generated method stub
		return sec_AttrContainers;
	}

	/*
	 * ---------------------------------------My ROUTINES----------------------------------------------------------
	 */

	/**
	 * ---------Validate Data for a non Unique Sector --------------------------------------------
	 * 
	 * @param scRootContainersList
	 * @throws EX_General
	 *              ------------------------------------------------------------------------------------------
	 */
	private void validateData(ArrayList<TY_ScRoot_AttrContainers> scRootContainersList) throws EX_General
	{
		sectorName = scRootContainersList.get(0).getScRoot().getscSector();
		this.scCodes = new ArrayList<String>();
		for ( TY_ScRoot_AttrContainers ty_ScRoot_AttrContainers : scRootContainersList )
		{
			if (ty_ScRoot_AttrContainers.getScRoot().getscSector().equals(sectorName))
			{
				scCodes.add(ty_ScRoot_AttrContainers.getScRoot().getscCode());
			}
			else
			{
				EX_General msgErr = new EX_General("ERR_WRONG_SECTOR", new Object[]
				{ sectorName, ty_ScRoot_AttrContainers.getScRoot().getscSector()
				});
				msgFormatter.generate_message_snippet(msgErr);
				throw msgErr;
			}
		}
	}

	/**
	 * ----------------------------Populate Time Frame for SCrip Attribute Root Container-------
	 * 
	 * @param scRootAttrCont
	 *             - ScRootAttribute contianer Instance
	 *             ------------------------------------------------------------------------------------
	 */
	private void populateTimeRange(TY_ScRoot_AttrContainers scRootAttrCont)
	{
		String firstmShareAttrib = mShareAttrs.get(0);
		TY_Attr_Container attrCont = scRootAttrCont.getAttrContainers().stream().filter(x -> x.getAttrName().equals(firstmShareAttrib)).findFirst()
		          .get();
		if (attrCont != null)
		{
			MinYear = attrCont.getKeyFigures().stream().mapToInt(TY_KeyFigure::getKey).min().getAsInt();
			MaxYear = attrCont.getKeyFigures().stream().mapToInt(TY_KeyFigure::getKey).max().getAsInt();
		}
	}

	/**
	 * ---------------------------- Process SCRootAttribute Containers to get Sector ScripAttribute Containers
	 * 
	 * @param scRootContainersList
	 * @return
	 *         -------------------------------------------------------------------------------------------------
	 */
	private TY_Sector_AttrContainers processScRootContainers(ArrayList<TY_ScRoot_AttrContainers> scRootContainersList)
	{
		TY_Sector_AttrContainers secAttrContainers = new TY_Sector_AttrContainers();
		secAttrContainers.setSector(sectorName);

		// Fix the Attributes and loop - Get the data across years for each attribute one by one
		for ( String cMShareAttrib : mShareAttrs )
		{
			TY_Attr_ScKeyFigsContainer attr_ScKeyFigsContainer = new TY_Attr_ScKeyFigsContainer();
			attr_ScKeyFigsContainer.setAttrName(cMShareAttrib);

			// Fix the Year and loop
			for ( int cYear = MinYear; cYear <= MaxYear; cYear++ )
			{
				ArrayList<TY_scCodeKeyFigure> cYearscCodeKeyfigs = new ArrayList<TY_scCodeKeyFigure>();
				int currentYear = cYear;
				// Loop through each Scrip Attribute Container to get the data for Current Year(cYear) and Current
				// Attribute(cMShareAttrib)
				for ( TY_ScRoot_AttrContainers cscAttrContainer : scRootContainersList )
				{
					TY_scCodeKeyFigure scCodeKeyFig = new TY_scCodeKeyFigure();
					/**
					 * Get Attribute Container for Current Attribute
					 */
					TY_Attr_Container attrCont = cscAttrContainer.getAttrContainers().stream().filter(x -> x.getAttrName().equals(cMShareAttrib))
					          .findFirst().get();
					if (attrCont != null)
					{
						/**
						 * Get Figure(s) across Scrips for Current Attribute and Current Year
						 */
						TY_KeyFigure keyFig = null;
						try
						{
							keyFig = attrCont.getKeyFigures().stream().filter(y -> y.getKey() == currentYear).findFirst().get();
						}
						catch (NoSuchElementException Ex)
						{
							// Ignore No value found for Current Scrip for The Current YEar
							keyFig = new TY_KeyFigure(currentYear, 0);
						}
						if (keyFig != null)
						{
							scCodeKeyFig.setScCode(cscAttrContainer.getScRoot().getscCode());
							scCodeKeyFig.setKey(currentYear);
							scCodeKeyFig.setFigure(keyFig.getFigure());
							cYearscCodeKeyfigs.add(scCodeKeyFig);
						}

					}

				}
				// After all scrips for current Year are evaluated for Current Attribute convert the
				// cYearscCodeKeyfigs to percentages and append to final result for Current Year and Current
				// Attribute

				attr_ScKeyFigsContainer.getScCodeKeyFigures().addAll(convertToRelativePercentage(cYearscCodeKeyfigs));
			}
			secAttrContainers.getAttrSp_scKeyFigs().add(attr_ScKeyFigsContainer);

		}

		return secAttrContainers;
	}

	/**
	 * ----------convert the cYearscCodeKeyfigs to percentages specific Current Year scCodeKeyFigures-----
	 * 
	 * @param cYearscCodeKeyfigs
	 * @return
	 *         --------------------------------------------------------------------------------------
	 */
	private ArrayList<TY_scCodeKeyFigure> convertToRelativePercentage(ArrayList<TY_scCodeKeyFigure> cYearscCodeKeyfigs)
	{
		ArrayList<TY_scCodeKeyFigure> relPercentages = new ArrayList<TY_scCodeKeyFigure>();

		/**
		 * Get the Sum of figures
		 */
		double sumFigs = cYearscCodeKeyfigs.stream().mapToDouble(TY_scCodeKeyFigure::getFigure).sum();

		/**
		 * Loop at each scCode Key figure to compute relative percentage as per computed sum and return
		 */
		for ( TY_scCodeKeyFigure scCodeKeyFigure : cYearscCodeKeyfigs )
		{
			double per = (scCodeKeyFigure.getFigure() / sumFigs) * 100;
			TY_scCodeKeyFigure perScCodeKeyFig = new TY_scCodeKeyFigure(scCodeKeyFigure.getScCode(), scCodeKeyFigure.getKey(), per);
			relPercentages.add(perScCodeKeyFig);
		}

		return relPercentages;
	}

}
