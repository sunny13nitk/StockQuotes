package root.busslogic.Services.interfaces;

import java.util.List;

import root.busslogic.SQLProc.entities.Rep_FL;
import root.busslogic.entity.FundLine;
import root.busslogic.entity.FundLineItem;
import root.user.model.User;

public interface IFundLineSrv
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
	
	public FundLine getFundLinebyId(
	        int flid
	);
	
	public void addFundLineforUser(
	        FundLine newfl, User user
	) throws Exception;
	
	public void addFundLineItem(
	        int flid, FundLineItem flItem
	) throws Exception;
	
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
