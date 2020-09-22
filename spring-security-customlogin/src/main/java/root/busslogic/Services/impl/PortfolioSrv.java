package root.busslogic.Services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.busslogic.DAO.interfaces.IFundLineDAO;
import root.busslogic.DAO.interfaces.IPortfolioDAO;
import root.busslogic.SQLProc.entities.Rep_Holdings;
import root.busslogic.SQLProc.entities.Rep_PFSS;
import root.busslogic.Services.interfaces.IPortfolioSrv;
import root.busslogic.annotations.PFTransaction;
import root.busslogic.entity.FundLine;
import root.busslogic.entity.Holding;
import root.busslogic.entity.Portfolio;
import root.busslogic.entity.Trade;
import root.busslogic.session.interfaces.ISessionUser;
import root.enums.PFTransactionOrigin;
import root.user.model.User;

@Service
public class PortfolioSrv implements IPortfolioSrv
{
	@Autowired
	private IPortfolioDAO pfDAO;
	
	@Autowired
	private IFundLineDAO flDAO;
	
	@Autowired
	private ISessionUser sessUserSrv;
	
	@Override
	@Transactional
	public List<Portfolio> getPortfoliosforUser(
	        User user
	)
	{
		return pfDAO.getPortfoliosforUser(user);
	}
	
	@Override
	@Transactional
	public int deletePortfolio(
	        int pfID
	)
	{
		return pfDAO.deletePortfolio(pfID);
		
	}
	
	@Override
	@Transactional
	public void updatePortfolio(
	        Portfolio updPF
	) throws Exception
	{
		
		pfDAO.updatePortfolio(updPF);
		
	}
	
	@Override
	@Transactional
	public void addPortfolioforUser(
	        Portfolio newPF, User user
	) throws Exception
	{
		pfDAO.addPortfolioforUser(newPF, user);
		
	}
	
	@Override
	@Transactional
	/**
	 * ASsign Fund Line to Portfolio - One Way Action ONLY! Only for Once can a Fund Line be Assigned to a Given
	 * Portfolio
	 */
	public void assignFundLinetoPortfolio(
	        int pid, int flid
	) throws Exception
	{
		
		if (pid > 0 && flid > 0)
		{
			
			if (flDAO != null && pfDAO != null)
			{
				
				// Check if Fund Line Already assigned to the Portfolio
				if (pfDAO.getAssignedFundLine(pid) == null)
				{
					// No fund Line Assigned
					Portfolio pf = this.getPortfoliosforUser(sessUserSrv.getSessionUser()).stream()
					        .filter(x -> x.getPid() == pid).findFirst().get();
					if (pf != null)
					{
						FundLine fl = flDAO.getFundLinebyId(flid);
						if (fl != null)
						{
							pf.setFundLine(fl);
							this.updatePortfolio(pf);
							
						}
					} else
					{
						throw new Exception(
						        "Portfolio id - " + pid + " not Maintainable for User - "
						                + sessUserSrv.getSessionUser().getUserName());
						
					}
					
				} else
				{
					throw new Exception(
					        "Fund Line is already assigned to requested Portfolio! No Muliple Assignments Possible!");
				}
			}
		}
		
	}
	
	@Override
	@Transactional
	public FundLine getAssignedFundLine(
	        int pid
	) throws Exception
	{
		return pfDAO.getAssignedFundLine(pid);
	}
	
	@Override
	@Transactional
	public Portfolio getPorfolioById(
	        int pid
	) throws Exception
	{
		Portfolio pf = null;
		if (pid > 0 && sessUserSrv != null)
		{
			try
			{
				pf = this.getPortfoliosforUser(sessUserSrv.getSessionUser()).stream().filter(
				        x -> x.getPid() == pid).findFirst().get();
			} catch (Exception e)
			{
				throw new Exception(
				        "Invalid Portfolio with id - " + pid + " for User - " + sessUserSrv.getSessionUser()
				                + "requested!");
			}
			
		}
		
		return pf;
	}
	
	@Override
	@Transactional
	public Portfolio getPorfolioByIdDeep(
	        int pid
	) throws Exception
	{
		Portfolio pf = null;
		pf = this.getPorfolioById(pid);
		if (pf != null)
		{
			FundLine fl = this.getAssignedFundLine(pid);
			pf.setFundLine(fl);
		}
		
		return pf;
	}
	
	@Override
	@Transactional
	public void updatePortfolioHeader(
	        Portfolio updPF
	)
	{
		this.pfDAO.updatePortfolioHeader(updPF);
		
	}
	
	@Override
	@Transactional
	public List<Holding> getHoldings(
	        int pid
	)
	{
		return pfDAO.getHoldings(pid);
	}
	
	@Override
	@Transactional
	public Holding getHoldingforScrip(
	        int pid, String scCode
	)
	{
		return pfDAO.getHoldingforScrip(pid, scCode);
	}
	
	@Override
	@Transactional
	/**
	 * Aspect Woven Around this Annotation for central Transaction Processing for Portfolio
	 * 
	 */
	@PFTransaction(
	        origin = PFTransactionOrigin.Buy_Sell
	)
	public void tradeforPF(
	        int pid, Trade tradeItem
	) throws Exception
	{
		// Will be Handled by Aspect in Entirety
		
	}
	
	@Override
	@Transactional
	public void createHolding(
	        Holding hlI
	)
	{
		pfDAO.createHolding(hlI);
		
	}
	
	@Override
	@Transactional
	public void updateHolding(
	        Holding hlI
	)
	{
		pfDAO.updateHolding(hlI);
		
	}
	
	@Override
	@Transactional
	public void createTrade(
	        Trade tradeItem
	)
	{
		pfDAO.createTrade(tradeItem);
		
	}
	
	@Override
	@Transactional
	public List<Rep_Holdings> getHoldingsReport(
	        int pid
	)
	{
		return pfDAO.getHoldingsReport(pid);
	}
	
	@Override
	@Transactional
	public Rep_PFSS getPfStats(
	        int pid
	)
	{
		return pfDAO.getPfStats(pid);
	}
	
}
