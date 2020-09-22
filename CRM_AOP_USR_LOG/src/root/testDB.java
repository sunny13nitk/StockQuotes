package root;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class testDB
 */
@WebServlet(
    "/testDB"
)
public class testDB extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(
	        HttpServletRequest request, HttpServletResponse response
	) throws ServletException, IOException
	{
		
		// Get Connection to DB
		
		String dbUrl  = "jdbc:sqlserver://localhost;databasename=demo;integratedSecurity=true";
		String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		
		try
		{
			
			PrintWriter writer = response.getWriter();
			
			writer.println("Connecting to Db: " + dbUrl);
			
			Class.forName(driver); // Checking driver Class
			
			Connection conn = DriverManager.getConnection(dbUrl);
			
			if (conn != null)
			{
				writer.println("SUCCESS!!");
				
				conn.close();
			}
			
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new ServletException(e);
		}
		
	}
	
}
