package root.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import root.busslogic.SQLProc.entities.Rep_Holdings;
import root.busslogic.SQLProc.entities.Rep_HoldingsTR;
import root.busslogic.SQLProc.entities.Rep_PFSS;
import root.busslogic.SQLProc.entities.Rep_PFSS_TR;
import root.busslogic.Services.interfaces.IBrokerTxnFeeSrv;
import root.busslogic.Services.interfaces.IConfigSrv;
import root.busslogic.Services.interfaces.IFundLineSrv;
import root.busslogic.Services.interfaces.IPortfolioSrv;
import root.busslogic.entity.Portfolio;
import root.busslogic.entity.Trade;
import root.busslogic.session.interfaces.ISessionUser;

@Controller
@RequestMapping(
    "/pf"
)
public class PFController
{
	@Autowired
	private ISessionUser sessUserSrv;
	
	@Autowired
	private IPortfolioSrv pfSrv;
	
	@Autowired
	private IFundLineSrv flSrv;
	
	@Autowired
	private IConfigSrv configSrv;
	
	@Autowired
	private IBrokerTxnFeeSrv brokerSrv;
	
	/*
	 * It is used in the form validation process. Here we add support to trim empty strings to null.
	 */
	@InitBinder
	public void initBinder(
	        WebDataBinder dataBinder
	)
	{
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	/*
	 * ----------------- GET MAPPINGS ----------------------
	 */
	@GetMapping(
	    "/main"
	)
	public String showPFHome(
	        Model model
	)
	{
		if (sessUserSrv != null)
		{
			model.addAttribute("pfList", pfSrv.getPortfoliosforUser(sessUserSrv.getSessionUser()));
			model.addAttribute("userName", sessUserSrv.getSessionUser().getUserName());
		}
		// return "pf-main";
		return "pf-mainM";
	}
	
	@GetMapping(
	    "/showAddTxn"
	)
	public String showAddTxn(
	        @RequestParam(
	            "pid"
	        ) int pid, Model model
	)
	{
		loadnewTradePF(pid, model);
		return "pf-buy-sell";
	}
	
	@GetMapping(
	    "/sellStockinPF"
	)
	public String showSellStockinPF(
	        @RequestParam(
	            "pid"
	        ) int pid, @RequestParam(
	            "scCode"
	        ) String scCode, Model model
	)
	{
		loadnewTradeBuySellPF(pid, scCode, model, 'S');
		return "pf-buy-sell";
	}
	
	@GetMapping(
	    "/buyStockinPF"
	)
	public String showBuyStockinPF(
	        @RequestParam(
	            "pid"
	        ) int pid, @RequestParam(
	            "scCode"
	        ) String scCode, Model model
	)
	{
		loadnewTradeBuySellPF(pid, scCode, model, 'B');
		return "pf-buy-sell";
	}
	
	@GetMapping(
	    "/createPF"
	)
	public String showCreatePF(
	        Model model
	)
	{
		// Set new PF - Will be tied to user using Session User Service on PF POST
		if (sessUserSrv != null)
		{
			model.addAttribute("newPF", new Portfolio());
			model.addAttribute("brokers", configSrv.getBrokers());
		}
		
		return "pf-create";
	}
	
	@GetMapping(
	    "/updatePF"
	)
	public String showUpdatePF(
	        @RequestParam(
	            "pid"
	        ) int pid, Model model
	)
	{
		// Set new PF - Will be tied to user using Session User Service on PF POST
		if (pfSrv != null && configSrv != null && pid > 0)
		{
			try
			{
				Portfolio pf = pfSrv.getPorfolioById(pid);
				model.addAttribute("PF", pf);
				model.addAttribute("brokers", configSrv.getBrokers());
			} catch (Exception e)
			{
				model.addAttribute("formError", e.getMessage());
				return "redirect:/pf/main";
			}
			
		}
		
		return "pf-update";
	}
	
	@GetMapping(
	    "/deletePF"
	)
	public String processdeletePF(
	        @RequestParam(
	            "pid"
	        ) int pid, Model model
	)
	{
		int numdel = pfSrv.deletePortfolio(pid);
		if (numdel > 0)
		{
			model.addAttribute("formSucc", numdel + " - Portfolio(s) Deleted Successfully!");
		}
		
		return "redirect:/pf/main";
	}
	
	@GetMapping(
	    "/PFdetailsShow"
	)
	public String showPFDetails(
	        @RequestParam(
	            "pid"
	        ) int pid, Model model
	)
	{
		// Populate PF in Model and navigate to PF details page
		if (pid > 0 && pfSrv != null)
		{
			try
			{
				loadPFDEtailsinModel(pid, model);
			}
			
			catch (javax.persistence.PersistenceException e)
			{
				return "pf-details-metro-new";
			} catch (Exception e)
			{
				model.addAttribute("formError", e.getMessage());
				return "redirect:/pf/main";
			}
			
		}
		return "pf-details-metro-new";
	}
	
	@GetMapping(
	    "/assgnFL"
	)
	public String showAssignFL(
	        @RequestParam(
	            "pid"
	        ) int pid, Model model
	)
	{
		// Populate PF in Model and navigate to PF details page
		if (pid > 0 && flSrv != null)
		{
			
			try
			{
				model.addAttribute("flList", flSrv.getFundLinesforUser(sessUserSrv.getSessionUser()));
				model.addAttribute("userName", sessUserSrv.getSessionUser().getUserName());
				model.addAttribute("pid", pid);
			} catch (Exception e)
			{
				model.addAttribute("formError", e.getMessage());
				return "redirect:/pf/main";
			}
			
		}
		return "fl-assign";
	}
	
	@GetMapping(
	    "/assignFLConfirm"
	)
	public String confirmAssignFL(
	        @RequestParam(
	            "flid"
	        ) int flid, @RequestParam(
	            "pid"
	        ) int pid, Model model
	)
	{
		
		if (flid > 0 && pid > 0 && pfSrv != null)
		{
			try
			{
				pfSrv.assignFundLinetoPortfolio(pid, flid);
				loadPFDEtailsinModel(pid, model);
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				model.addAttribute("formError", e.getMessage());
			}
		}
		
		return "pf-details-metro-new";
	}
	
	@GetMapping(
	    "/listHoldings"
	)
	public String showHoldings(
	        @RequestParam(
	            "pid"
	        ) int pid, Model model
	)
	{
		
		if (pid > 0 && pfSrv != null)
		{
			try
			{
				List<Rep_Holdings>        holdings   = pfSrv.getHoldingsReport(pid);
				ArrayList<Rep_HoldingsTR> holdingsTR = null;
				if (holdings != null)
				{
					if (holdings.size() > 0)
					{
						holdingsTR = new ArrayList<Rep_HoldingsTR>();
						for (Rep_Holdings holding : holdings)
						{
							holdingsTR.add(holding.transformToMoneyFormat());
						}
					}
				}
				model.addAttribute("holdings", holdingsTR);
				model.addAttribute("pid", pid);
			}
			
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				model.addAttribute("formError", e.getMessage());
			}
		}
		
		return "list-holdings";
	}
	
	@GetMapping(
	    "/PFposDonut"
	)
	public String showPosDonut(
	        @RequestParam int pid, Model model
	)
	{
		if (pid > 0 && pfSrv != null)
		{
			List<Rep_Holdings>        holdings   = pfSrv.getHoldingsReport(pid);
			ArrayList<Rep_HoldingsTR> holdingsTR = null;
			if (holdings != null)
			{
				if (holdings.size() > 0)
				{
					Map<String, Double> datapfwt  = new LinkedHashMap<String, Double>();
					Map<String, Double> datapfval = new LinkedHashMap<String, Double>();
					holdingsTR = new ArrayList<Rep_HoldingsTR>();
					
					for (Rep_Holdings holding : holdings)
					{
						String label = "'" + holding.getScCode() + "'";
						datapfwt.put(label, holding.getPerPF());
						datapfval.put(label, holding.getTotalInvestment());
						holdingsTR.add(holding.transformToMoneyFormat());
					}
					
					model.addAttribute("datapfwt", datapfwt);
					model.addAttribute("datapfval", datapfval);
					model.addAttribute("holdings", holdingsTR);
					model.addAttribute("pid", pid);
				}
			}
			
		}
		
		return "pos-donut";
		
	}
	
	/*
	 * ----------------- POST MAPPINGS ----------------------
	 */
	@PostMapping(
	    "/processCreatePF"
	)
	public String processCreatePF(
	        @Valid @ModelAttribute(
	            "newPF"
	        ) Portfolio newPf, BindingResult bRes, Model model
	)
	{
		
		// Validate Form for Errors- Found stay at form!
		if (bRes.hasErrors())
		{
			return "pf-create";
		} else
		{
			if (sessUserSrv != null)
			{
				// Create the PF for Session User
				try
				{
					pfSrv.addPortfolioforUser(newPf, sessUserSrv.getSessionUser());
					model.addAttribute("formSucc", "Portfolio Added Successfully!");
				} catch (Exception e)
				{
					// Populate Model with REgistration Error to display on Registration Form
					model.addAttribute("formError", e.getMessage());
					return "pf-create";
				}
			}
		}
		return "redirect:/pf/main";
	}
	
	@PostMapping(
	    "/processUpdatePF"
	)
	public String processUpdatePF(
	        @Valid @ModelAttribute(
	            "PF"
	        ) Portfolio Pf, BindingResult bRes, Model model
	)
	{
		
		// Validate Form for Errors- Found stay at form!
		if (bRes.hasErrors())
		{
			return "pf-update";
		} else
		{
			if (pfSrv != null)
			{
				// Update the Portfolio
				try
				{
					pfSrv.updatePortfolioHeader(Pf);
					model.addAttribute("formSucc", "Portfolio Updated Successfully!");
				} catch (Exception e)
				{
					
					model.addAttribute("formError", e.getMessage());
					return "pf-update";
				}
			}
		}
		return "redirect:/pf/main";
	}
	
	@PostMapping(
	    "/processPFTxn"
	)
	public String processPFTxn(
	        @Valid @ModelAttribute(
	            "newTxn"
	        ) Trade trade, BindingResult bRes, Model model
	)
	{
		
		// Validate Form for Errors- Found stay at form!
		if (bRes.hasErrors())
		{
			model.addAttribute("formError", bRes.getAllErrors());
			loadnewTradePF(trade.getPid(), model);
			return "pf-buy-sell";
		} else
		{
			if (pfSrv != null && trade != null)
			{
				// Update the Portfolio
				try
				{
					pfSrv.tradeforPF(trade.getPid(), trade);
					loadPFDEtailsinModel(trade.getPid(), model);
					
				} catch (Exception e)
				{
					
					model.addAttribute("formError", e.getMessage());
					loadnewTradePF(trade.getPid(), model);
					return "pf-buy-sell";
				}
			}
		}
		
		return "pf-details-metro-new";
		
	}
	
	/*
	 * ----------------- Private Methods ----------------------
	 */
	
	/*
	 * Load PF Details - Deep
	 */
	private void loadPFDEtailsinModel(
	        int pid, Model model
	) throws Exception
	{
		Portfolio pf = pfSrv.getPorfolioById(pid);
		if (pf != null)
		{
			model.addAttribute("pf", pf);
			Rep_PFSS    pfStatsO  = pfSrv.getPfStats(pid);
			Rep_PFSS_TR pfStatsTR = null;
			if (pfStatsO != null)
			{
				if (pfStatsO.getTotalTradeAmnt() > 0 && brokerSrv != null)
				{
					double txnCost = brokerSrv.getTxnCost(pfStatsO.getBroker(), pfStatsO.getTotalTradeAmnt());
					pfStatsO.setTxnCost(txnCost);
					pfStatsTR = pfStatsO.transformToMoneyFormat();
				}
				
				List<Rep_Holdings> top5H = pfSrv.getHoldingsReport(pid);
				if (top5H != null)
				{
					int hlSize = top5H.size();
					if (hlSize > 0)
					{
						if (hlSize < 5)
						{
							top5H = top5H.subList(0, hlSize);
						} else
						{
							top5H = top5H.subList(0, 5);
						}
					}
					
					model.addAttribute("top5H", top5H);
				}
				
				model.addAttribute("pfStats", pfStatsTR);
				
			}
			
		}
	}
	
	/*
	 * Load New Trade For PF
	 */
	
	private void loadnewTradePF(
	        int pid, Model model
	)
	{
		if (configSrv != null)
		{
			
			Trade newTxn = new Trade();
			newTxn.setPid(pid);
			newTxn.settCode('B');
			newTxn.setTxnDate(new Date()); // Default to Today's date
			model.addAttribute("newTxn", newTxn);
			model.addAttribute("scrips", configSrv.getScrips());
		}
	}
	
	/*
	 * Load New Trade For PF
	 */
	
	private void loadnewTradeBuySellPF(
	        int pid, String scCode, Model model, char mode
	)
	{
		if (configSrv != null)
		{
			
			Trade newTxn = new Trade();
			newTxn.setPid(pid);
			newTxn.settCode(mode);
			newTxn.setTxnDate(new Date()); // Default to Today's date
			newTxn.setScCode(scCode);
			model.addAttribute("newTxn", newTxn);
			model.addAttribute("scrips", configSrv.getScrips());
		}
	}
	
}
