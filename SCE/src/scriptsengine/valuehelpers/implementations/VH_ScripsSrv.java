package scriptsengine.valuehelpers.implementations;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import modelframework.interfaces.IQueryService;
import modelframework.managers.QueryManager;
import modelframework.types.TY_NameValue;
import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.sectors.services.implementations.SectorsService;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.implementations.ScripSheetMetadataService;
import scriptsengine.utilities.implementations.LambdaUtilities;
import scriptsengine.valuehelpers.definitions.TY_StringKeyDesc;
import scriptsengine.valuehelpers.interfaces.IValueHelpAware;
import scriptsengine.valuehelpers.interfaces.IValueHelpScrips;

/**
 * 
 * Value Help Service for Scrips - Application Context Scope
 */
@Service
public class VH_ScripsSrv implements IValueHelpAware, IValueHelpScrips
{

	@Autowired
	private ScripSheetMetadataService		shMdtSrv;
	@Autowired
	private MessagesFormatter			msgFormatter;
	@Autowired
	private SectorsService				secSrv;

	private ArrayList<TY_StringKeyDesc>	scCodesDesc;
	private ArrayList<TY_StringKeyDesc>	scCodesSectors;
	private ArrayList<TY_StringKeyDesc>	scCodesUrl;
	private ArrayList<String>			sectorsUniq;
	private ArrayList<OB_Scrip_General>	scRootList;

	/**
	 * @return the shMdtSrv
	 */
	public ScripSheetMetadataService getShMdtSrv()
	{
		return shMdtSrv;
	}

	/**
	 * @return the scRootList
	 */
	public ArrayList<OB_Scrip_General> getScRootList()
	{
		return scRootList;
	}

	/**
	 * Gets Scrip Codes and Descriptions for Maintianed Scrips----------------------------------------
	 * 
	 * @return - Scrip codes and Descriptions
	 * @throws EX_General
	 *              -----------------------------------------------------------------------------------
	 */
	@Override
	public ArrayList<TY_StringKeyDesc> getScripCodesDesc() throws EX_General
	{
		if (this.scCodesDesc == null)
		{
			this.scCodesDesc = new ArrayList<TY_StringKeyDesc>();
			try
			{
				populateRootEntities();
				if (this.scRootList != null)
				{
					if (this.scRootList.size() > 0)
					{
						for ( OB_Scrip_General scRoot : this.scRootList )
						{
							TY_StringKeyDesc scCodeDesc = new TY_StringKeyDesc(scRoot.getscCode(), scRoot.getscName());
							this.scCodesDesc.add(scCodeDesc);

						}
					}
				}
			}
			catch (Exception e)
			{
				EX_General egen = new EX_General("ERR_VHHELP", new Object[]
				{ this.getClass().getSimpleName(), e.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}
		}

		return this.scCodesDesc;
	}

	/**
	 * Gets Scrip Codes and Sectors for Maintained Scrips----------------------------------------
	 * 
	 * @return - Scrip codes and Sectors
	 * @throws EX_General
	 *              -----------------------------------------------------------------------------------
	 */
	@Override
	public ArrayList<TY_StringKeyDesc> getScripCodesSectors() throws EX_General
	{
		if (this.scCodesSectors == null)
		{
			this.scCodesSectors = new ArrayList<TY_StringKeyDesc>();
			try
			{
				populateRootEntities();
				if (this.scRootList != null)
				{
					if (this.scRootList.size() > 0)
					{
						for ( OB_Scrip_General scRoot : this.scRootList )
						{
							TY_StringKeyDesc scCodeSec = new TY_StringKeyDesc(scRoot.getscCode(), scRoot.getscSector());
							this.scCodesSectors.add(scCodeSec);

						}
					}
				}
			}
			catch (Exception e)
			{
				EX_General egen = new EX_General("ERR_VHHELP", new Object[]
				{ this.getClass().getSimpleName(), e.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}
		}

		return this.scCodesSectors;
	}

	/**
	 * Gets Scrip Codes and Urls for Maintained Scrips----------------------------------------
	 * 
	 * @return - Scrip codes and Urls
	 * @throws EX_General
	 *              -----------------------------------------------------------------------------------
	 */
	@Override
	public ArrayList<TY_StringKeyDesc> getScripCodesUrl() throws EX_General
	{
		if (this.scCodesUrl == null)
		{
			this.scCodesUrl = new ArrayList<TY_StringKeyDesc>();
			try
			{
				populateRootEntities();
				if (this.scRootList != null)
				{
					if (this.scRootList.size() > 0)
					{
						for ( OB_Scrip_General scRoot : this.scRootList )
						{
							TY_StringKeyDesc scCodeUrl = new TY_StringKeyDesc(scRoot.getscCode(), scRoot.geturl());
							this.scCodesUrl.add(scCodeUrl);

						}
					}
				}
			}
			catch (Exception e)
			{
				EX_General egen = new EX_General("ERR_VHHELP", new Object[]
				{ this.getClass().getSimpleName(), e.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}
		}

		return this.scCodesUrl;
	}

	/**
	 * Gets Unique Sectors for Maintained Scrips----------------------------------------
	 * 
	 * @return - Unique Sectors
	 * @throws EX_General
	 *              -----------------------------------------------------------------------------------
	 */
	@Override
	public ArrayList<String> getUniqueSectorsforScripsMaintained() throws EX_General
	{

		if (this.sectorsUniq == null)
		{
			this.sectorsUniq = new ArrayList<String>();
			try
			{
				populateRootEntities();
				if (this.scRootList != null)
				{
					if (this.scRootList.size() > 0)
					{
						ArrayList<OB_Scrip_General> uniqSec = new ArrayList<OB_Scrip_General>();
						uniqSec = this.scRootList.stream().filter(LambdaUtilities.distinctByKey(x -> x.getscSector()))
						          .collect(Collectors.toCollection(ArrayList::new));
						for ( OB_Scrip_General scRoot : uniqSec )
						{
							this.sectorsUniq.add(scRoot.getscSector());

						}

					}
				}
			}
			catch (Exception e)
			{
				EX_General egen = new EX_General("ERR_VHHELP", new Object[]
				{ this.getClass().getSimpleName(), e.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}
		}

		return this.sectorsUniq;
	}

	/**
	 * Gets Scrip Codes for a Specific Sector for Maintained Scrips----------------------------------------
	 * 
	 * @return - Scrip codes and descriptions for a Specific Sector
	 * @throws EX_General
	 *              -----------------------------------------------------------------------------------
	 */
	@Override
	public ArrayList<TY_StringKeyDesc> getScripsforSector(String Sector) throws EX_General
	{
		ArrayList<TY_StringKeyDesc> retScrips = null;
		if (secSrv != null && Sector != null)
		{
			try
			{
				if (secSrv.sectorExists(Sector))
				{
					IQueryService qs = (IQueryService) QueryManager.getQuerybyRootObjname(shMdtSrv.getRootObjectName());
					if (qs != null)
					{
						ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();

						params.add(new TY_NameValue("scSector", Sector));
						ArrayList<OB_Scrip_General> secScrips = qs.executeQuery("scSector = ?", params);
						if (secScrips != null)
						{
							if (secScrips.size() > 0)
							{
								retScrips = new ArrayList<TY_StringKeyDesc>();
								for ( OB_Scrip_General scRoot : secScrips )
								{
									TY_StringKeyDesc scCodeDesc = new TY_StringKeyDesc(scRoot.getscCode(), scRoot.getscName());
									retScrips.add(scCodeDesc);

								}
							}
						}

					}
				}
			}
			catch (Exception e)
			{
				EX_General egen = new EX_General("ERR_VHHELP", new Object[]
				{ this.getClass().getSimpleName(), e.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}
		}

		return retScrips;
	}

	/**
	 * Default Generic Implementation returns Scrip Codes and Descriptions
	 */
	@Override
	public <T> ArrayList<T> getvalueHelpList() throws EX_General
	{

		return (ArrayList<T>) this.getScripCodesDesc();
	}

	/************************************************************************************
	 * PRIVATE SECTION
	 * *
	 * 
	 *****************************************************************************
	 */

	/**
	 * ----------------------------------------------------------------------
	 * Populate Root Entities if they are not popuulated
	 * 
	 * @throws Exception
	 *              -------------------------------------------------------------------
	 */
	private void populateRootEntities() throws Exception
	{
		if (this.getScRootList() == null)
		{
			if (shMdtSrv != null)
			{
				IQueryService qs = (IQueryService) QueryManager.getQuerybyRootObjname(shMdtSrv.getRootObjectName());
				if (qs != null)
				{
					this.scRootList = qs.executeQuery();
				}
			}
		}

	}

}
