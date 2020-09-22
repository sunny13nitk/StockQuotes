package modelframework.usermanager.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import modelframework.events.declarations.EV_Logon;
import modelframework.usermanager.exceptions.EX_InvalidLogonException;
import modelframework.usermanager.exceptions.EX_UserLogonException;
import modelframework.utilities.EncryptionUtility;

/********************************************************************************************
 * User Logon Service - Initialized in Configuration Xml as session scoped proxy bean
 *******************************************************************************************/

public class LogonService implements ApplicationContextAware, ApplicationEventPublisherAware
{
	
	private DataSource datasource;
	
	private String username;
	
	private Boolean authenticated;
	
	private ApplicationEventPublisher publisher;
	
	public String getUsername(
	)
	{
		return username;
	}
	
	public boolean IS_Authenticated(
	)
	{
		return authenticated;
	}
	
	public void setUsername(
	        String username
	)
	{
		this.username = username;
	}
	
	public LogonService(
	)
	{
		
	}
	
	/*************************************************************************************************
	 * Validate the User Logon passing User Password
	 * 
	 * @throws EX_UserLogonException
	 * @throws SQLException
	 * @throws EX_InvalidLogonException
	 * 
	 * 
	 *************************************************************************************************/
	public void Validate_Logon(
	        String password
	) throws EX_UserLogonException, SQLException, EX_InvalidLogonException
	{
		
		// Using Join get the encrypted password and Key for the User
		if (this.datasource != null && password != null)
		{
			// Declared Outside try to close in finally
			Connection        Conn  = null;
			PreparedStatement pstmt = null;
			ResultSet         rs    = null;
			String            pw    = null;
			
			try
			{
				Conn = datasource.getConnection();
				if (Conn != null)
				{
					
					if (this.username != "DRYRUN")
					{
						// Connection up and running
						// Prepare Query String to fetch infor from DB for the User
						String query = "SELECT TB_UserKey.UserName,TB_UserKey.KeyGen,TB_KeyPairs.KeyValue  "
						        + "from TB_UserKey , TB_KeyPairs" + " Where TB_UserKey.UserName = ?  "
						        + " AND TB_UserKey.KeyGen = TB_KeyPairs.KeyGen";
						
						// Now Get a prepared Statement
						pstmt = Conn.prepareStatement(query);
						pstmt.setString(1, this.getUsername());
						
						rs = pstmt.executeQuery();
						
						if (rs.next())
						{
							// Store Key and Value in HashMap to be sent for Decryption
							Map<String, String> Key_Value = new HashMap<String, String>();
							Key_Value.put(rs.getString(2), rs.getString(3));
							try
							{
								pw = EncryptionUtility.Decrypt(Key_Value);
								
							} catch (Exception Ex)
							{
								throw new EX_UserLogonException(new String[]
								{ this.getUsername(), "Error Decrypting Password!- " + Ex.getMessage() });
							}
							
							if (pw.equals(password))
							{
								this.authenticated = true;
								// Trigger the Logon event to Initialize Framework
								EV_Logon logonEvent = new EV_Logon(this);
								publisher.publishEvent(logonEvent);
							} else
							{
								throw new EX_InvalidLogonException(getUsername());
							}
							
						}
						
					} else
					{
						this.authenticated = true;
						// Trigger the Logon event to Initialize Framework
						EV_Logon logonEvent = new EV_Logon(this);
						publisher.publishEvent(logonEvent);
					}
					
				}
			} catch (SQLException Ex)
			{
				throw new EX_UserLogonException(
				        new String[]
						{ this.getUsername(), "SQL Error! " + Ex.getMessage() });
			}
			
			finally
			{
				if (this.username != "DRYRUN")
				{
					rs.close();
					pstmt.close();
					Conn.close();
				}
			}
			
		}
		
	}
	
	@Override
	public void setApplicationContext(
	        ApplicationContext context
	        ) throws BeansException
	{
		if (context != null)
		{
			this.datasource = (DataSource) context.getBean("dataSource");
		}
		
	}
	
	@Override
	public void setApplicationEventPublisher(
	        ApplicationEventPublisher publisher
	        )
	{
		if (publisher != null)
		{
			this.publisher = publisher;
		}
		
	}
	
}
