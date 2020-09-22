package scriptsengine.uploadengine.services.implementations;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import scriptsengine.uploadengine.services.interfaces.IWBfromFilepath;

@Service("WBFilepathService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WBFilepathService implements IWBfromFilepath
{

	@Override
	public XSSFWorkbook getWBcontextfromFilepath(String filepath) throws IOException
	{
		XSSFWorkbook wbcontext = null;
		if (filepath != null)
		{
			FileInputStream fis = new FileInputStream(filepath);
			// Finds the workbook instance for XLSX file
			wbcontext = new XSSFWorkbook(fis);
		}

		return wbcontext;
	}

}
