package scriptsengine.portfolio.services.implementations;

import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import modelframework.interfaces.IQueryService;
import modelframework.managers.QueryManager;
import scriptsengine.dividends.interfaces.IDividendSrv;
import scriptsengine.enums.SCEenums.scripSellMode;
import scriptsengine.portfolio.definitions.TY_DematData;
import scriptsengine.portfolio.definitions.TY_PosSS;
import scriptsengine.portfolio.definitions.TY_ScripBuy;
import scriptsengine.portfolio.definitions.TY_ScripBuySummary;
import scriptsengine.portfolio.definitions.TY_Scrip_PositionModel;
import scriptsengine.portfolio.pojos.OB_Positions_Header;
import scriptsengine.portfolio.services.interfaces.IPF_ScripSellSrv;
import scriptsengine.portfolio.services.interfaces.IPortfolioManager;
import scriptsengine.portfolio.services.interfaces.IScripBuyService;
import scriptsengine.portfolio.services.interfaces.IScripPositionsDB;
import scriptsengine.simulations.sell.definitions.TY_Sell_Proposal;
import scriptsengine.simulations.sell.definitions.TY_Sell_Quote;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.interfaces.IScripExists;
import scriptsengine.valuehelpers.definitions.TY_StringKeyDesc;
import scriptsengine.valuehelpers.interfaces.IValueHelpScrips;

/**
 * 
 * Session Scoped BEan for Portfolio Management
 */

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PortfolioManager implements ApplicationContextAware, IPortfolioManager
{
	/**
	 * -----------------------------------------------
	 * PLEASE HOOK US UP - AUTOWIRING
	 * ----------------------------------------------
	 */

	@Autowired
	private MessagesFormatter				msgFormatter;

	@Autowired
	private IScripExists					scEXSrv;

	@Autowired
	private IValueHelpScrips					valHlpScSrv;

	@Autowired
	private IScripPositionsDB				scPosDBSrv;

	/**
	 * -----------------------------------------------
	 * PLEASE HOOK US UP - AUTOWIRING
	 * ----------------------------------------------
	 */

	private ArrayList<TY_DematData>			myDematACs;

	private ArrayList<TY_Scrip_PositionModel>	ScripsPositions;

	private ApplicationContext				appCtxt;

	private final String					posRootObjName	= "OB_Positions_Header";

	/**
	 * -----------------------------------------------
	 * GETTERS & SETTERS
	 * ----------------------------------------------
	 */

	/**
	 * @return the myDematACs
	 */
	@Override
	public ArrayList<TY_DematData> getMyDematACs() throws EX_General
	{
		if (myDematACs == null)
		{
			loadMyPortfolio();
		}
		return myDematACs;
	}

	/**
	 * @return the scripPositions
	 */
	@Override
	public ArrayList<TY_Scrip_PositionModel> getScripPositions()
	{
		return ScripsPositions;
	}

	/**
	 * @param scripPositions
	 *             the scripPositions to set
	 */
	public void setScripPositions(ArrayList<TY_Scrip_PositionModel> scripPositions)
	{
		ScripsPositions = scripPositions;
	}

	/**
	 * -----------------------------------------------
	 * INTERFACES IMPLEMENTATIONS
	 * ----------------------------------------------
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
	 * -------------------------------------------------------------------------------------------------------
	 * Load My Portfolio for the logged in User
	 * To Be Implemented when the dematACs is initial - For Now will be initialized through Xml in scebeans.xml
	 * -------------------------------------------------------------------------------------------------------
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void loadMyPortfolio() throws EX_General
	{
		if (this.myDematACs == null)
		{
			// Logic to be Inserted for Quering through Accounts of type "Demat" and status "Active" or for filtering
			// thorugh Already User Populated accouunts through Account Service for "active" "Demat" Accounts
			/**
			 * Placeholder for Logic once Account Integration is done
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 */

			/**
			 * This portion of logic to be removed Later - also delete bean myDemats from scebeans.xml
			 */

			this.myDematACs = (ArrayList<TY_DematData>) appCtxt.getBean("myDematsTest");

		}
		/**
		 * This portion of logic to be removed Later
		 */

		/**
		 * Load My Positions after my Demats have been loaded - querying through Dbase
		 */
		try
		{
			loadMyPositions();
		}
		catch (Exception e)
		{
			EX_General egen = new EX_General("ERR_LOAD_POSITIONS", new Object[]
			{ e.getMessage()
			});
			throw egen;
		}

	}

	/**
	 * --------------------------------------------------------------------------
	 * Load My Positions after my Demats have been loaded - querying through Dbase
	 * Populate Scrip Positions in Session Bean
	 * -------------------------------------------------------------------------
	 * 
	 * @throws Exception
	 * 
	 */
	private void loadMyPositions() throws Exception
	{
		IQueryService qs = (IQueryService) QueryManager.getQuerybyRootObjname(posRootObjName);
		if (qs != null)
		{
			ArrayList<OB_Positions_Header> positionsH = qs.executeQuery();
			if (positionsH != null)
			{
				if (positionsH.size() > 0)
				{
					// Create Positions Model
					this.ScripsPositions = new ArrayList<TY_Scrip_PositionModel>();

					// Loop through Positions Header to Get Positons Items and prepare the Scrip Position Model
					TY_Scrip_PositionModel scPosModel = null;
					for ( OB_Positions_Header posH : positionsH )
					{
						// Initialize new Scrip Position
						scPosModel = new TY_Scrip_PositionModel();
						scPosModel.setScPosHeader(posH);

						scPosModel.setScPosItems(posH.getRelatedEntities("OB_Positions_Items_Rel"));
						// Add to Current Portflio Positions
						this.ScripsPositions.add(scPosModel);
					}

				}
				else
				{
					// Position Header is Null. No Positions yet created for User
					// Initialize a blank position
					this.ScripsPositions = new ArrayList<TY_Scrip_PositionModel>();

				}

			}

		}
	}

	/**
	 * -------------------------Get positions model for a Particular Scrip
	 * 
	 * @param scCode
	 *             - Scrip Code
	 * @return - Scrip Positions Model
	 * @throws EX_General
	 *              ----------------------------------------------------------------
	 */
	@Override
	public TY_Scrip_PositionModel getPositionsModelforScrip(String scCode) throws EX_General
	{
		TY_Scrip_PositionModel posModel = null;

		if (this.ScripsPositions != null)
		{
			if (this.ScripsPositions.size() > 0)
			{
				try
				{
					posModel = this.ScripsPositions.stream().filter(x -> x.getScPosHeader().getScCode().equals(scCode)).findFirst().get();
				}
				catch (NoSuchElementException e)
				{
					return null;
				}
			}
		}

		return posModel;
	}

	/**
	 * -------------------------Get positions header for a Particular Scrip
	 * 
	 * @param scCode
	 *             - Scrip Code
	 * @return - Scrip Positions Header
	 * @throws EX_General
	 *              ----------------------------------------------------------------
	 */

	@Override
	public OB_Positions_Header getPositionsHeaderforScrip(String scCode) throws EX_General
	{
		OB_Positions_Header scPosHead = null;

		if (this.ScripsPositions != null)
		{
			if (this.ScripsPositions.size() > 0)
			{
				try
				{
					scPosHead = this.ScripsPositions.stream().filter(x -> x.getScPosHeader().getScCode().equals(scCode)).findFirst().get()
					          .getScPosHeader();
				}
				catch (NoSuchElementException e)
				{
					return null;
				}
			}
		}

		return scPosHead;
	}

	/**
	 * --------------------- Purchase Scrip ---------------------------------------
	 * 
	 * @param scripPurchaseDetails
	 *             - Scrip Purchase Container - Invoke Scrip buy Service Prototype Bean
	 * @throws EX_General
	 *              - Exception
	 *              ---------------------------------------------------------------------
	 */
	@Override
	public TY_ScripBuySummary buyScrip(TY_ScripBuy scripPurchaseDetails, Boolean simulate) throws EX_General
	{
		TY_ScripBuySummary buySummary = new TY_ScripBuySummary();

		if (scripPurchaseDetails != null && appCtxt != null)
		{
			IScripBuyService scBuySrv = appCtxt.getBean(IScripBuyService.class);
			if (scBuySrv != null)
			{
				// Handles FI Aspect of Scrip Purchase - via ScripBuyFIAspect
				scBuySrv.prePurchaseProcessTrigger(scripPurchaseDetails);

				// Execute the Purchase - In case Insuff Balance Exception Not Triggered from Pre Process
				buySummary = scBuySrv.buyScrip(scripPurchaseDetails, simulate);

				// Handles FI Aspect of Scrip Purchase - via ScripBuyFIAspect
				scBuySrv.postPurchaseProcessTrigger();
			}
		}

		return buySummary;

	}

	/**
	 * ------------------------------------------------------------------
	 * Validate DEmat Account from Current Demat AC for the User--------------
	 * 
	 * @param dematAC
	 *             - demat AC to Validate
	 * @return - true if Account is valid for the User, else False
	 *         ----------------------------------------------------------------------
	 */
	@Override
	public boolean validateDematAC(String dematAC)
	{
		Boolean isValid = false;
		if (dematAC != null)
		{
			if (this.myDematACs != null)
			{
				if (this.myDematACs.size() > 0)
				{
					try
					{
						if (this.myDematACs.stream().filter(x -> x.getAcNum().equalsIgnoreCase(dematAC)).findFirst().get() != null)

						{
							isValid = true;
						}
					}

					catch (NoSuchElementException e)
					{
						isValid = false;
					}
				}
			}

		}

		return isValid;
	}

	/**
	 * -------------------------------------------------------------------------
	 * SEll Scrip by Sell Quote and User Requested Sell Mode - P&L /Free Shares
	 * ------------------------------------------------------------------------
	 * 
	 * @throws Exception
	 */
	@Override
	public TY_Sell_Proposal sellScrip(TY_Sell_Quote sellQuote, scripSellMode sellMode) throws Exception
	{
		TY_Sell_Proposal proposal = null;
		if (sellQuote != null && appCtxt != null)
		{
			IPF_ScripSellSrv scripSellSrv = appCtxt.getBean(IPF_ScripSellSrv.class);
			if (scripSellSrv != null)
			{
				scripSellSrv.preSellProcessTrigger(sellQuote);
				scripSellSrv.executeScripSell(sellMode);
				scripSellSrv.postSellProcessTrigger();

				// Get Proposal Back
				proposal = scripSellSrv.getSellProposal();

				// Adjust Portfolio for sold Scrip - Drop entries of positions from buffer for the scrip in ques and
				// reload from DB again to buffer - Create a single method in PFM for the same that invokes a
				// separate service for same
				adjustScripPositionModelforScrip(proposal.getScTxnSummary().getScCode());

			}

		}
		return proposal;

	}

	@Override
	public void sellScrip(TY_Sell_Proposal proposal, scripSellMode sellMode) throws Exception
	{
		if (proposal != null && appCtxt != null)
		{
			IPF_ScripSellSrv scripSellSrv = appCtxt.getBean(IPF_ScripSellSrv.class);
			if (scripSellSrv != null)
			{
				scripSellSrv.preSellProcessTrigger(proposal);
				scripSellSrv.executeScripSell(sellMode);
				scripSellSrv.postSellProcessTrigger();

				// Adjust Portfolio for sold Scrip - Drop entries of positions from buffer for the scrip in ques and
				// reload from DB again to buffer - Create a single method in PFM for the same that invokes a
				// separate service for same
				adjustScripPositionModelforScrip(proposal.getScTxnSummary().getScCode());

			}
		}

	}

	/******************************
	 * PRIVATE SECTION
	 *****************************/

	private void adjustScripPositionModelforScrip(String scCode) throws EX_General
	{
		if (scCode != null && scPosDBSrv != null)
		{
			TY_Scrip_PositionModel scPosModel = scPosDBSrv.getPosModelforScCode(scCode);
			if (scPosModel != null)
			{
				this.ScripsPositions.removeIf(x -> x.getScPosHeader().getScCode().equals(scCode));
				this.ScripsPositions.add(scPosModel);
			}
		}
	}

	/**
	 * -------------------------------------------------------------
	 * Get Total Portfolio Value - Current Investments
	 * 
	 * @throws Exception
	 *              -------------------------------------------------------------
	 */
	@Override
	public double getTotalPortfolioValue() throws Exception
	{
		double val = 0;

		if (this.ScripsPositions == null)
		{
			loadMyPositions();
		}

		if (this.ScripsPositions != null)
		{
			if (this.ScripsPositions.size() > 0)
			{
				val = this.ScripsPositions.stream().mapToDouble(x -> x.getScPosHeader().getCurrInvestment()).sum();
			}
		}

		return val;
	}

	/**
	 * Get Percentage Contribution to Protfolio for a Scrip by Scrip Code
	 */
	@Override
	public double getScripContributiontoPortfolio(String scCode) throws Exception
	{
		double scCont = 0;
		if (scCode != null)
		{
			OB_Positions_Header posH = this.getPositionsHeaderforScrip(scCode);
			if (posH != null)
			{
				double total = this.getTotalPortfolioValue();
				if (total != 0)
					scCont = (posH.getCurrInvestment() / total) * 100;
			}

		}

		return scCont;
	}

	/**
	 * Get Sector contribution for Current Scrip's Sector
	 * 
	 * @param scCode
	 * @return
	 * @throws Exception
	 */
	@Override
	public double getSectorContributiontoPortfoliobyScCode(String scCode) throws Exception
	{
		double secCont = 0;

		// First Get Sector for Scrip
		if (valHlpScSrv != null && scCode != null)
		{
			// Get all Sectors and Scrip Codes List
			ArrayList<TY_StringKeyDesc> scSectors = valHlpScSrv.getScripCodesSectors();
			if (scSectors != null)
			{
				String currSector = scSectors.stream().filter(x -> x.getKey().equals(scCode)).findFirst().get().getDesc();
				if (currSector != null)
				{
					secCont = this.getSectorContributiontoPortfolio(currSector);
				}
			}
		}

		return secCont;
	}

	/**
	 * Get Sector Contribution to Portfolio by Sector
	 * 
	 * @param sector
	 * @return
	 * @throws Exception
	 */
	@Override
	public double getSectorContributiontoPortfolio(String sector) throws Exception
	{
		double secCont = 0;

		if (valHlpScSrv != null && sector != null)
		{
			// Get Scrips for the Sector
			ArrayList<TY_StringKeyDesc> scSectors = valHlpScSrv.getScripsforSector(sector);
			if (scSectors != null)
			{
				// From these total the Current Investments as per current Positions in Portfolio
				for ( TY_StringKeyDesc scSector : scSectors )
				{
					for ( TY_Scrip_PositionModel currPosition : this.ScripsPositions )
					{
						if (currPosition.getScPosHeader().getScCode().equals(scSector.getKey()))
						{
							secCont += currPosition.getScPosHeader().getCurrInvestment();
						}
					}
				}

				double total = this.getTotalPortfolioValue();
				if (total != 0)
					secCont = (secCont / total) * 100;

			}
		}
		return secCont;
	}

	/**
	 * ----------------------------------------------------------------------------------------------------
	 * Get Portfolio Contribution for an Amount as percentage of Total Portfolio Value - Useful in Simulations
	 * 
	 * @param Amount
	 * @return
	 * @throws Exception
	 */
	@Override
	public double getContributiontoPortfoliobyAmount(double Amount) throws Exception
	{
		double val = 0;
		if (Amount > 0)
		{
			double total = this.getTotalPortfolioValue();
			if (total != 0)
				val = (Amount / total) * 100;

		}
		return val;
	}

	/**
	 * -----------------------------------------------------------------------------------------
	 * REturn Position snapshot for Scrip Including Scrip and Scrip's Sector Contribution to Portfolio
	 * 
	 * @param scCode
	 *             - Scrip Code
	 * @return - TY_PoSSS - Position Snapshot Instance
	 *         ----------------------------------------------------------------------------------------
	 */
	@Override
	public TY_PosSS getScripPosSnapshot(String scCode) throws Exception
	{
		TY_PosSS posSS = null;
		if (scCode != null)
		{
			OB_Positions_Header posH = this.getPositionsHeaderforScrip(scCode);
			if (posH != null)
			{
				posSS = new TY_PosSS();
				posSS.setScCode(scCode);
				posSS.setHoldings(posH.getCurrHolding());
				posSS.setFreeShares(posH.getFreeHolding());
				posSS.setInvestments(posH.getCurrInvestment());
				posSS.setPpu(posH.getPPU());
				posSS.setScPFContr(this.getScripContributiontoPortfolio(scCode));
				posSS.setSecPFContr(this.getSectorContributiontoPortfolio(scCode));

			}
		}

		return posSS;
	}

	/**
	 * ------------- Process Dividend for Scrip by Scrip Code --------------------
	 * 
	 * @param scCode
	 *             - Scrip Code
	 * @param date
	 *             - Dividend Announcement Date
	 * @param DPS
	 *             - Announced Dividend per Share
	 *             -----------------------------------------------------------------------
	 */
	@Override
	public void processDividendforScrip(String scCode, Date date, double DPS) throws EX_General
	{
		if (appCtxt != null)
		{
			IDividendSrv divSrv = appCtxt.getBean(IDividendSrv.class);
			if (divSrv != null)
			{
				divSrv.processDividendforScrip(scCode, date, DPS);
			}
		}

	}

	/**
	 * ------------- Process Dividend for Scrip by Scrip Description --------------------
	 * 
	 * @param scCode
	 *             - Scrip Code
	 * @param date
	 *             - Dividend Announcement Date
	 * @param DPS
	 *             - Announced Dividend per Share
	 *             -----------------------------------------------------------------------
	 */
	@Override
	public void processDividendforScripDesc(String scDesc, Date date, double DPS) throws EX_General, Exception
	{
		if (appCtxt != null)
		{
			IDividendSrv divSrv = appCtxt.getBean(IDividendSrv.class);
			if (divSrv != null)
			{
				divSrv.processDividendforScripDesc(scDesc, date, DPS);
			}
		}
	}

}
