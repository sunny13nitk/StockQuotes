package scriptsengine.aspects;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import modelframework.definitions.Object_Info;
import modelframework.exposed.FrameworkManager;
import modelframework.implementations.DependantObject;
import modelframework.implementations.GeneralMessage;
import modelframework.implementations.MessagesFormatter;
import modelframework.interfaces.IEnumable;
import scriptsengine.enums.SCEenums;
import scriptsengine.enums.SCEenums.alertType;
import scriptsengine.reportsengine.repDS.annotations.AlertSubscribeTrendDelta;
import scriptsengine.reportsengine.repDS.annotations.AlertSubscribeTrendPenultimate;
import scriptsengine.reportsengine.repDS.annotations.ReportRefFieldName;
import scriptsengine.reportsengine.repDS.definitions.TY_AlertInfo;
import scriptsengine.reportsengine.repDS.definitions.TY_Attr_Container;
import scriptsengine.reportsengine.repDS.definitions.TY_ReportDS_fldInfo;
import scriptsengine.reportsengine.repDS.definitions.TY_ScRoot_AttrContainers;
import scriptsengine.reportsengine.repDS.interfaces.IReportDataSource;
import scriptsengine.statistics.JAXB.interfaces.IStatsSrvConfigMetadata;
import scriptsengine.statistics.alerts.definitions.TY_Alert;
import scriptsengine.statistics.alerts.interfaces.IGenericDeltaAlerts;
import scriptsengine.statistics.definitions.TY_AttrKeyFigures;
import scriptsengine.statistics.definitions.TY_AttribKeyFigure;
import scriptsengine.statistics.definitions.TY_AttribSingleVal;
import scriptsengine.statistics.definitions.TY_DeltaFigure;
import scriptsengine.statistics.definitions.TY_KeyFigure;
import scriptsengine.statistics.interfaces.IKeyFiguresGenerator;
import scriptsengine.statistics.interfaces.ISingleValGenerator;
import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripDataContainerSrv;
import scriptsengine.uploadengine.services.interfaces.IScripSheetMetadata;
import scriptsengine.uploadengine.validations.interfaces.IScripExists;
import scriptsengine.utilities.implementations.DeltaCalcService;

@Aspect
@Component
public class ReportDataSourceAspect implements ApplicationContextAware
{

	@Autowired
	private MessagesFormatter		msgFormatter;

	@Autowired
	private DeltaCalcService			deltaSrv;

	@Autowired
	private IScripExists			scExSrv;
	@Autowired
	private IStatsSrvConfigMetadata	attrMdtSrv;

	@Autowired
	private IScripSheetMetadata		shMdtSrv;

	private ApplicationContext		appCtxt;

	private final String			constantFaceVal	= "FaceValue";

	/**
	 * ----------------------- Advice to handle Report Data Source Generation ------------------------
	 * This advice is Cacheable for the Calling IReportDataSource Implementation with key generated in DSKeyGenerator
	 * Service bean.
	 * Cacke Key = Implementation Name + parameters supplied
	 * 
	 * @param pjp
	 * @param scCode
	 * @return
	 * @throws EX_General
	 *              -----------------------------------------------------------------------------------
	 */

	@Around("ReportDataSourceInterface() && generateDataSource() && args(scCode)")
	public TY_ScRoot_AttrContainers generateDataSourceforScCode(ProceedingJoinPoint pjp, String scCode) throws EX_General
	{

		TY_ScRoot_AttrContainers scRoot_attrContainers = null;
		ArrayList<TY_ReportDS_fldInfo> repfldsInfoList = new ArrayList<TY_ReportDS_fldInfo>();

		if (pjp != null)
		{
			if (pjp.getTarget() != null && scExSrv != null && attrMdtSrv != null && scCode != null)
			{
				if (pjp.getTarget() instanceof IReportDataSource)
				{
					/**
					 * Get All Declared fields in Report Data Source Implementation
					 */
					Field[] properties = pjp.getTarget().getClass().getFields();
					if (properties != null)
					{
						if (properties.length > 0)
						{
							scRoot_attrContainers = new TY_ScRoot_AttrContainers();
							/**
							 * Validate Scrip Code First - If Found set in return Structure
							 */
							try
							{
								scRoot_attrContainers.setScRoot(scExSrv.getScripRootbyCode(scCode));
							}
							catch (Exception e)
							{
								EX_General msgChgErr = new EX_General("SCRIPEXISTERROR", new Object[]
								{ e.getMessage()
								});
								msgFormatter.generate_message_snippet(msgChgErr);
								throw msgChgErr;
							}

							/**
							 * Get Annotaion Specific Info. in Structures for further processing
							 */
							for ( Field field : properties )
							{
								try
								{
									repfldsInfoList.add(getfldInfoforImplFld(field, pjp.getTarget()));
								}
								catch (Exception e)
								{
									// ERR_SCDS_GEN = Error generating Scrip Data Source for DS Service - {0}
									// on field - {1} - Details - {2}!!
									EX_General msgChgErr = new EX_General("ERR_SCDS_GEN", new Object[]
									{ this.getClass().getSimpleName(), field.getName(), e.getMessage()
									});
									msgFormatter.generate_message_snippet(msgChgErr);
									throw msgChgErr;

								}
							}

							/**
							 * Populate Key Figures for all determined Attriutes
							 */
							populateKeyFigsforAttribs(scRoot_attrContainers, repfldsInfoList);

							/**
							 * Prepare Delta for Key Figures
							 */
							prepareDeltaFiguresandAverages(scRoot_attrContainers, repfldsInfoList);

							/**
							 * Process Alerts
							 */
							processAlerts(scRoot_attrContainers, repfldsInfoList);

							/**
							 * Process Long Term Trends
							 */
							processLongTermTrends(scRoot_attrContainers, repfldsInfoList);

						}
					}

				}
			}
		}

		return scRoot_attrContainers;

	}

	/**
	 * -------------------------------------------------------------
	 * Application Context Initialization
	 * ------------------------------------------------------------------
	 */
	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		if (ctxt != null)
		{
			appCtxt = ctxt;
		}

	}

	/**
	 * ------------------------------ PRIVATE SECTION -----------------------------------------------
	 */

	/**
	 * -----------------------------------------------------------------------------------------
	 * Get FieldInfo Runtime Information for Data Source Information Interface Implementation field
	 * 
	 * @param fld
	 *             - Field type - Impl. Field for Interface Data Source Impl
	 * @return - Fld Metadata from attribute Mdt Srv and Annotations defined on field
	 *         -------------------------------------------------------------------------------------
	 * @throws Exception
	 */

	private TY_ReportDS_fldInfo getfldInfoforImplFld(Field field, Object obj) throws Exception
	{
		TY_ReportDS_fldInfo fldInfo = new TY_ReportDS_fldInfo();

		/**
		 * REf Field Annotation
		 */
		if (field.isAnnotationPresent(ReportRefFieldName.class))
		{
			Annotation annotation = field.getAnnotation(ReportRefFieldName.class);
			ReportRefFieldName refFldAnnotation = (ReportRefFieldName) annotation;
			if (refFldAnnotation != null)
			{
				/**
				 * Get Report Metadata Reference Field Asigned usng Annotation
				 */
				fldInfo.setRefAttrName(refFldAnnotation.attrName());

				/**
				 * Get the field Label from Implementation class
				 * Returns the value of the field represented by this Field, on the specified object. The value is
				 * automatically wrapped in an object if it has a primitive type.
				 */
				String label = (String) field.get(obj);

				fldInfo.setLabel(label);

				/**
				 * Get Attribute Metadata defined as per SS.xml
				 */
				if (attrMdtSrv != null)
				{
					fldInfo.setAttribMdt(attrMdtSrv.getattrDetailsbyAttrName(fldInfo.getRefAttrName()));
				}

			}
		}

		/**
		 * Alerts Annotation - Delta Avg Trend Alert(s)
		 */
		if (field.getAnnotationsByType(AlertSubscribeTrendDelta.class).length > 0)
		{
			/**
			 * Validate Delta Alert Annotation is not on a single val field
			 */
			validate_SingleValFieldDeltaAlerts(fldInfo);
			Annotation[] annotations = field.getAnnotationsByType(AlertSubscribeTrendDelta.class);
			if (annotations.length > 0)
			{
				for ( Annotation annotation : annotations )
				{
					AlertSubscribeTrendDelta trendAnnotation = (AlertSubscribeTrendDelta) annotation;
					if (trendAnnotation != null)
					{
						TY_AlertInfo alertInfo = new TY_AlertInfo(alertType.TREND, trendAnnotation.Occurances(),
						          trendAnnotation.TrendDirection(), trendAnnotation.valueToCompare(), trendAnnotation.msgID(),
						          trendAnnotation.isMsgGeneric(), trendAnnotation.alertMode());
						fldInfo.getAlertsList().add(alertInfo);
					}
				}
			}

		}

		/**
		 * Alerts Annotation - Delta Avg Penultimate Alert(s)
		 */
		if (field.getAnnotationsByType(AlertSubscribeTrendPenultimate.class).length > 0)
		{
			/**
			 * Validate Delta Alert Annotation is not on a single val field
			 */
			validate_SingleValFieldDeltaAlerts(fldInfo);
			Annotation[] annotations = field.getAnnotationsByType(AlertSubscribeTrendPenultimate.class);
			if (annotations.length > 0)
			{
				for ( Annotation annotation : annotations )
				{
					AlertSubscribeTrendPenultimate trendAnnotation = (AlertSubscribeTrendPenultimate) annotation;
					if (trendAnnotation != null)
					{
						TY_AlertInfo alertInfo = new TY_AlertInfo(alertType.PENULTIMATE, 1, trendAnnotation.TrendDirection(), 1,
						          trendAnnotation.msgID(), trendAnnotation.isMsgGeneric(), trendAnnotation.alertMode());
						fldInfo.getAlertsList().add(alertInfo);
					}
				}
			}

		}

		return fldInfo;
	}

	/**
	 * --------------------------------------------------------------------------------------------------
	 * Validate Single Value Field for Delta Alert Annotation
	 * 
	 * @param fldInfo
	 *             - Field Information
	 * @throws EX_General
	 *              - Exception in Case Alert annotation is configured for a Single Value field
	 *              --------------------------------------------------------------------------------------------------
	 */

	private void validate_SingleValFieldDeltaAlerts(TY_ReportDS_fldInfo fldInfo) throws EX_General
	{
		if (fldInfo != null)
		{
			if (fldInfo.getAttribMdt().isSingleVal())
			{
				EX_General msgChgErr = new EX_General("ERR_SINGLEVAL_DELTAALERT", new Object[]
				{ fldInfo.getRefAttrName()
				});
				msgFormatter.generate_message_snippet(msgChgErr);
				throw msgChgErr;
			}
		}
	}

	/**
	 * ---------------------------------------------------------------------------------------------
	 * Populate Keys Figures for Requested Attributes
	 * 
	 * @param scRoot_attrContainers
	 *             - Scrip Root Attrib Container - will be populated with key figures for attributes
	 * @param repfldsInfoList
	 *             - Fields Info List
	 *             ----------------------------------------------------------------------------------
	 * @throws EX_General
	 */
	private void populateKeyFigsforAttribs(TY_ScRoot_AttrContainers scRoot_attrContainers, ArrayList<TY_ReportDS_fldInfo> repfldsInfoList)
	          throws EX_General
	{
		ArrayList<TY_AttrKeyFigures> attrKeyFigsList = new ArrayList<TY_AttrKeyFigures>();
		ArrayList<TY_AttribSingleVal> attrSingleValsList = new ArrayList<TY_AttribSingleVal>();

		ArrayList<String> processedSheetNames = new ArrayList<String>();
		/**
		 * Get Scrip Data Container
		 */
		ScripDataContainer scDataCon = null;
		if (appCtxt != null)
		{
			IScripDataContainerSrv scDataConSrv = appCtxt.getBean(IScripDataContainerSrv.class);
			if (scDataConSrv != null)
			{
				scDataCon = scDataConSrv.getScripDetailsfromDB(scRoot_attrContainers.getScRoot().getscCode());
			}
		}

		for ( TY_ReportDS_fldInfo fldInfo : repfldsInfoList )
		{
			// DB Processing for Key Figures
			if (fldInfo.getAttribMdt().getSheetName() != null)
			{
				String sheetNameCurr = fldInfo.getAttribMdt().getSheetName();
				/**
				 * Check if sheet already processed
				 */

				if (sheetNameCurr != null && SheetNotProcessed(sheetNameCurr, processedSheetNames))
				{
					ArrayList<TY_ReportDS_fldInfo> selSheetfldInfo = new ArrayList<TY_ReportDS_fldInfo>();
					ArrayList<TY_ReportDS_fldInfo> selSheetfldSheetMaintained = new ArrayList<TY_ReportDS_fldInfo>();
					/**
					 * Separate Fields Info for Selected sheet
					 */
					selSheetfldSheetMaintained = repfldsInfoList.stream().filter(x -> x.getAttribMdt().getSheetName() != null)
					          .collect(Collectors.toCollection(ArrayList::new));
					selSheetfldInfo = selSheetfldSheetMaintained.stream().filter(x -> x.getAttribMdt().getSheetName().equals(sheetNameCurr))
					          .collect(Collectors.toCollection(ArrayList::new));

					/**
					 * Field is not Single Value Field
					 */
					if (!fldInfo.getAttribMdt().isSingleVal())
					{
						attrKeyFigsList.addAll(getAttribKeyFigsforSelectedAttribs(scDataCon, selSheetfldInfo));
					}
					else
					{
						/**
						 * Field is Single Value Field - Process accordingly by a separate Service that returns
						 * Key - String and Value Double and append to corresponding ArrayList in
						 * ScRoot_AttrContainers
						 */
						attrSingleValsList.addAll(getSingleValsforSelectedAttribs(scDataCon, selSheetfldInfo));

					}
					/**
					 * No Subsequent procesing of Same sheet
					 */
					processedSheetNames.add(sheetNameCurr);

				}

			}

			else // Key Figures Service Bean Processing
			{
				/**
				 * If the Field is Not single Value Candidate
				 */
				if (!fldInfo.getAttribMdt().isSingleVal())
				{
					IKeyFiguresGenerator keyFigGenBean = (IKeyFiguresGenerator) appCtxt.getBean(fldInfo.getAttribMdt().getFiguresSrvBean());
					if (keyFigGenBean != null)
					{
						TY_AttrKeyFigures attrKeyFigs = new TY_AttrKeyFigures();
						attrKeyFigs.setAttrName(fldInfo.getRefAttrName());
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
								keyFigs.stream().sorted((e2, e1) -> Integer.compare(e1.getKey(), e2.getKey()))
								          .forEach(e -> keyFigsSorted.add(e));
								attrKeyFigs.setKeyFigs(keyFigsSorted);
								attrKeyFigsList.add(attrKeyFigs);
							}

						}
					}
				}

				else
				{
					/**
					 * Field is Single Value Field - Process accordingly by a separate Service that returns
					 * Key - String and Value Double and append to corresponding ArrayList in
					 * ScRoot_AttrContainers
					 */
					ISingleValGenerator SVGenBean = (ISingleValGenerator) appCtxt.getBean(fldInfo.getAttribMdt().getFiguresSrvBean());
					if (SVGenBean != null)
					{
						TY_AttribSingleVal SVG_instance = SVGenBean.generateSinglValueAttribute(scDataCon);
						if (SVG_instance.getValue() != null)
						{
							SVG_instance.setAttrName(fldInfo.getRefAttrName());
							attrSingleValsList.add(SVG_instance);
						}
					}

				}

			}

		}

		/*------------------------------------------------------------------------------------------------------
		 * According to Order of Specified fields in Data Source Implementation populate in ScRootAttribute
		 * Containers the Key figures and Single Value Attributes
		 * ----------------------------------------------------------------------------------------------------*/
		if (attrKeyFigsList.size() > 0)
		{
			for ( TY_ReportDS_fldInfo fldInfo : repfldsInfoList )
			{
				if (!fldInfo.getAttribMdt().isSingleVal())
				{

					TY_AttrKeyFigures selkeyFigs = attrKeyFigsList.stream().filter(x -> x.getAttrName().equals(fldInfo.getRefAttrName()))
					          .findFirst().get();
					/*
					 * Popoulate Attribute Containers In Order of Field specification in Implementation of DS
					 * Interface
					 */
					if (selkeyFigs != null)
					{
						TY_Attr_Container attrContainer = new TY_Attr_Container();
						attrContainer.setAttrName(fldInfo.getRefAttrName());
						attrContainer.setAttrLabel(fldInfo.getLabel());
						attrContainer.setKeyFigures(selkeyFigs.getKeyFigs());
						scRoot_attrContainers.getAttrContainers().add(attrContainer);
					}
				}
				else if (fldInfo.getAttribMdt().isSingleVal())
				{
					/*
					 * Populate Single Value Containers In Order of Field specification in Implementation of DS
					 * Interface
					 */

					{
						TY_AttribSingleVal selsingleVal = attrSingleValsList.stream()
						          .filter(x -> x.getAttrName().equals(fldInfo.getRefAttrName())).findFirst().get();
						/*
						 * Popoulate Attribute Containers In Order of Field specification in Implementation of DS
						 * Interface
						 */
						if (selsingleVal != null)
						{
							selsingleVal.setAttrName(fldInfo.getRefAttrName());
							selsingleVal.setAttrLabel(fldInfo.getLabel());

							scRoot_attrContainers.getSingleValContainers().add(selsingleVal);
						}

					}
				}
			}
		}
	}

	/**
	 * ---------------------------------------------------------------------------------------
	 * Validate if Sheet Has Already been processed foe Key Figures
	 * 
	 * @param currSheet
	 *             - currentSheet for Evaluation
	 * @param sheetsList
	 *             - List of processed sheets
	 * @return - true if Sheet is not processed
	 *         --------------------------------------------------------------------------------
	 */
	private boolean SheetNotProcessed(String currSheet, ArrayList<String> sheetsList)
	{
		boolean sheetNotProcessed = true;

		if (sheetsList.size() > 0)
		{
			try
			{
				String sheetFoundName = sheetsList.stream().filter(x -> x.equals(currSheet)).findFirst().get();
				if (sheetFoundName != null)
				{
					sheetNotProcessed = false;
				}
			}
			catch (NoSuchElementException e)
			{
				// Do Nothing - Just Catch
			}
		}
		return sheetNotProcessed;
	}

	/**
	 * ---------------------------------------------------------------------------------------------------------
	 * Get Attribute Name and KeyFigures Arraylist Computed for Selected Sheet Fields REquested
	 * 
	 * @param scDataCon
	 *             - Scrip Data Container- Buffer Supported for a scrip code
	 * @param selSheetFldsInfoList
	 *             - Selected fields requested for current sheet
	 * @return - Array List of Attribute Name and KeyFigures Arraylist
	 *         ------------------------------------------------------------------------------------------------
	 * @throws EX_General
	 */
	private ArrayList<TY_AttrKeyFigures> getAttribKeyFigsforSelectedAttribs(ScripDataContainer scDataCon,
	          ArrayList<TY_ReportDS_fldInfo> selSheetFldsInfoList) throws EX_General
	{
		ArrayList<TY_AttrKeyFigures> attrKeyFigs = new ArrayList<TY_AttrKeyFigures>();

		ArrayList<TY_AttribKeyFigure> attrkeyfigFlatList = new ArrayList<TY_AttribKeyFigure>();

		String sheetName = selSheetFldsInfoList.get(0).getAttribMdt().getSheetName();

		String relName = shMdtSrv.getRelationNameforSheetName(sheetName);
		Method getter = null;
		if (relName != null)
		{
			// Get Sheet Object Type
			String depobjName = shMdtSrv.getSheetMdtbySheetName(sheetName).getBobjName();

			// Get Object info details for the Object
			// Get Object Information from Object Factory
			Object_Info obj_info = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(depobjName);
			if (obj_info != null)
			{

				@SuppressWarnings("rawtypes")
				SheetEntities depObjList = scDataCon.getEntitiesforSheet(sheetName);
				if (depObjList != null)
				{
					ArrayList<DependantObject> entList = depObjList.getSheetEntityList();
					if (entList != null && entList.size() > 0)
					{
						for ( DependantObject depObj : entList )
						{
							boolean nextEnt = true;
							int key = 0;
							for ( TY_ReportDS_fldInfo selfldInfo : selSheetFldsInfoList )
							{

								TY_AttribKeyFigure attrkeyFigFlat = new TY_AttribKeyFigure();
								attrkeyFigFlat.setAttrib(selfldInfo.getRefAttrName()); // Field Name Set

								if (selfldInfo.getAttribMdt().getKeyfldName() != null && selfldInfo.getAttribMdt().getFieldName() != null)
								{
									/**
									 * Get Getters for Field Name and Key Name
									 */
									if (nextEnt == true) // Fetch key only once for a row traverse for a single
									                     // Key
									{
										// Get Getter Name for Key field
										getter = obj_info.Get_Getter_for_FieldName(selfldInfo.getAttribMdt().getKeyfldName());
										if (getter != null)
										{
											// Get the value for the parameter from POJO
											Object value;
											try
											{
												value = getter.invoke(depObj, new Object[] {});
												if (value != null)
												{
													attrkeyFigFlat.setKey((int) value); // Field Key Set
													key = attrkeyFigFlat.getKey();
													nextEnt = false; // Set to false in same record loop
													                 // read traverse
												}
											}
											catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
											{
												EX_General msgChgErr = new EX_General("ERR_SS_VALGET", new Object[]
												{ selfldInfo.getAttribMdt().getKeyfldName(), depObj.getClass().getSimpleName(),
												          scDataCon.getScRoot().getscCode(), e.getMessage()
												});
												msgFormatter.generate_message_snippet(msgChgErr);
												throw msgChgErr;
											}

										}
									}
									else
									{
										/**
										 * Key will be same for Same Relation entity for a different Attribute
										 * - No need to process key unless entity changes
										 */
										attrkeyFigFlat.setKey(key); // Field Ley Set
									}

									// Get Getter Name for Value field
									getter = null;
									getter = obj_info.Get_Getter_for_FieldName(selfldInfo.getAttribMdt().getFieldName());
									if (getter != null)
									{
										// Get the value for the parameter from POJO
										Object value;
										try
										{
											value = getter.invoke(depObj, new Object[] {});
											if (value != null)
											{
												/**
												 * Check if needs to be calcualted w.r.t face value
												 */
												if (selfldInfo.getAttribMdt().isWrtFaceValue())
												{
													/**
													 * Get getter for face value and get the face value by
													 * invoking getter
													 */
													double fval = getFaceValuefordepObj(depObj, obj_info);
													if (fval > 0)
													{
														value = (double) value / fval;
													}

												}

												attrkeyFigFlat.setFigure((double) value);
											}
										}
										catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
										{
											EX_General msgChgErr = new EX_General("ERR_SS_VALGET", new Object[]
											{ selfldInfo.getAttribMdt().getFieldName(), depObj.getClass().getSimpleName(),
											          scDataCon.getScRoot().getscCode(), e.getMessage()
											});
											msgFormatter.generate_message_snippet(msgChgErr);
											throw msgChgErr;
										}

									}
								}

								attrkeyfigFlatList.add(attrkeyFigFlat);

							}
						}

					}
				}
			}
		}

		/*
		 * Rearrange Flat structure to Individual Attribute KeyFigures List
		 */
		for ( TY_ReportDS_fldInfo selfldInfo : selSheetFldsInfoList )
		{
			ArrayList<TY_AttribKeyFigure> selAttribKeyFig = new ArrayList<TY_AttribKeyFigure>();
			ArrayList<TY_AttribKeyFigure> selAttribKeyFigSorted = new ArrayList<TY_AttribKeyFigure>();
			selAttribKeyFig = attrkeyfigFlatList.stream().filter(x -> x.getAttrib().equals(selfldInfo.getRefAttrName()))
			          .collect(Collectors.toCollection(ArrayList::new));
			/*
			 * Sort Key Figures by key descending - So DElta are calcualted properly
			 */

			selAttribKeyFig.stream().sorted((e2, e1) -> Integer.compare(e1.getKey(), e2.getKey())).forEach(e -> selAttribKeyFigSorted.add(e));

			/*
			 * Prepare the final Structure that could be appended to return list
			 */
			TY_AttrKeyFigures attrKeyFigsprep = new TY_AttrKeyFigures();
			attrKeyFigsprep.setAttrName(selfldInfo.getRefAttrName());
			for ( TY_AttribKeyFigure ty_AttribKeyFigure : selAttribKeyFigSorted )
			{
				attrKeyFigsprep.getKeyFigs().add(new TY_KeyFigure(ty_AttribKeyFigure.getKey(), ty_AttribKeyFigure.getFigure()));
			}

			attrKeyFigs.add(attrKeyFigsprep);

		}

		return attrKeyFigs;

	}

	/**
	 * ------------------------------------------------------------------------------------------------
	 * Get Face Value for Depenedant object entity
	 * 
	 * @param depObj
	 *             - Dependent Object Instance
	 * @param obj_info
	 *             - Dependant object Info structure
	 * @return - Face Value if face value field found in dependant Oject as per Specified Constant
	 * @throws EX_General
	 *              - Exception
	 *              -----------------------------------------------------------------------------------
	 */
	private double getFaceValuefordepObj(DependantObject depObj, Object_Info obj_info) throws EX_General
	{
		double fv = 0;

		Method getter = null;
		getter = obj_info.Get_Getter_for_FieldName(this.constantFaceVal);
		if (getter != null)
		{
			// Get the value for the parameter from POJO
			Object value;
			try
			{
				value = getter.invoke(depObj, new Object[] {});
				if (value != null)
				{
					fv = (double) value;
				}

			}

			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				EX_General msgChgErr = new EX_General("ERR_SS_VALGET", new Object[]
				{ this.constantFaceVal, depObj.getClass().getSimpleName(), "NA", e.getMessage()
				});
				msgFormatter.generate_message_snippet(msgChgErr);
				throw msgChgErr;
			}

		}

		return fv;

	}

	/**
	 * --------------------------------------------------------------------------------------------------------
	 * Get Single Values for Selected Attributes
	 * 
	 * @param scDataCon
	 *             - Scrip Data Container
	 * @param selSheetFldsInfoList
	 *             - Selected Sheet Fields subscribed for getting Single Values
	 * @return - ArrayList of Name Value pairs for Requested fields
	 * @throws EX_General
	 *              - Exception
	 *              ------------------------------------------------------------------------------------------
	 */
	private ArrayList<TY_AttribSingleVal> getSingleValsforSelectedAttribs(ScripDataContainer scDataCon,
	          ArrayList<TY_ReportDS_fldInfo> selSheetFldsInfoList) throws EX_General
	{
		ArrayList<TY_AttribSingleVal> singleValsList = new ArrayList<TY_AttribSingleVal>();

		String sheetName = selSheetFldsInfoList.get(0).getAttribMdt().getSheetName();

		String relName = shMdtSrv.getRelationNameforSheetName(sheetName);
		Method getter = null;
		if (relName != null)
		{
			// Get Sheet Object Type
			String depobjName = shMdtSrv.getSheetMdtbySheetName(sheetName).getBobjName();

			// Get Object info details for the Object
			// Get Object Information from Object Factory
			Object_Info obj_info = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(depobjName);
			if (obj_info != null)
			{

				@SuppressWarnings("rawtypes")
				SheetEntities depObjList = scDataCon.getEntitiesforSheet(sheetName);
				if (depObjList != null)
				{
					ArrayList<DependantObject> entList = depObjList.getSheetEntityList();
					if (entList != null && entList.size() > 0)
					{
						for ( DependantObject depObj : entList )
						{

							for ( TY_ReportDS_fldInfo selfldInfo : selSheetFldsInfoList )
							{

								TY_AttribSingleVal singleVal = new TY_AttribSingleVal();
								// Field Name Set will be replaced by label finally to maintain Order in calling
								// method
								singleVal.setAttrName(selfldInfo.getRefAttrName());

								if (selfldInfo.getAttribMdt().getFieldName() != null)
								{
									/**
									 * Get Getters for Field Name and Key Name
									 */

									// Get Getter Name for Key field
									getter = obj_info.Get_Getter_for_FieldName(selfldInfo.getAttribMdt().getFieldName());
									if (getter != null)
									{
										// Get the value for the parameter from POJO
										Object value;
										try
										{
											value = getter.invoke(depObj, new Object[] {});
											if (value != null)
											{
												Class<?> retType = getter.getReturnType();
												singleVal.setAttrType(retType.getSimpleName());
												switch (retType.getSimpleName())
												{
													case "Int":
													case "int":
													case "Integer":
														singleVal.setValue((int) value);
														break;

													case "Double":
													case "double":
														singleVal.setValue((double) value);
														break;

													case "String":
														singleVal.setValue(value);
														break;

													case "Boolean":
													case "boolean":
														singleVal.setValue((boolean) value);
														break;

													default: // Enums that implement IEnumable
													         // Interface

														Object enum_obj = value;
														if (enum_obj instanceof IEnumable)
														{
															IEnumable casted_obj = (IEnumable) enum_obj;
															singleVal.setValue(casted_obj.Get_Value_From_Enums(enum_obj));

														}

														break;
												}

											}
										}
										catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
										{
											EX_General msgChgErr = new EX_General("ERR_SS_VALGET", new Object[]
											{ selfldInfo.getAttribMdt().getKeyfldName(), depObj.getClass().getSimpleName(),
											          scDataCon.getScRoot().getscCode(), e.getMessage()
											});
											msgFormatter.generate_message_snippet(msgChgErr);
											throw msgChgErr;
										}

									}
									singleValsList.add(singleVal);

								}

							}
						}

					}
				}
			}
		}

		return singleValsList;

	}

	/**
	 * -------------------------------------------------------------------------------------------------
	 * PRepare DElta Figures for Attribute Container(s) which have key figures
	 * 
	 * @param scRoot_attrContainers
	 *             -------------------------------------------------------------------------------------
	 */
	private void prepareDeltaFiguresandAverages(TY_ScRoot_AttrContainers scRoot_attrContainers, ArrayList<TY_ReportDS_fldInfo> reportDS_fldInfo)
	{

		for ( TY_Attr_Container attrContainer : scRoot_attrContainers.getAttrContainers() )
		{
			ArrayList<TY_DeltaFigure> deltaFigs = new ArrayList<TY_DeltaFigure>();
			if (attrContainer.getKeyFigures() != null)
			{
				if (attrContainer.getKeyFigures().size() >= 2)
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

							// Avoid divide by zero error
							if ((attrContainer.getKeyFigures().stream().filter(x -> x.getKey() == compyear)).findFirst().get()
							          .getFigure() != 0)
							{
								deltaFig.setFigure(deltaSrv.getDeltaPercentage(
								          (attrContainer.getKeyFigures().stream().filter(x -> x.getKey() == compyear)).findFirst().get()
								                    .getFigure(),
								          attrContainer.getKeyFigures().stream().filter(x -> x.getKey() == compyear + 1).findFirst().get()
								                    .getFigure()));
							}
							else
							{
								deltaFig.setFigure(0);
							}

							deltaFigs.add(deltaFig);
						}

					}
					attrContainer.setDeltaFigures(deltaFigs);
					TY_ReportDS_fldInfo fldInfo = reportDS_fldInfo.stream().filter(x -> x.getRefAttrName().equals(attrContainer.getAttrName()))
					          .findFirst().get();
					/**
					 * Compute Averages - Only compute if more than 1 row
					 */
					if (fldInfo != null)
					{
						if (fldInfo.getAttribMdt().isAvgON())
						{
							attrContainer.setAvg(
							          attrContainer.getKeyFigures().stream().mapToDouble(TY_KeyFigure::getFigure).average().getAsDouble());
						}
						if (deltaFigs.size() > 1)
						{

							if (fldInfo.getAttribMdt().isAvgdeltaON())
							{
								attrContainer.setDeltaAvg(attrContainer.getDeltaFigures().stream().mapToDouble(TY_DeltaFigure::getFigure)
								          .average().getAsDouble());
							}
						}
						else
						{
							attrContainer.setDeltaAvg(attrContainer.getDeltaFigures().get(0).getFigure());

						}
					}

				}
			}
		}

	}

	/**
	 * -----------------------------------------------------------------------------------
	 * Process Alerts
	 * 
	 * @param scRoot_attrContainers
	 * @param repfldsInfoList
	 *             ----------------------------------------------------------------------------
	 * @throws EX_General
	 */
	private void processAlerts(TY_ScRoot_AttrContainers scRoot_attrContainers, ArrayList<TY_ReportDS_fldInfo> repfldsInfoList) throws EX_General
	{
		for ( TY_Attr_Container attrContainer : scRoot_attrContainers.getAttrContainers() )
		{
			if (attrContainer.getDeltaFigures() != null)
			{
				if (attrContainer.getDeltaFigures().size() > 0)
				{
					TY_ReportDS_fldInfo fldInfo = repfldsInfoList.stream().filter(x -> x.getRefAttrName().equals(attrContainer.getAttrName()))
					          .findFirst().get();
					if (fldInfo != null)
					{
						if (fldInfo.getAlertsList().size() > 0)
						{
							IGenericDeltaAlerts genDeltaAlertSrv = appCtxt.getBean(IGenericDeltaAlerts.class);
							if (genDeltaAlertSrv != null)
							{
								ArrayList<TY_Alert> alerts = genDeltaAlertSrv.getAlertsforAttrContainer(attrContainer,
								          fldInfo.getAlertsList());
								if (alerts != null)
								{
									attrContainer.getAlerts().addAll(alerts);
								}
							}
						}
					}
				}
			}
		}
	}

	private void processLongTermTrends(TY_ScRoot_AttrContainers scRoot_attrContainers, ArrayList<TY_ReportDS_fldInfo> repfldsInfoList)
	{
		for ( TY_Attr_Container attrContainer : scRoot_attrContainers.getAttrContainers() )
		{
			if (attrContainer.getDeltaFigures().size() >= 2)
			{
				TY_ReportDS_fldInfo fldInfo = repfldsInfoList.stream().filter(x -> x.getRefAttrName().equals(attrContainer.getAttrName()))
				          .findFirst().get();
				if (fldInfo != null)
				{
					boolean alert = true;
					int size = attrContainer.getDeltaFigures().size();
					for ( int x = (size - 1); x > 0; x-- )
					{
						double figcurr = attrContainer.getDeltaFigures().get(x).getFigure();
						double figpen = attrContainer.getDeltaFigures().get(x - 1).getFigure();

						if (fldInfo.getAttribMdt().isLongtermIncON()) // Should be Increasing Trend
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
						if (fldInfo.getAttribMdt().isLongtermIncON())
						{
							GeneralMessage msgChgErr = new GeneralMessage("AL_LTT_INC", new Object[]
							{ attrContainer.getAttrLabel()
							});
							attrContainer.getAlerts()
							          .add(new TY_Alert(SCEenums.alertMode.FAVOR, msgFormatter.generate_message_snippet(msgChgErr)));
						}
						else
						{
							GeneralMessage msgChgErr = new GeneralMessage("AL_LTT_DEC", new Object[]
							{ attrContainer.getAttrLabel()
							});
							attrContainer.getAlerts()
							          .add(new TY_Alert(SCEenums.alertMode.FAVOR, msgFormatter.generate_message_snippet(msgChgErr)));
						}
					}
				}
			}
		}

	}

	/********************************************************************************************************************
	 **************************************** POINTCUT DEFINITIONS SECTION ********************************************
	 ********************************************************************************************************************/

	/**
	 * Pointcut for Implementing Interface 'IReportDataSource'
	 */
	@Pointcut("target(scriptsengine.reportsengine.repDS.interfaces.IReportDataSource)")
	public void ReportDataSourceInterface()
	{

	}

	/**
	 * PointCut for generateReportDataSourceforScripCode
	 */
	@Pointcut("execution( public * *.generateReportDataSourceforScripCode(..))")
	public void generateDataSource()
	{

	}

}
