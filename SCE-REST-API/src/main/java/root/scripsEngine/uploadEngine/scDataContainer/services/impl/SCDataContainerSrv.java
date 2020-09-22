package root.scripsEngine.uploadEngine.scDataContainer.services.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.scripsEngine.uploadEngine.exceptions.EX_General;
import root.scripsEngine.uploadEngine.scDataContainer.DAO.interfaces.ISCDataContainerDAO;
import root.scripsEngine.uploadEngine.scDataContainer.services.interfaces.ISCDataContainerSrv;
import root.scripsEngine.uploadEngine.scDataContainer.types.scDataContainer;

@Service
public class SCDataContainerSrv implements ISCDataContainerSrv
{
	@Autowired
	private ISCDataContainerDAO scDCDao;
	
	private scDataContainer scDC;
	
	@Override
	@Transactional
	public void load(
	        String scCode
	) throws EX_General, Exception
	{
		if (scDCDao != null)
		{
			this.scDC = scDCDao.load(scCode);
		}
	}
	
	@Override
	public <T> ArrayList<T> load(
	        String scCode, String bobjName
	)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public scDataContainer getScDC(
	)
	{
		// TODO Auto-generated method stub
		return this.scDC;
	}
	
	@Override
	public Method getMethodfromSCDataContainer(
	        String sheetName, char Type
	)
	{
		
		return scDCDao.getMethodfromSCDataContainer(sheetName, Type);
	}
	
}
