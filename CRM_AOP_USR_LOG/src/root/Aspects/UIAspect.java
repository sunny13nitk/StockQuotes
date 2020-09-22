package root.Aspects;

import java.sql.Timestamp;
import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import root.entity.SessionI;
import root.enums.Enums;
import root.services.interfaces.ISessionSrv;

@Aspect
@Component
public class UIAspect
{
	@Autowired
	private ISessionSrv sessSrv;
	
	@Before(
	    "anyControllerMethod()"
	)
	public void logControllers(
	        JoinPoint jp
	)
	{
		if (jp != null)
		{
			// Populate Session Item Instance
			if (sessSrv != null)
			{
				if (sessSrv.getSessionHeader() != null)
				{
					SessionI sessI = new SessionI(sessSrv.getSessionHeader().getSessionGuid());
					if (sessI != null)
					{
						sessI.setAspectName(this.getClass().getSimpleName());
						
						sessI.setArtifactName(jp.getTarget().getClass().getSimpleName());
						
						sessI.setMethodName(jp.getSignature().getName());
						
						sessI.setTimeStamp(new Timestamp(new Date().getTime()));
						
						sessI.setOpType(Enums.dbOperation.None);
						sessI.setMessage("UI Navigation");
						
						sessSrv.getSessionHeader().addSessionItem(sessI);
					}
					
				}
			}
		}
		
	}
	
	// - POINTCUTS
	
	// Any Controller Method
	@Pointcut(
	    "execution(* root.controllers.*.*(..))"
	)
	public void anyControllerMethod(
	)
	{
		
	}
	
}
