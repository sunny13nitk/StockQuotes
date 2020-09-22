package root;

import java.sql.Connection;
import java.sql.DriverManager;

public class testJDBC
{
	
	public static void main(
	        String[] args
	)
	{
		try
		{
			String jdbcUrl_sqlServer = "jdbc:sqlserver://localhost;databasename=demo;integratedSecurity=true";
			
			System.out.println("Connecting to DB: " + jdbcUrl_sqlServer);
			
			// Get connection from Driver Manager
			Connection myConn = DriverManager.getConnection(jdbcUrl_sqlServer);
			if (myConn != null)
			{
				System.out.println("Connection Successful");
			}
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
}
