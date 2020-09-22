package root.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import root.exceptions.ScripUploadException;
import root.scripsEngine.pojos.helperObjs.XlsFilePath;
import root.scripsEngine.uploadEngine.exceptions.EX_General;
import root.scripsEngine.uploadEngine.scripSheetServices.interfaces.IXLS_Scrip_Upload_Srv;

@RestController
@RequestMapping(
    "/apiSCE"
)
public class ScripsRestController
{
	@Autowired
	private IXLS_Scrip_Upload_Srv scUploadSrv;
	
	@PostMapping(
	    "/scrip"
	)
	public String uploadScrip(
	        @RequestBody XlsFilePath xlsFilePath
	)
	{
		String msg = null;
		if (xlsFilePath != null)
		{
			if (xlsFilePath.getFilePath() != null)
			{
				if (xlsFilePath.getFilePath().trim().length() > 0 && scUploadSrv != null)
				{
					try
					{
						if (scUploadSrv.Upload_Scrip_from_XLS_Filepath(xlsFilePath.getFilePath()))
						{
							msg = "Scrip Uploaded Successfully!";
						}
					} catch (EX_General e)
					{
						throw new ScripUploadException(e.getMessage());
					}
				}
			}
		}
		
		return msg;
	}
}
