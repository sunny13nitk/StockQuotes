package modelframework.usermanager.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import modelframework.usermanager.services.LogonService;

/*************************************************************************************************
 * User Manager Service - Initialized as a singleton proxy bean by package scan in configuration XML
 * 
 * Holds the following attributes : Reference to AOP session scoped LogonService Bean
 *************************************************************************************************/
@Service
public class UserManager
	{
		@Autowired
		private LogonService LogonService;
		
		public LogonService getLogonService()
			{
				return LogonService;
			}
			
		public String Get_LoggedUser()
			{
				String username = null;
				if (this.LogonService != null)
					{
						username = LogonService.getUsername();
					}
				return username;
			}
			
		public boolean IS_User_Authentic()
			{
				boolean authenticated = false;
				
				authenticated = this.LogonService.IS_Authenticated();
				return authenticated;
			}
			
		public UserManager()
			{
				
			}
			
	}
