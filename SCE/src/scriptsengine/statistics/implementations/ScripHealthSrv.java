package scriptsengine.statistics.implementations;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_InvalidObjectException;
import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.exceptions.EX_NotRootException;
import modelframework.exceptions.EX_NullParamsException;
import modelframework.exceptions.EX_ParamCountMismatchException;
import modelframework.exceptions.EX_ParamInitializationException;
import modelframework.implementations.MessagesFormatter;
import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.reportsengine.repDS.definitions.TY_Attr_Container;
import scriptsengine.statistics.JAXB.definitions.StatsAttrDetails;
import scriptsengine.statistics.JAXB.definitions.StatsAttrList;
import scriptsengine.statistics.JAXB.interfaces.IStatsSrvConfigMetadata;
import scriptsengine.statistics.interfaces.IAttribProcessor;
import scriptsengine.statistics.interfaces.IScripHealthSrv;
import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripDataContainerSrv;
import scriptsengine.uploadengine.validations.interfaces.IScripExists;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripHealthSrv implements IScripHealthSrv, ApplicationContextAware
{
	/**
	 * -------------------------- AutoWired Scrip and General Beans ---
	 */
	@Autowired
	private MessagesFormatter			msgFormatter;
	@Autowired
	private IScripExists				scExSrv;
	@Autowired
	private IStatsSrvConfigMetadata		attrMdtSrv;

	/**
	 * -------------------------- AutoWired Scrip and General Beans ---
	 */

	/**
	 * ------------------------------------- Scrip Health Services data placeholders -----------
	 */
	private IScripDataContainerSrv		scDataConSrv;

	private ApplicationContext			appCtxt;

	private ArrayList<TY_Attr_Container>	attrContainers;

	private OB_Scrip_General				scRoot;

	private ScripDataContainer			scDataCon;

	/**
	 * ------------------------------------- Scrip Health Services data placeholders -----------
	 */

	/**
	 * ------------------------------------- Getters and Setters -----------
	 */

	/**
	 * @return the scDataConSrv
	 */
	public IScripDataContainerSrv getScDataConSrv()
	{
		return scDataConSrv;
	}

	/**
	 * @return the scDataCon
	 */
	public ScripDataContainer getScDataCon()
	{
		return scDataCon;
	}

	/**
	 * @return the scRoot
	 */
	public OB_Scrip_General getScRoot()
	{
		return scRoot;
	}

	/**
	 * @param scDataConSrv
	 *             the scDataConSrv to set
	 */
	public void setScDataConSrv(IScripDataContainerSrv scDataConSrv)
	{
		this.scDataConSrv = scDataConSrv;
	}

	/**
	 * @return the attrContainers
	 */
	public ArrayList<TY_Attr_Container> getAttrContainers()
	{
		return attrContainers;
	}

	/**
	 * @param attrContainers
	 *             the attrContainers to set
	 */
	public void setAttrContainers(ArrayList<TY_Attr_Container> attrContainers)
	{
		this.attrContainers = attrContainers;
	}

	/**
	 * ------------------------------------- Getters and Setters -----------
	 */

	/**
	 * ------------- Set Application Context----------------------
	 */
	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		if (ctxt != null)
		{
			this.appCtxt = ctxt;
		}

	}

	/**
	 * ---------------------- Get Scrip Health for Scrip Code ---------------------------
	 */

	@Override
	public ArrayList<TY_Attr_Container> getScripHealthforScrip(String scCode) throws EX_General
	{
		if (this.getAttrContainers() == null)
		{
			if (scCode != null && scExSrv != null && appCtxt != null)
			{
				try
				{
					this.scRoot = this.scExSrv.getScripRootbyCode(scCode);
					if (scRoot != null)
					{
						this.attrContainers = new ArrayList<TY_Attr_Container>();
						if (scDataConSrv == null)
						{
							scDataConSrv = appCtxt.getBean(IScripDataContainerSrv.class);
						}
						if (scDataConSrv != null)
						{
							this.scDataCon = scDataConSrv.getScripDetailsfromDB(scCode);
							if (scDataCon != null)
							{
								/**
								 * Get the ArrayList of Attribute(s) Container for the scDataContainer
								 */

								populateAttributesContainers();

							}
						}

					}
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException
				          | NoSuchMethodException | SecurityException | EX_InvalidObjectException | EX_NotRootException | SQLException
				          | EX_NullParamsException | EX_ParamCountMismatchException | EX_ParamInitializationException
				          | EX_InvalidRelationException e)
				{
					EX_General egen = new EX_General("SCRIPEXISTERROR", new Object[]
					{ e.getMessage()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}
		}

		return getAttrContainers();

	}

	/**
	 * ---------------------- Get Scrip Health for Scrip Description Pattern ---------------------------
	 */
	@Override
	public ArrayList<TY_Attr_Container> getScripHealthforScripDesc(String desc_startswith) throws EX_General
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * -------------------------- PRIVATE SECTION ------------------------------------------
	 * 
	 * @throws EX_General
	 */

	private void populateAttributesContainers() throws EX_General
	{
		if (this.scDataCon != null && attrMdtSrv != null)
		{
			// Get atributes Customized
			StatsAttrList attrList = attrMdtSrv.getStatAtrributesList();
			if (attrList != null)
			{
				if (attrList.getStatAttrs() != null)
				{
					if (attrList.getStatAttrs().size() > 0)
					{
						for ( StatsAttrDetails attribDet : attrList.getStatAttrs() )
						{
							if (appCtxt != null)
							{
								IAttribProcessor attrProcBean = appCtxt.getBean(IAttribProcessor.class);
								if (attrProcBean != null)
								{
									this.attrContainers.add(attrProcBean.getAttribDataforAttrib(scDataCon, attribDet));
								}

							}
						}
					}
				}
			}

		}

	}
}
