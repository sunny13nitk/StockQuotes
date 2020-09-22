package root.busslogic.Services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import root.busslogic.Services.interfaces.ISCUploadSrv;

@Service
public class SCUploadSrv implements ISCUploadSrv
{
	private RestTemplate restTemplate;
	
	private String restUrl;
	
	@Autowired // restTemplate is Injected
	public SCUploadSrv(
	        RestTemplate restTemplate, @Value(
	            "${rest.url}"
	        ) String crmRestUrl
	)
	{
		super();
		this.restTemplate = restTemplate;
		this.restUrl      = crmRestUrl;
	}
	
	@Override
	public String uploadScrip(
	        String wbfilePath
	) throws Exception
	{
		ResponseEntity<String> msg = null;
		
		if (wbfilePath != null)
		{
			if (wbfilePath.trim().length() > 0)
			{
				msg = restTemplate.postForEntity(restUrl, wbfilePath, String.class);
			}
		}
		return msg.getBody().toString();
	}
	
}
