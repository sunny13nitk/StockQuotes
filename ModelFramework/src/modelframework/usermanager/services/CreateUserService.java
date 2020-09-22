package modelframework.usermanager.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import modelframework.usermanager.exceptions.EX_DuplicateUserException;
import modelframework.usermanager.exceptions.EX_UserCreationException;
import modelframework.usermanager.models.SimpleUser;
import modelframework.utilities.EncryptionUtility;

/********************************************************************************************************
 * User Creation Service that Creates a New User in following Steps Step 1- Initialize_Creation by passing the
 * User details Step 2 - CreateUser to finally create the user
 *******************************************************************************************************/
@Service
public class CreateUserService
	{
		@Autowired
		private DataSource	datasource;
		
		private SimpleUser	User;
		
		private Boolean		Created;
		
		public DataSource getDatasource()
			{
				return datasource;
			}
			
		public Boolean IS_Created()
			{
				return Created;
			}
			
		public SimpleUser getUser()
			{
				return User;
			}
			
		/**********************************************************************************
		 * Initialize User Creation by passing User name and Password via SimpleUser Object
		 **********************************************************************************/
		public void Initialize_Creation(SimpleUser userNew)
			{
				if (userNew != null)
					{
						this.User = userNew;
					}
			}
			
		/*************************************************************************************************
		 * Invoke User Creation Process
		 * 
		 * @throws SQLException
		 *             : Database Connection Issue
		 * @throws EX_DuplicateUserException
		 *             : User already Exists in system
		 * @throws EX_UserCreationException
		 *             : Error During User Creation process
		 *************************************************************************************************/
		public void CreateUser() throws SQLException, EX_DuplicateUserException, EX_UserCreationException
			{
				
				if (this.getDatasource() != null && this.getUser() != null)
					{
						boolean user_exists = UserAlreadyExists();
						if (user_exists == true)
							{
								throw new EX_DuplicateUserException(this.getUser().getUserName());
							}
						else
							{
								
								String QY_newUser = "INSERT INTO TB_UserKey (UserName, KeyGen )VALUES ( ?,? )";
								String QY_keyGen = "INSERT INTO TB_KeyPairs (KeyGen,KeyValue) Values (?,? )";
								
								Connection conn = null;
								PreparedStatement Insert_User = null;
								PreparedStatement Insert_Keys = null;
								try
									{
										// Get Connection in Txn Mode
										conn = this.getDatasource().getConnection();
										conn.setAutoCommit(false);
										
										Insert_User = conn.prepareStatement(QY_newUser);
										Insert_Keys = conn.prepareStatement(QY_keyGen);
										
										Map<String, String> Key_Value = EncryptionUtility
										        .Encrypt(this.getUser().getPassword());
										if (Key_Value != null)
											{
												Set keys = Key_Value.keySet();
												Iterator itr = keys.iterator();
												
												String key = null;
												String value = null;
												while (itr.hasNext())
													{
														key = (String) itr.next();
														value = Key_Value.get(key);
														
													}
												if (key != null && value != null)
													{
														Insert_User.setString(1, this.getUser().getUserName());
														Insert_User.setString(2, key);
														Insert_User.executeUpdate();
														
														Insert_Keys.setString(1, key);
														Insert_Keys.setString(2, value);
														Insert_Keys.executeUpdate();
														try
															{
																conn.commit();
																this.Created = true;
															}
														catch (SQLException Ex)
															{
																// If Commit fails
																// roll back both transactions
																conn.rollback();
																throw new Exception("Not able to Save User to Dbase!");
															}
															
													}
												else
													{
														
														// Trigger and Raise User Creation Exception in
														// Password Encryption
														throw new Exception("Not able to Encrypt User Password!!");
													}
													
											}
									}
									
								catch (Exception Ex)
									{
										this.Created = false;
										throw new EX_UserCreationException(
										        new String[] { this.getUser().getUserName(), Ex.getMessage() });
									}
									
								finally
									{
										Insert_User.close();
										Insert_Keys.close();
										conn.setAutoCommit(true);
										conn.close();
										
									}
									
							}
					}
			}
			
		/*************************************************************************************************
		 * Validate that requested User Already exists in the system
		 * 
		 * @throws SQLException
		 *             : Database Connection Issue
		 *************************************************************************************************/
		private boolean UserAlreadyExists() throws SQLException
			{
				boolean exists = false;
				int records = 0;
				Connection conn;
				String Query = "Select COUNT(*) from TB_UserKey Where Username = ?";
				
				try
					{
						conn = this.getDatasource().getConnection();
						if (conn != null)
							{
								// Connection Established
								// Write Prepare Statement
								PreparedStatement prep_stmt = conn.prepareStatement(Query);
								prep_stmt.setString(1, this.User.getUserName());
								ResultSet rs = prep_stmt.executeQuery();
								while (rs.next())
									{
										records = rs.getInt(1);
									}
									
								// Close the Statement
								prep_stmt.close();
								// Close the Connection
								conn.close();
							}
							
						if (records > 0)
							{
								exists = true;
							}
					}
				catch (SQLException e)
					{
						throw new SQLException(e.getMessage());
					}
					
				return exists;
			}
			
	}
