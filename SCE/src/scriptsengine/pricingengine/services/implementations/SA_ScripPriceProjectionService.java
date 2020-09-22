package scriptsengine.pricingengine.services.implementations;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import scriptsengine.constants.SheetNamesConstants;
import scriptsengine.enums.SCEenums;
import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.pojos.OB_Scrip_Shareholding;
import scriptsengine.pricingengine.JAXB.definitions.PPFactorMetadata;
import scriptsengine.pricingengine.JAXB.interfaces.IPPConfigMetadata;
import scriptsengine.pricingengine.definitions.TY_AllowancesBean;
import scriptsengine.pricingengine.definitions.TY_AvgPE;
import scriptsengine.pricingengine.definitions.TY_Last_NP_FVR;
import scriptsengine.pricingengine.definitions.TY_NPSD;
import scriptsengine.pricingengine.definitions.TY_PFactor;
import scriptsengine.pricingengine.definitions.TY_PFactorDetail;
import scriptsengine.pricingengine.definitions.TY_PricesReturn;
import scriptsengine.pricingengine.definitions.TY_RawMPP_Stats;
import scriptsengine.pricingengine.priceStratergies.services.interfaces.IPPS_DefaultStratergy;
import scriptsengine.pricingengine.priceStratergies.services.interfaces.IRawMaterial_PPStatistics;
import scriptsengine.pricingengine.reports.definitions.TY_ReportPPS_Default;
import scriptsengine.pricingengine.reports.interfaces.IDefaultPriceStratergyStats;
import scriptsengine.pricingengine.services.interfaces.ISA_AvgPEService;
import scriptsengine.pricingengine.services.interfaces.ISA_ENPRService;
import scriptsengine.pricingengine.services.interfaces.ISA_LastNettProfit_FVR_Service;
import scriptsengine.pricingengine.services.interfaces.ISA_NPDService;
import scriptsengine.pricingengine.services.interfaces.ISA_ScripPriceProjectionService;
import scriptsengine.reportsengine.interfaces.IXLSReportAware;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripSheetMetadata;
import scriptsengine.uploadengine.validations.interfaces.IScripExists;

/**
 * Scrip Price Projection Service Prototype Scoped Bean
 *
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SA_ScripPriceProjectionService implements ISA_ScripPriceProjectionService, IXLSReportAware, ApplicationContextAware
{

	/**
	 * -------------------------- AutoWired Scrip Analytics and General Beans ---
	 */
	@Autowired
	private static MessagesFormatter		msgFormatter;
	@Autowired
	private IScripExists				scExSrv;
	@Autowired
	private IPPConfigMetadata			ppMdtSrv;
	@Autowired
	private ISA_ENPRService				enprSrv;
	@Autowired
	private ISA_AvgPEService				avgPESrv;
	@Autowired
	private ISA_NPDService				npdSrv;
	@Autowired
	private ISA_LastNettProfit_FVR_Service	fvrSrv;
	@Autowired
	private TY_AllowancesBean			allowancesBean;
	@Autowired
	private IScripSheetMetadata			shMdtSrv;
	/**
	 * --------------------------- AutoWired Scrip Analytics and General Beans -----
	 */

	/**
	 * ------------------------------------- SA Services data placeholders -----------
	 */
	private String						scCode;
	private OB_Scrip_General				scRoot;
	private double						avgENPR;
	private TY_AvgPE					avgPE;
	private TY_Last_NP_FVR				lastNp_FVR;
	private TY_NPSD					npd;
	private ArrayList<TY_RawMPP_Stats>		rawMstats;
	private TY_ReportPPS_Default			defPstats;
	private double						promoterHolding;
	private double						DMA_200;

	/**
	 * ------------------------------------- SA Services data placeholders ---------
	 */

	/**
	 * ------------------------------------- Price Projections Return Structure ---------
	 */

	private ArrayList<TY_PricesReturn>		projectedPrices;

	/**
	 * ------------------------------------- Price Projections Return Structure ---------
	 */

	/**
	 * ------------------------------------- Factors that can influence Price Projections ------
	 */
	private ArrayList<TY_PFactor>			projectionFactors;
	/**
	 * ------------------------------------- Factors that can influence Price Projections ----
	 */

	private ApplicationContext			appCtxt;

	// -------------------------------------------------------- Getter and Setters ------------------------------------

	/**
	 * @return the scCode
	 */
	@Override
	public String getScCode()
	{
		return scCode;
	}

	/**
	 * @return the promoterHolding
	 */
	@Override
	public double getPromoterHolding()
	{
		return promoterHolding;
	}

	/**
	 * @param promoterHolding
	 *             the promoterHolding to set
	 */
	public void setPromoterHolding(double promoterHolding)
	{
		this.promoterHolding = promoterHolding;
	}

	/**
	 * @return the dMA_200
	 */
	@Override
	public double getDMA_200()
	{
		return DMA_200;
	}

	/**
	 * @param dMA_200
	 *             the dMA_200 to set
	 */
	public void setDMA_200(double dMA_200)
	{
		DMA_200 = dMA_200;
	}

	/**
	 * @param scCode
	 *             the scCode to set
	 */
	public void setScCode(String scCode)
	{
		this.scCode = scCode;
	}

	/**
	 * @return the rawMstats
	 */
	@Override
	public ArrayList<TY_RawMPP_Stats> getRawMstats()
	{
		return rawMstats;
	}

	/**
	 * @return the avgENPR
	 */
	@Override
	public double getAvgENPR()
	{
		return avgENPR;
	}

	/**
	 * @param avgENPR
	 *             the avgENPR to set
	 */
	public void setAvgENPR(double avgENPR)
	{
		this.avgENPR = avgENPR;
	}

	/**
	 * @return the avgPE
	 */
	@Override
	public TY_AvgPE getAvgPE()
	{
		return avgPE;
	}

	/**
	 * @param avgPE
	 *             the avgPE to set
	 */
	public void setAvgPE(TY_AvgPE avgPE)
	{
		this.avgPE = avgPE;
	}

	/**
	 * @return the lastNp_FVR
	 */
	@Override
	public TY_Last_NP_FVR getLastNp_FVR()
	{
		return lastNp_FVR;
	}

	/**
	 * @param lastNp_FVR
	 *             the lastNp_FVR to set
	 */
	public void setLastNp_FVR(TY_Last_NP_FVR lastNp_FVR)
	{
		this.lastNp_FVR = lastNp_FVR;
	}

	/**
	 * @return the npd
	 */
	@Override
	public TY_NPSD getNpd()
	{
		return npd;
	}

	/**
	 * @param npd
	 *             the npd to set
	 */
	public void setNpd(TY_NPSD npd)
	{
		this.npd = npd;
	}

	/**
	 * @param projectedPrices
	 *             the projectedPrices to set
	 */
	public void setProjectedPrices(ArrayList<TY_PricesReturn> projectedPrices)
	{
		this.projectedPrices = projectedPrices;
	}

	/**
	 * @param projectionFactors
	 *             the projectionFactors to set
	 */
	public void setProjectionFactors(ArrayList<TY_PFactor> projectionFactors)
	{
		this.projectionFactors = projectionFactors;
	}

	/**
	 * @return the projectionFactors
	 */
	@Override
	public ArrayList<TY_PFactor> getProjectionFactors()
	{
		return projectionFactors;
	}

	/**
	 * @return the projectedPrices
	 */
	@Override
	public ArrayList<TY_PricesReturn> getProjectedPrices()
	{
		return projectedPrices;
	}

	/**
	 * @return the defPstats - Populate the status if it is null & return
	 * @throws EX_General
	 */
	@Override
	public TY_ReportPPS_Default getDefPstats() throws EX_General
	{

		if (this.defPstats == null)
		{
			// Prepare the Default Pricing Stratergy Status
			this.defPstats = prepareDefaultPricingStats();
		}

		return defPstats;
	}

	// -------------------------------------------------------- Getters and Setters -----------------------------------

	/**
	 * -------------------------------------------------------------
	 * Get Price Projections for the Scrip by Scrip Code
	 * 
	 * @param scCode
	 *             - Scrip Code
	 * @return - List of Price Projections
	 * @throws EX_General
	 *              - Exception
	 *              -------------------------------------------------
	 */
	@Override
	public ArrayList<TY_PricesReturn> getPriceProjectionsforScrip(String scCode) throws EX_General
	{

		if (scCode != null && enprSrv != null && avgPESrv != null && npdSrv != null && fvrSrv != null && allowancesBean != null)
		{
			// Loop through Price Projection Statergies Config
			if (this.ppMdtSrv.getPPMetadata() != null)
			{
				if (this.appCtxt != null)
				{
					if (this.scRoot == null)
					{
						try
						{
							this.scRoot = scExSrv.getScripRootbyCode(scCode);
						}
						catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException
						          | NoSuchMethodException | SecurityException | EX_InvalidObjectException | EX_NotRootException | SQLException
						          | EX_NullParamsException | EX_ParamCountMismatchException | EX_ParamInitializationException
						          | EX_InvalidRelationException e)
						{
							EX_General msgChgErr = new EX_General("ERR_PPSR_LOAD", new Object[]
							{ scCode, e.getMessage()
							});
							msgFormatter.generate_message_snippet(msgChgErr);
							throw msgChgErr;
						}
					}

					/**
					 * Populate Necessary Data from Scrip analytics SA Services
					 */

					populateSADatafromServices(scCode);
					/**
					 * Always Calculate the prices by Default Price Projection Stratergy
					 */
					// Get default Price Projection Stratergy bean
					String defPPS_BeanName = this.ppMdtSrv.getDefaultPricingBeanName();
					if (defPPS_BeanName != null)
					{
						try
						{
							IPPS_DefaultStratergy defPPS_Bean = (IPPS_DefaultStratergy) appCtxt.getBean(defPPS_BeanName);
							if (defPPS_Bean != null)
							{
								this.projectedPrices.add(defPPS_Bean.getPriceProjections(this));
							}
						}
						catch (Exception e)
						{
							EX_General msgChgErr = new EX_General("ERR_PPSRV_DEFBEANINST", new Object[]
							{ defPPS_BeanName
							});
							msgFormatter.generate_message_snippet(msgChgErr);
							throw msgChgErr;
						}

					}

					if (this.projectionFactors.size() > 0)
					{
						/**
						 * Validate Factor Type(s)
						 */
						validateFactors();

						/**
						 * Proceed for Rach Individual Factor Evaluation
						 */
						for ( TY_PFactor ppFactor : projectionFactors )
						{
							// Get default Price Projection Stratergy bean
							String pfPPS_BeanName = this.ppMdtSrv.getProjectionFactorBeanName(ppFactor.getFactorType());
							if (pfPPS_BeanName != null)
							{
								try
								{
									IPPS_DefaultStratergy pfPPS_Bean = (IPPS_DefaultStratergy) appCtxt.getBean(pfPPS_BeanName);
									if (pfPPS_Bean != null)
									{
										this.projectedPrices.add(pfPPS_Bean.getPriceProjections(this));
										/**
										 * Populate Raw Material Statistics for Analysis if needed for Further
										 * evaluation
										 */
										if (pfPPS_Bean instanceof IRawMaterial_PPStatistics)
										{
											this.rawMstats = ((IRawMaterial_PPStatistics) pfPPS_Bean).getStatisticsforRawMaterialSrv();
										}
									}
								}
								catch (Exception e)
								{
									EX_General msgChgErr = new EX_General("ERR_PPSRV_DEFBEANINST", new Object[]
									{ pfPPS_BeanName
									});
									msgFormatter.generate_message_snippet(msgChgErr);
									throw msgChgErr;
								}

							}
						}
					}

				}
			}
		}

		return projectedPrices;
	}

	@Override
	public void addFactorforPriceCalculation(TY_PFactor pFactor) throws EX_General
	{

		if (pFactor != null)
		{
			if (this.projectionFactors != null)
			{
				if (pFactor.getFactorType() != null && pFactor.getFactoritems().size() > 0)
				{
					for ( TY_PFactorDetail factorItem : pFactor.getFactoritems() )
					{
						if (factorItem.getDesc() != null && factorItem.getDirection() != SCEenums.direction.NONE
						          && factorItem.getPercentage() != 0)
						{
							// Do Nothing
						}
						else
						{

							EX_General msgChgErr = new EX_General("ERR_PPSRV_BADFACTORITEM", new Object[]
							{ pFactor.getFactorType()
							});
							msgFormatter.generate_message_snippet(msgChgErr);
							throw msgChgErr;

						}

					}

					this.projectionFactors.add(pFactor);
				}
			}
		}

	}

	@Override
	public void addFactorItemforPriceFactor(String factorType, TY_PFactorDetail factorItem) throws EX_General
	{
		if (factorType != null && factorItem != null)
		{
			/**
			 * Determine if Factor Type Exists for PPSrv
			 */
			try
			{
				TY_PFactor pFactor = this.projectionFactors.stream().filter(x -> x.getFactorType().equals(factorType)).findFirst().get();
				if (pFactor != null)
				{
					if (pFactor.getFactoritems() != null && factorItem.getDesc() != null && factorItem.getDirection() != SCEenums.direction.NONE
					          && factorItem.getPercentage() != 0)
					{
						pFactor.getFactoritems().add(factorItem);
					}
				}
			}
			catch (NoSuchElementException e)
			{
				EX_General msgChgErr = new EX_General("ERR_PPSRV_FACNOTFOUND", new Object[]
				{ factorType, this.getScCode()
				});
				msgFormatter.generate_message_snippet(msgChgErr);
				throw msgChgErr;

			}

		}

	}

	@Override
	public void setApplicationContext(ApplicationContext ctxt) throws BeansException
	{
		if (ctxt != null)
		{
			this.appCtxt = ctxt;
		}

	}

	public SA_ScripPriceProjectionService()
	{
		super();
		this.projectedPrices = new ArrayList<TY_PricesReturn>();
		this.projectionFactors = new ArrayList<TY_PFactor>();
		this.rawMstats = new ArrayList<TY_RawMPP_Stats>();
	}

	/**
	 * ---------------------------------------------------------------------
	 * Get Scrip Price Projections by Scrip Description
	 * 
	 * @param scripDescPattern
	 *             - Scrip Description from Start
	 * @return - Projected Pricesl List as per Configured Stratergies
	 * @throws EX_General
	 *              ---------------------------------------------------------------------
	 */

	@Override
	public ArrayList<TY_PricesReturn> getPriceProjectionsforScrip_byDescription(String scripDescPattern) throws EX_General
	{
		if (scripDescPattern != null)
		{
			if (scExSrv != null)
			{
				try
				{
					this.scRoot = scExSrv.getScripRootbyDescStartingwith(scripDescPattern);
					if (scRoot != null)
					{
						String scCode = scRoot.getscCode();
						if (scCode != null)
						{
							this.projectedPrices = (this.getPriceProjectionsforScrip(scCode));
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
		// TODO Auto-generated method stub
		return this.projectedPrices;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
	{
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	/**
	 * Generate XLS Report - No Implementation needed will be handled via an aspect
	 * Variable filepath wil also be passed with pjp
	 */
	@Override
	public void generateXLSReport(String filepath) throws EX_General
	{
		// Do nothing handled via an aspect
	}

	/**
	 * Generate XLS Report in specified WorkBook Context- No Implementation needed will be handled via an aspect
	 * Variable filepath wil also be passed with pjp
	 */

	@Override
	public void generateXLSReport(XSSFWorkbook wbCtxt) throws EX_General
	{
		// Do nothing handled via an aspect

	}

	/**
	 * ----------------------------------------------- PRIVATE SECTION ------------------------------
	 */

	private void populateSADatafromServices(String scCode) throws EX_General
	{
		try
		{
			this.scCode = scCode;
			this.avgENPR = enprSrv.getAvgENPRforScripCode(scCode);
			this.avgPE = avgPESrv.getPERatiosforScripCode(scCode);
			this.npd = npdSrv.getNettProfitDeltaforScripCode(scCode);
			this.lastNp_FVR = fvrSrv.getLastNettProfit_FVRforScripCode(scCode);
			this.DMA_200 = this.scRoot.getsc200DMA();
			if (shMdtSrv != null)
			{
				ArrayList<OB_Scrip_Shareholding> scSholdlist = scRoot
				          .getRelatedEntities(shMdtSrv.getRelationNameforSheetName(SheetNamesConstants.ShareHoldingSheet));
				if (scSholdlist != null)
				{
					if (scSholdlist.size() > 0)
					{
						this.promoterHolding = scSholdlist.get(0).getpromoter() - scSholdlist.get(0).getpledged();
					}
					else
					{
						this.promoterHolding = 0;
					}
				}
			}
		}
		catch (Exception e)
		{
			EX_General msgChgErr = new EX_General("ERR_PPSR_LOAD", new Object[]
			{ scCode, e.getMessage()
			});
			msgFormatter.generate_message_snippet(msgChgErr);
			throw msgChgErr;
		}
	}

	/**
	 * Validate Projection Factors
	 * 
	 */
	private void validateFactors() throws EX_General
	{

		/**
		 * First validate for Distinct Factors
		 */
		int numFactors = this.projectionFactors.size();

		int numDistinctFactors = this.projectionFactors.stream().filter(distinctByKey(x -> x.getFactorType())).collect(Collectors.toList()).size();

		if (numDistinctFactors != numFactors)
		{
			EX_General egen = new EX_General("ERR_PPSRV_FACTORSDISTINCT", new Object[]
			{ this.getScCode(), numFactors, numDistinctFactors
			});
			msgFormatter.generate_message_snippet(egen);
			throw egen;
		}

		/**
		 * Validate for factor Types - Should be as per Config
		 */
		for ( TY_PFactor pFactor : this.getProjectionFactors() )
		{
			try
			{
				PPFactorMetadata pFMdt = this.ppMdtSrv.getPPMetadata().getPricingFactorsConfig().stream()
				          .filter(x -> x.getPpFactor().equals(pFactor.getFactorType())).findFirst().get();
				if (pFMdt != null)
				{
					// Do Nothing
				}
			}
			catch (NoSuchElementException e)
			{
				EX_General egen = new EX_General("ERR_PPSRV_INVFACTOR", new Object[]
				{ pFactor.getFactorType(), this.getScCode()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;

			}

		}

	}

	/**
	 * ------------------------------------------------------------------------------------
	 * Prepare DEfault Pricing Stratergy Stats
	 * 
	 * @return - DEfault Stratergy Statistics
	 *         ------------------------------------------------------------------------------------
	 * @throws EX_General
	 */
	private TY_ReportPPS_Default prepareDefaultPricingStats() throws EX_General
	{
		TY_ReportPPS_Default defStat = new TY_ReportPPS_Default();
		if (appCtxt != null)
		{
			IDefaultPriceStratergyStats defStatBean = appCtxt.getBean(IDefaultPriceStratergyStats.class);
			if (defStatBean != null)
			{
				defStat = defStatBean.getDefaultPriceProjection_Stats(this);
			}
		}

		return defStat;
	}

}
