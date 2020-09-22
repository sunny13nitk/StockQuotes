package scriptsengine.statistics.implementations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.definitions.Object_Info;
import modelframework.exposed.FrameworkManager;
import modelframework.implementations.DependantObject;
import modelframework.implementations.GeneralMessage;
import modelframework.implementations.MessagesFormatter;
import scriptsengine.enums.SCEenums;
import scriptsengine.reportsengine.repDS.definitions.TY_Attr_Container;
import scriptsengine.statistics.JAXB.definitions.StatsAttrDetails;
import scriptsengine.statistics.alerts.definitions.TY_Alert;
import scriptsengine.statistics.alerts.interfaces.IAlertAware;
import scriptsengine.statistics.definitions.TY_DeltaFigure;
import scriptsengine.statistics.definitions.TY_KeyFigure;
import scriptsengine.statistics.interfaces.IAttribProcessor;
import scriptsengine.statistics.interfaces.IKeyFiguresGenerator;
import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripSheetMetadata;
import scriptsengine.utilities.implementations.DeltaCalcService;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AttribProcessorSrv implements IAttribProcessor, ApplicationContextAware
{
	@Autowired
	private MessagesFormatter	msgFormatter;
	@Autowired
	private IScripSheetMetadata	shMdtSrv;
	@Autowired
	private FrameworkManager		FWMgr;
	@Autowired
	private DeltaCalcService		deltaSrv;

	private ApplicationContext	appCtxt;
	private TY_Attr_Container	attrContainer;

	@SuppressWarnings("unchecked")
	@Override
	public TY_Attr_Container getAttribDataforAttrib(ScripDataContainer scDataCon, StatsAttrDetails attribMdt) throws EX_General
	{
		if (shMdtSrv != null && FWMgr != null)
		{
			// Create Attribute Container Instance
			this.attrContainer = new TY_Attr_Container();
			attrContainer.setAttrName(attribMdt.getAttrName());

			/**
			 * --------------------------------------- KEY FIGURES----------------------------------------------
			 */
			/**
			 * Determine if figures are to be DB driven or driven by a separate Service Bean
			 */
			// Db Driven
			if (attribMdt.getFiguresSrvBean() == null)
			{
				if (attribMdt.getSheetName() != null)
				{
					ArrayList<TY_KeyFigure> keyFigs = this.getKeyFiguresfromDBforAttrib(scDataCon, attribMdt);
					if (keyFigs != null)
					{
						if (keyFigs.size() > 0)
						{
							attrContainer.setKeyFigures(keyFigs);
						}
					}

				}
			}

			else // Service Driven
			{
				IKeyFiguresGenerator keyFigGenBean = (IKeyFiguresGenerator) appCtxt.getBean(attribMdt.getFiguresSrvBean());
				if (keyFigGenBean != null)
				{
					ArrayList<TY_KeyFigure> keyFigs = keyFigGenBean.generateKeyFigures(scDataCon);
					ArrayList<TY_KeyFigure> keyFigsSorted = new ArrayList<TY_KeyFigure>();
					if (keyFigs != null)
					{
						/**
						 * Sort Key Figures by Key in Descending Order : Very Important for Correct Delta
						 * Calculations
						 */
						if (keyFigs.size() > 1)
						{
							keyFigs.stream().sorted((e2, e1) -> Integer.compare(e1.getKey(), e2.getKey())).forEach(e -> keyFigsSorted.add(e));
							attrContainer.setKeyFigures(keyFigsSorted);
						}

					}
				}
			}

			if (attrContainer.getKeyFigures().size() > 0)
			{
				/**
				 * ----------------------------------- DELTA FIGURES----------------------------------------------
				 */
				prepareDeltaFigures();
				if (attrContainer.getDeltaFigures().size() > 0)
				{
					/**
					 * --------------------------------------- AVERAGES----------------------------------
					 */
					computeAverages(attribMdt);
				}
			}

			/**
			 * ---------------------------------------ALERTS PROCESSING----------------------------------------------
			 */
			processAlerts(attribMdt);

			/**
			 * --------------------------------------- LONG TERM TREND----------------------------------------------
			 */
			/**
			 * Process Long Term Trend - For Positivity only if No -ve Alerts have yet been trigerred for the Scrip
			 */

			if (attrContainer.getAlerts() != null)
			{
				if (attrContainer.getAlerts().size() == 0)
				{
					processLongTermTrend(attribMdt);
				}
			}

		}

		// TODO Auto-generated method stub
		return attrContainer;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		if (ctxt != null)
		{
			this.appCtxt = ctxt;
		}

	}

	/**
	 * ----------------------------------- PRIVATE SECTION ------------------------------------
	 */

	@SuppressWarnings("unchecked")
	private ArrayList<TY_KeyFigure> getKeyFiguresfromDBforAttrib(ScripDataContainer scDataCon, StatsAttrDetails attribMdt) throws EX_General
	{
		ArrayList<TY_KeyFigure> keyFigures = new ArrayList<TY_KeyFigure>();
		ArrayList<TY_KeyFigure> keyFiguresSorted = new ArrayList<TY_KeyFigure>();
		String relName = shMdtSrv.getRelationNameforSheetName(attribMdt.getSheetName());
		if (relName != null)
		{
			// Get Sheet Object Type
			String depobjName = shMdtSrv.getSheetMdtbySheetName(attribMdt.getSheetName()).getBobjName();

			// Get Object info details for the Object
			// Get Object Information from Object Factory
			Object_Info obj_info = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(depobjName);
			if (obj_info != null)
			{

				@SuppressWarnings("rawtypes")
				SheetEntities depObjList = scDataCon.getEntitiesforSheet(attribMdt.getSheetName());
				if (depObjList != null)
				{
					ArrayList<DependantObject> entList = depObjList.getSheetEntityList();
					if (entList != null && entList.size() > 0)
					{
						for ( DependantObject depObj : entList )
						{

							TY_KeyFigure keyfigure = new TY_KeyFigure();
							if (attribMdt.getKeyfldName() != null && attribMdt.getFieldName() != null)
							{
								/**
								 * Get Getters for Field Name and Key Name
								 */
								// Get Getter Name for Key field
								Method getter = obj_info.Get_Getter_for_FieldName(attribMdt.getKeyfldName());
								if (getter != null)
								{
									// Get the value for the parameter from POJO
									Object value;
									try
									{
										value = getter.invoke(depObj, new Object[] {});
										if (value != null)
										{
											keyfigure.setKey((int) value);
										}
									}
									catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
									{
										EX_General msgChgErr = new EX_General("ERR_SS_VALGET", new Object[]
										{ attribMdt.getKeyfldName(), depObj.getClass().getSimpleName(), scDataCon.getScRoot().getscCode(),
										          e.getMessage()
										});
										msgFormatter.generate_message_snippet(msgChgErr);
										throw msgChgErr;
									}

								}

								// Get Getter Name for Value field
								getter = null;
								getter = obj_info.Get_Getter_for_FieldName(attribMdt.getFieldName());
								if (getter != null)
								{
									// Get the value for the parameter from POJO
									Object value;
									try
									{
										value = getter.invoke(depObj, new Object[] {});
										if (value != null)
										{
											keyfigure.setFigure((double) value);
										}
									}
									catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
									{
										EX_General msgChgErr = new EX_General("ERR_SS_VALGET", new Object[]
										{ attribMdt.getFieldName(), depObj.getClass().getSimpleName(), scDataCon.getScRoot().getscCode(),
										          e.getMessage()
										});
										msgFormatter.generate_message_snippet(msgChgErr);
										throw msgChgErr;
									}

								}

								keyFigures.add(keyfigure);

							}
						}
					}

				}
			}

		}
		/**
		 * Sort Key Figures by key descending - So DElta are calcualted properly
		 */

		keyFigures.stream().sorted((e2, e1) -> Integer.compare(e1.getKey(), e2.getKey())).forEach(e -> keyFiguresSorted.add(e));

		return keyFiguresSorted;
	}

	/**
	 * Prepare Delta Figures
	 */
	private void prepareDeltaFigures()
	{
		/**
		 * Get Min'm and Max Year
		 */

		int minYear = attrContainer.getKeyFigures().stream().mapToInt(TY_KeyFigure::getKey).min().getAsInt();
		int maxYear = attrContainer.getKeyFigures().stream().mapToInt(TY_KeyFigure::getKey).max().getAsInt();

		/**
		 * Start With Min'm Year and Traverse to Max'm Year
		 */
		for ( int cyear = minYear; cyear < maxYear; cyear++ )
		{
			TY_DeltaFigure deltaFig = new TY_DeltaFigure();
			if (deltaSrv != null)
			{
				int compyear = cyear;
				deltaFig.setDeltaKey(cyear + "-" + (cyear + 1));
				deltaFig.setFigure(deltaSrv.getDeltaPercentage(
				          (attrContainer.getKeyFigures().stream().filter(x -> x.getKey() == compyear)).findFirst().get().getFigure(),
				          attrContainer.getKeyFigures().stream().filter(x -> x.getKey() == compyear + 1).findFirst().get().getFigure()));
				attrContainer.getDeltaFigures().add(deltaFig);
			}

		}

	}

	private void computeAverages(StatsAttrDetails attribMdt)
	{
		if (attribMdt.isAvgON())
		{
			attrContainer.setAvg(attrContainer.getKeyFigures().stream().mapToDouble(TY_KeyFigure::getFigure).average().getAsDouble());
		}
		if (attribMdt.isAvgdeltaON())
		{
			attrContainer.setDeltaAvg(attrContainer.getDeltaFigures().stream().mapToDouble(TY_DeltaFigure::getFigure).average().getAsDouble());
		}
	}

	private void processAlerts(StatsAttrDetails attribMdt) throws EX_General
	{
		if (attribMdt.isAlertON())
		{
			if (attribMdt.getAlertDetSrvBean() != null && appCtxt != null)
			{
				try
				{
					IAlertAware alertProcBean = (IAlertAware) appCtxt.getBean(attribMdt.getAlertDetSrvBean());
					if (alertProcBean != null)
					{
						attrContainer.setAlerts(alertProcBean.processAlertsforAttribContainer(attrContainer));
					}
				}
				catch (Exception e)
				{
					EX_General msgChgErr = new EX_General("AL_SRV_ERR", new Object[]
					{ attribMdt.getAttrName(), e.getMessage()
					});
					msgFormatter.generate_message_snippet(msgChgErr);
					throw msgChgErr;
				}
			}
		}
	}

	private void processLongTermTrend(StatsAttrDetails attribMdt)
	{
		boolean lTTrendInc = attribMdt.isLongtermIncON();
		boolean alert = true;
		if (attrContainer.getDeltaFigures().size() >= 2)
		{
			int size = attrContainer.getDeltaFigures().size();

			for ( int x = (size - 1); x > 0; x-- )
			{
				double figcurr = attrContainer.getDeltaFigures().get(x).getFigure();
				double figpen = attrContainer.getDeltaFigures().get(x - 1).getFigure();

				if (lTTrendInc) // Should be Increasing Trend
				{
					if (figpen > figcurr)
					{
						alert = false;
					}
				}
				else // Should be Decreasing Trend
				{
					if (figpen < figcurr)
					{
						alert = false;
					}
				}

			}

			if (alert)
			{
				if (lTTrendInc)
				{
					GeneralMessage msgChgErr = new GeneralMessage("AL_LTT_INC", new Object[]
					{ attribMdt.getAttrName()
					});
					attrContainer.getAlerts().add(new TY_Alert(SCEenums.alertMode.AGAINST, msgFormatter.generate_message_snippet(msgChgErr)));
				}
				else
				{
					GeneralMessage msgChgErr = new GeneralMessage("AL_LTT_DEC", new Object[]
					{ attribMdt.getAttrName()
					});
					attrContainer.getAlerts().add(new TY_Alert(SCEenums.alertMode.AGAINST, msgFormatter.generate_message_snippet(msgChgErr)));
				}
			}

		}

	}

}
