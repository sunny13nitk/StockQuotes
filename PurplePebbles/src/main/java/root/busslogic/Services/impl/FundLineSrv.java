package root.busslogic.Services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.busslogic.DAO.interfaces.IFundLineDAO;
import root.busslogic.SQLProc.entities.Rep_FL;
import root.busslogic.Services.interfaces.IFundLineSrv;
import root.busslogic.annotations.FundLineBalance;
import root.busslogic.entity.FundLine;
import root.busslogic.entity.FundLineItem;
import root.enums.FundLineOrigins;
import root.user.model.User;

@Service
public class FundLineSrv implements IFundLineSrv
{
	@Autowired
	private IFundLineDAO fundLineDAO;
	
	@Override
	@Transactional
	public List<FundLine> getFundLinesforUser(
	        User user
	)
	{
		
		return fundLineDAO.getFundLinesforUser(user);
	}
	
	@Override
	@Transactional
	public int deleteFundLine(
	        int pfID
	)
	{
		return fundLineDAO.deleteFundLine(pfID);
	}
	
	@Override
	@Transactional
	public int updateFundLine(
	        FundLine updFL
	)
	{
		return fundLineDAO.updateFundLine(updFL);
	}
	
	@Override
	@Transactional
	public void addFundLineforUser(
	        FundLine newfl, User user
	) throws Exception
	{
		fundLineDAO.addFundLineforUser(newfl, user);
		
	}
	
	@Override
	@Transactional
	public FundLine getFundLinebyId(
	        int flid
	)
	{
		
		return fundLineDAO.getFundLinebyId(flid);
	}
	
	@Override
	@Transactional
	/**
	 * Aspect Woven Around this Annotation for - Pre : -ve balance checks and Ready with After proceed updated Balance
	 * at Fund line level - Post : Update the FL item with updated balance Adding Exception to get the Exception
	 * Triggered from Aspect back in the Call
	 */
	@FundLineBalance(
	        origin = FundLineOrigins.FundLine
	)
	public void addFundLineItem(
	        int flid, FundLineItem flItem
	) throws Exception
	
	{
		
		/*
		 * To be handled in entirety via Aspect based processing
		 */
		
		// First Obtain Fundline to check Validity
		/*
		 * FundLine fl = fundLineDAO.getFundLinebyId(flid); if (fl != null) { if (fl.getFlItems() == null) {
		 * fl.setFlItems(new ArrayList<FundLineItem>()); } flItem.setFlid(flid); fl.getFlItems().add(flItem);
		 * fundLineDAO.updateFundLine(fl); }
		 */
	}
	
	@Override
	@Transactional
	public void updateFundLineBalance(
	        int flid, double Balance
	)
	{
		fundLineDAO.updateFundLineBalance(flid, Balance);
		
	}
	
	@Override
	@Transactional
	public void addFundLineItem(
	        FundLineItem flI
	)
	{
		fundLineDAO.addFundLineItem(flI);
		
	}
	
	@Override
	@Transactional
	public Rep_FL getFLDetailsbyflId(
	        int flId
	)
	{
		return fundLineDAO.getFLDetailsbyflId(flId);
	}
	
}
