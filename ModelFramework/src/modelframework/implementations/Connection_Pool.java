/**
 * 
 */
package modelframework.implementations;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * Connection Pool Class Stores the Static Connection Instance - SO we only have
 * 1 connection per Instance Stores Prepared Statements for Objects by their
 * Simple Name for below operations as INSERT - Using Object Simple Name UPDATE
 * - Using Object Simple && ( <Fname,Value> Changed Properties)
 */

public class Connection_Pool
{

	private static Connection	Connection;

	private static DataSource	Datasource;

	/*************************************************************************************************
	 * Get the Connection in AutoCommit mode i.e Auto commit turned ON
	 * 
	 * @throws SQLException
	 *              : Database Connection Issue
	 *************************************************************************************************/
	public Connection getConnection() throws SQLException
	{

		if (Datasource != null && Connection == null || Connection.isClosed() == true)
		{
			Connection = Datasource.getConnection();
		}

		return Connection;
	}

	/// Initialize Connection
	public Connection_Pool(DataSource ds)
	{
		if (ds != null)
		{
			Datasource = ds;
		}
	}

	/*************************************************************************************************
	 * Get the Connection in Transaction mode i.e Auto commit turned OFF
	 * 
	 * @throws SQLException
	 *              : Database Connection Issue
	 *************************************************************************************************/
	public Connection getConnection_TxnMode() throws SQLException
	{
		Connection Conn = null;
		if (Datasource != null && (Connection == null || Connection.isClosed() == true))
		{

			Connection = Datasource.getConnection();
			Conn = Connection;
			if (Conn.getAutoCommit() == true)
			{
				Conn.setAutoCommit(false);
			}
		}

		else
		{
			Conn = Connection;
			if (Conn.getAutoCommit() == true)
			{
				Conn.setAutoCommit(false);
			}
		}
		return Conn;
	}

	/*************************************************************************************************
	 * Reset Commit mode on the Connection and return to Connection pool
	 * 
	 * @throws SQLException
	 *              : Database Connection Issue
	 *************************************************************************************************/
	public void reset_commit_close(Connection Conn) throws SQLException
	{

		Conn.setAutoCommit(true);
		Conn.close();
	}

}
