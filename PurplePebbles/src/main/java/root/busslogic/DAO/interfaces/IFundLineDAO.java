package root.busslogic.DAO.interfaces;

import java.util.List;

import root.busslogic.SQLProc.entities.Rep_FL;
import root.busslogic.entity.FundLine;
import root.busslogic.entity.FundLineItem;
import root.user.model.User;

public interface IFundLineDAO
{
	public List<FundLine> getFundLinesforUser(
	        User user
	);
	
	public int deleteFundLine(
	        int pfID
	);
	
	public int updateFundLine(
	        FundLine updFL
	);
	
	public void addFundLineforUser(
	        FundLine newfl, User user
	) throws Exception;
	
	public FundLine getFundLinebyId(
	        int flid
	);
	
	public void updateFundLineBalance(
	        int flid, double Balance
	);
	
	public void addFundLineItem(
	        FundLineItem flI
	);
	
	public Rep_FL getFLDetailsbyflId(
	        int flId
	);
	
}
