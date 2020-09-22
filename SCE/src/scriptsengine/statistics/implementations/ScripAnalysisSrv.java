package scriptsengine.statistics.implementations;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.reportsengine.definitions.Ty_SectorAvgStats;
import scriptsengine.reportsengine.definitions.Ty_SectorCount;
import scriptsengine.reportsengine.interfaces.IXLSReportAware;
import scriptsengine.reportsengine.repDS.definitions.TY_Attr_Container;
import scriptsengine.reportsengine.repDS.definitions.TY_ScRoot_AttrContainers;
import scriptsengine.reportsengine.repDS.interfaces.IReportDataSource;
import scriptsengine.statistics.JAXB.definitions.StatsAttrDetails;
import scriptsengine.statistics.JAXB.interfaces.IStatsSrvConfigMetadata;
import scriptsengine.statistics.interfaces.IScripAnalysisSrv;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.valuehelpers.definitions.TY_StringKeyDesc;
import scriptsengine.valuehelpers.interfaces.IValueHelpScrips;

/**
 * 
 * Prototype Scope Scrip analysis Service Bean
 */
@Service("ScripAnalysisSrv")
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripAnalysisSrv implements IScripAnalysisSrv, ApplicationContextAware, IXLSReportAware
{
	/**
	 * ------------------------------------AUTOWIRED DEPENDENCIES ---------------------------------------------
	 */
	@Autowired
	private MessagesFormatter				msgFormatter;			// Message Formatter

	@Autowired
	private IValueHelpScrips					vhScSrv;				// Scrip Value Help Service

	@Autowired
	private IStatsSrvConfigMetadata			attrMdtSrv;

	/**
	 * ------------------------------------AUTOWIRED DEPENDENCIES ---------------------------------------------
	 */

	/**
	 * -------------------PRIVATE MEMBERS -----------------------------------------------
	 */

	private ApplicationContext				appCtxt;

	private IReportDataSource				dsSCA;				// Data Source Service

	private ArrayList<TY_ScRoot_AttrContainers>	scRootAttContainersList;

	private ArrayList<TY_StringKeyDesc>		scCodes;

	private ArrayList<String>				scSectors;

	private ArrayList<Ty_SectorCount>			sectorsCount;

	private ArrayList<Ty_SectorAvgStats>		sectorsAvgStats;

	/**
	 * -------------------PRIVATE MEMBERS -----------------------------------------------
	 */

	/**
	 * Getters and Setters -----------------------------------------------------
	 */

	/**
	 * 
	 * @return the scRootAttContainersList
	 */
	@Override
	public ArrayList<TY_ScRoot_AttrContainers> getScRootAttContainersList()
	{
		return scRootAttContainersList;
	}

	/**
	 * @return the sectorsCount
	 */
	@Override
	public ArrayList<Ty_SectorCount> getSectorsCount()
	{
		return sectorsCount;
	}

	/**
	 * @param sectorsCount
	 *             the sectorsCount to set
	 */
	public void setSectorsCount(ArrayList<Ty_SectorCount> sectorsCount)
	{
		this.sectorsCount = sectorsCount;
	}

	/**
	 * @return the sectorsAvgStats
	 */
	@Override
	public ArrayList<Ty_SectorAvgStats> getSectorsAvgStats()
	{
		return sectorsAvgStats;
	}

	/**
	 * @param sectorsAvgStats
	 *             the sectorsAvgStats to set
	 */
	public void setSectorsAvgStats(ArrayList<Ty_SectorAvgStats> sectorsAvgStats)
	{
		this.sectorsAvgStats = sectorsAvgStats;
	}

	/**
	 * @param scRootAttContainersList
	 *             the scRootAttContainersList to set
	 */
	public void setScRootAttContainersList(ArrayList<TY_ScRoot_AttrContainers> scRootAttContainersList)
	{
		this.scRootAttContainersList = scRootAttContainersList;
	}

	/**
	 * @return the scCodes
	 */
	@Override
	public ArrayList<TY_StringKeyDesc> getScCodes()
	{
		return scCodes;
	}

	/**
	 * @param scCodes
	 *             the scCodes to set
	 */
	public void setScCodes(ArrayList<TY_StringKeyDesc> scCodes)
	{
		this.scCodes = scCodes;
	}

	/**
	 * @return the scSectors
	 */
	@Override
	public ArrayList<String> getScSectors()
	{
		return scSectors;
	}

	/**
	 * @param scSectors
	 *             the scSectors to set
	 */
	public void setScSectors(ArrayList<String> scSectors)
	{
		this.scSectors = scSectors;
	}

	/**
	 * Getters and Setters -----------------------------------------------------
	 */

	/**
	 * ---------------------------------------- INTERFACE METHODS ----------------------------------------------
	 */

	/**
	 * ----------------------------------------------------------------------------------
	 * Populate Data for All Scrips
	 * ---------------------------------------------------------------------------------
	 */
	@Override
	public void populateDataforAllScrips() throws EX_General
	{
		if (vhScSrv != null)
		{
			// Get Scrip Codes Maintained
			this.scCodes = vhScSrv.getScripCodesDesc();
			// Get unique Sectors for Scrips Maintianed
			this.scSectors = vhScSrv.getUniqueSectorsforScripsMaintained();
			/**
			 * Iterate through maintained Scrips to populate Scrip Root Container List
			 */
			if (scCodes.size() > 0)
			{
				this.scRootAttContainersList = new ArrayList<TY_ScRoot_AttrContainers>();
				if (dsSCA != null)
				{
					for ( TY_StringKeyDesc scCodeDesc : scCodes )
					{
						this.scRootAttContainersList.add(dsSCA.generateReportDataSourceforScripCode(scCodeDesc.getKey()));
					}

					populate_SectorStats();

				}
			}
		}
	}

	/**
	 * -----------------------Populate Data for Requested Sector ------------------------------------
	 */
	@Override
	public void populateDataforSector(String Sector) throws EX_General
	{
		if (Sector != null && vhScSrv != null)
		{

			this.scCodes = vhScSrv.getScripsforSector(Sector);
			if (this.scCodes != null)
			{
				if (this.scCodes.size() > 0)
				{
					this.scSectors = new ArrayList<String>();
					this.scSectors.add(Sector);
					this.scRootAttContainersList = new ArrayList<TY_ScRoot_AttrContainers>();
					for ( TY_StringKeyDesc scCodeDesc : scCodes )
					{
						this.scRootAttContainersList.add(dsSCA.generateReportDataSourceforScripCode(scCodeDesc.getKey()));

					}
					populate_SectorStats();
				}
			}
		}

	}

	/**
	 * Populate Data for REquested Sectors -----------------------------------------------------
	 */
	@Override
	public void populateDataforSectors(ArrayList<String> Sectors) throws EX_General
	{
		if (Sectors != null && vhScSrv != null)
		{
			if (Sectors.size() > 0)
			{
				this.scCodes = new ArrayList<TY_StringKeyDesc>();
				this.scSectors = new ArrayList<String>();
				this.scRootAttContainersList = new ArrayList<TY_ScRoot_AttrContainers>();
				for ( String sec : Sectors )
				{
					this.scCodes.addAll(vhScSrv.getScripsforSector(sec));
					this.scSectors.add(sec);
				}
				for ( TY_StringKeyDesc scCodeDesc : scCodes )
				{
					this.scRootAttContainersList.add(dsSCA.generateReportDataSourceforScripCode(scCodeDesc.getKey()));

				}
				populate_SectorStats();
			}
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		if (ctxt != null)
		{
			this.appCtxt = ctxt;
			this.dsSCA = (IReportDataSource) appCtxt.getBean("DS_ScripsAnalysis");
		}

	}

	/**
	 * Generate XLS Report - No Implementation needed will be handled via an aspect
	 * Variable filepath wil also be passed with pjp
	 */
	@Override
	public void generateXLSReport(String filepath) throws EX_General
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Generate XLS Report - No Implementation needed will be handled via an aspect
	 * Variable wbctxt wil also be passed with pjp
	 */

	@Override
	public void generateXLSReport(XSSFWorkbook wbCtxt) throws EX_General
	{
		// TODO Auto-generated method stub

	}

	/**
	 * ---------------------------------------- INTERFACE METHODS ----------------------------------------------
	 */

	/**
	 * ---------------------------------------- PRIVATE METHODS ----------------------------------------------
	 */

	private void populate_SectorStats() throws EX_General
	{
		ArrayList<TY_ScRoot_AttrContainers> secSp_scRootAttrContainers = null;
		if (this.scRootAttContainersList != null && this.scSectors != null)
		{
			this.sectorsAvgStats = new ArrayList<Ty_SectorAvgStats>();
			this.sectorsCount = new ArrayList<Ty_SectorCount>();
			for ( String sec : scSectors )
			{
				secSp_scRootAttrContainers = this.scRootAttContainersList.stream().filter(x -> x.getScRoot().getscSector().equals(sec))
				          .collect(Collectors.toCollection(ArrayList::new));
				if (secSp_scRootAttrContainers != null)
				{
					if (secSp_scRootAttrContainers.size() > 0)
					{
						/**
						 * Populate Counts for Sector
						 */
						Ty_SectorCount secCountEnt = new Ty_SectorCount(sec, secSp_scRootAttrContainers.size());
						this.sectorsCount.add(secCountEnt);

						/**
						 * Populate Sector Averages
						 */
						this.sectorsAvgStats.addAll(this.getSectorAverageStatsforSecEntities(secSp_scRootAttrContainers));
					}

				}

			}
		}
	}

	private ArrayList<Ty_SectorAvgStats> getSectorAverageStatsforSecEntities(ArrayList<TY_ScRoot_AttrContainers> secSp_scRootAttrContainers)
	          throws EX_General
	{

		ArrayList<Ty_SectorAvgStats> secAvgStatsList = new ArrayList<Ty_SectorAvgStats>();
		ArrayList<TY_Attr_Container> secSpAttrContainers = new ArrayList<TY_Attr_Container>();
		ArrayList<TY_Attr_Container> attrSpAttrContainers = new ArrayList<TY_Attr_Container>();

		/**
		 * Prepare a pool of Attribute Containers from All Scrip Root Attribute Containers which could be leveraged to
		 * perform group operations on specific attribute
		 */
		for ( TY_ScRoot_AttrContainers scRootAttrContainers : secSp_scRootAttrContainers )
		{
			secSpAttrContainers.addAll(scRootAttrContainers.getAttrContainers());
		}

		/**
		 * Get all attributes of the DS - They will be in Attribute Containers and Single Value Containers
		 * Pick first Row from One of Root Attrib Conatiners and loop through the Attribute Containers to get the
		 * attributes
		 * For each of the Attribute determine the sec Specific avg and delta avg flags and calculate averages
		 * accordingly
		 */

		TY_ScRoot_AttrContainers first_scRootAttrContainers = secSp_scRootAttrContainers.get(0);
		if (first_scRootAttrContainers != null && attrMdtSrv != null)
		{
			String sectorName = first_scRootAttrContainers.getScRoot().getscSector();

			// Attribute Containers Scan to get Attributes
			for ( TY_Attr_Container attrContainer : first_scRootAttrContainers.getAttrContainers() )
			{
				Ty_SectorAvgStats secAvgStatEntity = new Ty_SectorAvgStats();
				secAvgStatEntity.setSector(sectorName);
				secAvgStatEntity.setAttrLabel(attrContainer.getAttrLabel());
				secAvgStatEntity.setAttrName(attrContainer.getAttrName());

				StatsAttrDetails attrDetails = attrMdtSrv.getattrDetailsbyAttrName(attrContainer.getAttrName());
				if (attrDetails != null)
				{

					// Average of Key figures to be Calculated if SelfsecSpON
					if (attrDetails.isSelfsecSpON() || attrDetails.isAvgDeltasecSpON())
					{
						attrSpAttrContainers = secSpAttrContainers.stream().filter(x -> x.getAttrName().equals(attrDetails.getAttrName()))
						          .collect(Collectors.toCollection(ArrayList::new));
						if (attrSpAttrContainers != null)
						{
							if (attrDetails.isSelfsecSpON())
							{
								secAvgStatEntity.setAvgAvg(
								          attrSpAttrContainers.stream().mapToDouble(TY_Attr_Container::getAvg).average().getAsDouble());
							}

							if (attrDetails.isAvgDeltasecSpON())
							{
								secAvgStatEntity.setAvgDeltaAvg(
								          attrSpAttrContainers.stream().mapToDouble(TY_Attr_Container::getDeltaAvg).average().getAsDouble());
							}

						}
					}
				}
				secAvgStatsList.add(secAvgStatEntity);

			}
		}

		return secAvgStatsList;
	}

	/**
	 * ---------------------------------------- PRIVATE METHODS ----------------------------------------------
	 */
}
