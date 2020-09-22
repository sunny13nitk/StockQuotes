/**
 * 
 */
package modelframework.aspects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import modelframework.definitions.Changed_Property;
import modelframework.definitions.EntityManager;
import modelframework.definitions.EntityMetadata;
import modelframework.enums.system.modelEnums.entityMode;
import modelframework.exceptions.EX_DuplicateAnnotationException;
import modelframework.exceptions.EX_PKeynotSpecified;
import modelframework.exposed.FrameworkManager;
import modelframework.implementations.GeneralMessage;
import modelframework.implementations.MessagesFormatter;
import modelframework.implementations.RootObject;
import modelframework.interfaces.IModelObject;
import modelframework.services.LockManagerService;
import modelframework.types.TY_MethodAnnotation;
import modelframework.utilities.AnnotationScanner;

@Component
@Aspect
/**
 * Aspect for Saving Data to DB Handles Root/Model Object(s)
 */
public class SaveAspect
{
	@Autowired
	private FrameworkManager				frameworkManager;
	@Autowired
	private MessagesFormatter			msgFormatter;
	@Autowired
	private EntityManager				entManager;
	@Autowired
	private LockManagerService			lockManager;

	private boolean					savepointON;
	private Connection					conn;
	private Savepoint					savePoint;
	private ArrayList<TY_MethodAnnotation>	methodAnnotations;

	private boolean					modelObject;
	private boolean					donotCommit;
	private ArrayList<EntityMetadata>		entMdtColl;
	private EntityMetadata				rootEntMdt;
	private ArrayList<PreparedStatement>	prepStmtBuffer;

	/**
	 * @return the frameworkManager
	 */

	public FrameworkManager getFrameworkManager()
	{
		return frameworkManager;
	}

	/**
	 * @return the msgFormatter
	 */
	public MessagesFormatter getMsgFormatter()
	{
		return msgFormatter;
	}

	/**
	 * @return the savepointON
	 */
	public boolean isSavepointON()
	{
		return savepointON;
	}

	/**
	 * @param savepointON
	 *             the savepointON to set
	 */
	public void setSavepointON(boolean savepointON)
	{
		this.savepointON = savepointON;
	}

	/**
	 * @return the conn
	 */
	public Connection getConn()
	{
		return conn;
	}

	/**
	 * @param conn
	 *             the conn to set
	 */
	public void setConn(Connection conn)
	{
		this.conn = conn;
	}

	/**
	 * @return the savePoint
	 */
	public Savepoint getSavePoint()
	{
		return savePoint;
	}

	/**
	 * @param savePoint
	 *             the savePoint to set
	 */
	public void setSavePoint(Savepoint savepoint)
	{
		this.savePoint = savepoint;
	}

	/**
	 * @return the modelObject
	 */
	public boolean isModelObject()
	{
		return modelObject;
	}

	/**
	 * @param modelObject
	 *             the modelObject to set
	 */
	public void setModelObject(boolean modelObject)
	{
		this.modelObject = modelObject;
	}

	/**
	 * @return the prepStmtBuffer
	 */
	public ArrayList<PreparedStatement> getPrepStmtBuffer()
	{
		return prepStmtBuffer;
	}

	/**
	 * @param prepStmtBuffer
	 *             the prepStmtBuffer to set
	 */
	public void setPrepStmtBuffer(ArrayList<PreparedStatement> prepStmtBuffer)
	{
		this.prepStmtBuffer = prepStmtBuffer;
	}

	/**
	 * @return the methodAnnotations
	 */
	public ArrayList<TY_MethodAnnotation> getMethodAnnotations()
	{
		return methodAnnotations;
	}

	/**
	 * @param methodAnnotations
	 *             the methodAnnotations to set
	 */
	public void setMethodAnnotations(ArrayList<TY_MethodAnnotation> methodAnnotations)
	{
		this.methodAnnotations = methodAnnotations;
	}

	/**
	 * @return the entMdtColl
	 */
	public ArrayList<EntityMetadata> getEntMdtColl()
	{
		return entMdtColl;
	}

	/**
	 * @param entMdtColl
	 *             the entMdtColl to set
	 */
	public void setEntMdtColl(ArrayList<EntityMetadata> entMdtColl)
	{
		this.entMdtColl = entMdtColl;
	}

	/**
	 * @return the rootEntMdt
	 */
	public EntityMetadata getRootEntMdt()
	{
		return rootEntMdt;
	}

	/**
	 * @param rootEntMdt
	 *             the rootEntMdt to set
	 */
	public void setRootEntMdt(EntityMetadata rootEntMdt)
	{
		this.rootEntMdt = rootEntMdt;
	}

	/**
	 * @return the donotCommit
	 */
	public boolean isDonotCommit()
	{
		return donotCommit;
	}

	/**
	 * @param donotCommit
	 *             the donotCommit to set
	 */
	public void setDonotCommit(boolean donotCommit)
	{
		this.donotCommit = donotCommit;
	}

	/********************************************************************************************************
	 * Around advice for Save Method Invoked on RootObject Controlled via Anotation @Saveable and
	 * Target Object to be Root
	 * 
	 * @return - Saved Boolean
	 * @throws Exception
	 *******************************************************************************************************/

	@Around("@annotation(modelframework.annotations.Saveable)")

	public boolean Save(ProceedingJoinPoint pjp) throws Exception
	{
		boolean saved = false;

		/*************************************************************************************************
		 * Root Object
		 *************************************************************************************************/
		if (pjp.getTarget() instanceof RootObject)
		{

			RootObject rootObj = (RootObject) pjp.getTarget();

			saved = saveRootObj(rootObj);
		}

		/*************************************************************************************************
		 * Model Object
		 *************************************************************************************************/
		if (pjp.getTarget() instanceof IModelObject)
		{
			/**
			 * Temporary Buffer eill be cleared after Commit
			 */
			this.prepStmtBuffer = new ArrayList<PreparedStatement>();
			/**
			 * Also Initialize Connection and Savepoint
			 * 
			 */
			initConnection();
			this.setModelObject(true);
			this.setSavePoint(this.getConn().setSavepoint());

			try
			{
				// method pJP execute();
				pjp.proceed();
			}
			catch (Throwable e)
			{
				// Get Hold OfExceptions
				getConn().rollback(getSavePoint());
			}

			finally
			{
				for ( PreparedStatement prepStatement : prepStmtBuffer )
				{
					if (prepStatement.isClosed() != true)
					{
						prepStatement.close();

					}
				}
				this.prepStmtBuffer.clear();
				this.getConn().setAutoCommit(true);
				this.getConn().close();
			}
		}

		return saved;

	}

	@SuppressWarnings("unchecked")
	private boolean saveRootObj(RootObject rootObj) throws Exception
	{
		boolean saved = false;

		// 1. Asign variables
		if (rootObj != null)
		{
			if (rootObj.getFW_Manager() != null)
			{
				if (rootObj.getFW_Manager().getMessageFormatter() != null)
				{

					/**
					 * Get all Save related Annotations from Annotation Scanner
					 * if any maintained on the Object
					 */
					this.setMethodAnnotations(AnnotationScanner.getMethodsforAnnotations(rootObj.getClass(), new Class[]
					{ modelframework.annotations.BeforeSaveValidator.class, modelframework.annotations.AfterCommitHandler.class,
					          modelframework.annotations.BeforeCommitValidator.class, modelframework.annotations.BeforeSaveHandler.class
					}));

					/**
					 * First Evaluate ValidateBeforeSave In Case of validation Failure
					 * - Stop Futher Processing
					 * - Update Log
					 * - Only 1 Save before Validator can be present in an Object
					 */
					handleBeforeSaveValidator_Annotations(rootObj);

					/**
					 * Set Entities Metadata Collection for Current Root Object from Entity Manager
					 */
					this.setEntMdtColl(entManager.getEntityMetadataColl_RootObject(rootObj));
					this.setRootEntMdt(this.entMdtColl.get(0));
					/**
					 * Evaluate Savepoint though it would be set only once per Object but
					 * still needed to
					 * Inject Key Generations in Individual Entity Metadata
					 */

					evalSavepoint(rootObj);

					try
					{
						// Initialize Connection in case it is already not

						if (getConn() == null)
						{

							{
								initConnection();
								this.setModelObject(false);
							}
						}

						else
						{
							/**
							 * Connection Already Initialized
							 * Triggered from Model Object
							 * disable Commit here in Root Object in this case and also don't
							 * execute finally
							 * to deallocate the resources
							 */

							if (this.getConn().isClosed() == true)
							{
								initConnection();

							}
						}

						/*
						 * // Create Savepoint if needed
						 * if (this.isSavepointON())
						 * {
						 */
						if (this.getConn() != null)
						{
							// Create Savepoint if needed
							if (this.isSavepointON())
							{
								this.setSavePoint(this.getConn().setSavepoint());
							}
							if (rootObj.getEntityManager() != null)
							{

								if (rootObj.getEntityManager().getMetadataColl() != null)
								{
									if (rootObj.getEntityManager().getMetadataColl().size() > 0)
									{
										// Instruct Entity manager to Generate
										// Prepared Statements
										try
										{
											/**
											 * Handle BeforeSaveHandler
											 * Annotations if any specified
											 */
											handleBeforeSaveHandler_Annotations(rootObj);

											/**
											 * Get the relevant EntityMetadata Collection for the Root
											 * Object
											 */

											if (this.getEntMdtColl() != null)
											{

												rootObj.getEntityManager().generate_PreparedStatements(getConn(), this.getEntMdtColl());

												/**
												 * Trigger Commit if is notModel
												 * Object
												 */
												if (isModelObject() == false)
												{
													/**
													 * Handle Before Commit
													 * Validator
													 */
													handleBeforeCommitValidator_Annotations(rootObj);
													getConn().commit();
													/**
													 * Handle After Commit
													 * Annotations
													 */
													handleAfterCommitHandler_Annotations(rootObj);
													/**
													 * Refresh the Entity Manager metadata items for
													 * current Root Object
													 */

													refreshEntityManager();

													releaseLock();
													// Return True
													saved = true;

												}
											}
										}
										catch (EX_PKeynotSpecified e)
										{
											getConn().rollback(getSavePoint());
											this.getMsgFormatter().generate_message_snippet(e);
											throw new Exception(e);

										}
									}
								}
							}
						}
					}

					// }
					catch (SQLException e)
					{
						getConn().rollback(getSavePoint());
						GeneralMessage msgReset = new GeneralMessage("ERR_CONN", new Object[]
						{ e.getMessage()
						});
						String msg = msgFormatter.generate_message_snippet(msgReset);
						throw new SQLException(msg);

					}
					finally // Release Resources like connection, prepared statements etc
					{
						try
						{
							/**
							 * Close all prepared Statements - if any open
							 * Only in case of a Root Object Invocation
							 * Not in case of Model Object Invocation
							 * Else the resources would be released separately in Model Object
							 * scenario
							 * in that case populated prepared statements to close in a
							 * temporary buffer
							 */

							if (isModelObject() == false)
							{
								for ( EntityMetadata entityMetadata : rootObj.getEntityManager().getMetadataColl() )
								{
									if (entityMetadata.getPreparedStatement() != null)
									{
										if (entityMetadata.getPreparedStatement().isClosed() != true)
										{
											entityMetadata.getPreparedStatement().close();
										}
									}
								}
								/**
								 * Set Connection Auto Commit ON and return connection to pool
								 */
								/**
								 * Only Commit if Do not commit is not True
								 * else just close the connection
								 */
								if (isDonotCommit() == false)
								{
									this.getConn().setAutoCommit(true);
								}
								this.getConn().close();
							}
							else
							{
								for ( EntityMetadata entityMetadata : rootObj.getEntityManager().getMetadataColl() )
								{
									if (entityMetadata.getPreparedStatement() != null)
									{
										this.prepStmtBuffer.add(entityMetadata.getPreparedStatement());
									}
								}
								// Let the same connection persist
							}
						}
						catch (SQLException Ex)
						{
							// Do nothing
						}
					}

				}
			}

		}

		return saved;
	}

	private void releaseLock()
	{
		if (this.getRootEntMdt() != null)
		{
			if (this.lockManager != null)
			{
				lockManager.releaseLock(this.getRootEntMdt().getObjectName(), this.getRootEntMdt().getPrimaryKey().getValue());
			}
		}
	}

	private void refreshEntityManager() throws SQLException
	{
		boolean delMode = false;
		for ( EntityMetadata entityMetadata : entMdtColl )
		{
			if (entityMetadata.getEntityMode() == entityMode.DELETE)
			{
				delMode = true;

			}
			else
			{
				if (delMode == false)
				{
					try
					{

						EntityMetadata entMdt = entManager.getMetadataColl().stream()
						          .filter(x -> x.getSelfID().equals(entityMetadata.getSelfID())).findFirst().get();
						if (entMdt != null)
						{
							entMdt.setEntityMode(entityMode.REFRESHED);
							entMdt.setPrimaryKey(entityMetadata.getPrimaryKey());
							entMdt.setForeignKey(entityMetadata.getForeignKey());
							entMdt.setChangedProperties(new ArrayList<Changed_Property>());
							if (entMdt.getPreparedStatement() != null)
							{
								if (entMdt.getPreparedStatement().isClosed() != true)
								{
									entMdt.getPreparedStatement().close();

								}
								entityMetadata.setPreparedStatement(null);
							}
						}
					}
					catch (NoSuchElementException NE)
					{
						// Do Nothing
					}
				}
			}
		}
		/**
		 * Clear collection in case of delete mode ON
		 */
		if (delMode == true)
		{
			entMdtColl.clear();
		}
	}

	@SuppressWarnings("static-access")
	private void initConnection() throws SQLException
	{
		if (this.frameworkManager.getConnectionPool() != null)
		{
			this.setConn(this.frameworkManager.getConnectionPool().getConnection_TxnMode());
		}
	}

	/**
	 * Evaluate if Savepoints is needed acccording to Root Object entity Manager
	 */
	private void evalSavepoint(RootObject rootObj)
	{
		EntityManager entMgr = rootObj.getEntityManager();
		if (entMgr != null)
		{
			int evalHierarchy = 0;
			// Get Maximum Hierarchy Value
			int maxHierarchy = this.getEntMdtColl().stream().mapToInt(EntityMetadata::getHierarchy).max().getAsInt();
			if (maxHierarchy > 0) // Any Related Entity Fouund In Entity Manager
			{
				evalHierarchy = maxHierarchy;
				while (evalHierarchy > 0)
				{
					int currHierarchy = evalHierarchy;
					try
					{
						EntityMetadata entMdt = this.getEntMdtColl().stream().filter(x -> x.getHierarchy() == currHierarchy).findFirst().get();

						if (entMdt != null)
						{
							/**
							 * Not forget to set Key Gen on the lowest(max value) hierarchy
							 * relaton - this would lead to SQL dump otherwise
							 * in case Table field is present in Primary key
							 */
							// if (entMdt.getPrimaryKey().getTableField() != null)
							// {
							// entMdt.setKeyGen(true);
							// }
							/**
							 * Now Evaluate higher hierarchy/parent relations
							 */
							// get Foreign Key field in Highest Hierarchy Relation
							String fKey = entMdt.getForeignKey().getObjField();
							if (fKey != null)
							{
								// Get Parent Relation if Fkey found
								EntityMetadata entMdt_parent = this.getEntMdtColl().stream()
								          .filter(x -> x.getHierarchy() == (currHierarchy - 1)).findFirst().get();
								// Compare Foreign Key in PArent Relation
								// Primary with AutoKey ON
								if (entMdt_parent.getPrimaryKey().getTableField() != null)
								{
									if (entMdt_parent.getPrimaryKey().getTableField().equals(fKey))
									{
										// save Point needed
										if (isSavepointON() == false)
										{
											setSavepointON(true);
										}

										// Also set KeyGen for
										// Relations with
										// same Name as entMdt_parent
										for ( EntityMetadata mDT : this.getEntMdtColl() )
										{
											if (mDT.getObjectName().equals(entMdt_parent.getObjectName()))
											{

												// Set Key Gen
												// ON
												mDT.setKeyGen(true);

											}
										}
									}
								}
							}
						}

						evalHierarchy--;
					}

					catch (NoSuchElementException NE)
					{
						// do nothing
					}
				}
			}

		}
	}

	/**
	 * Handles Before Save Annotation is any specified on Root Object extended instance
	 * 
	 * @param rootObj
	 * @throws Exception
	 */
	private void handleBeforeSaveValidator_Annotations(RootObject rootObj) throws Exception
	{
		if (this.getMethodAnnotations().size() > 0)
		{
			// Scan for matching modelframework.annotations.BeforeSaveValidator Annotations

			ArrayList<Method> saveBeforeValidators = new ArrayList<Method>();
			try
			{
				saveBeforeValidators = AnnotationScanner.getMethodfromAnnotationList(this.getMethodAnnotations(),
				          modelframework.annotations.BeforeSaveValidator.class, true);
			}
			catch (EX_DuplicateAnnotationException e1)
			{
				// ERR_MULTI_VALDATORS=Only One Before Save Validator Allowed
				// for an Object! {0} - Validators identified for Object - {1}
				GeneralMessage msgReset = new GeneralMessage("ERR_MULTI_VALDATORS", new Object[]
				{ rootObj.getClass().getSimpleName(), modelframework.annotations.BeforeSaveValidator.class.getSimpleName()
				});
				String msg = msgFormatter.generate_message_snippet(msgReset);
				throw new Exception(msg);
			}

			// Get the relevant first method
			try
			{
				boolean errorValidation = (boolean) saveBeforeValidators.get(0).invoke(rootObj, new Object[0]);
				if (errorValidation == true)
				{
					// ERR_SAVE_VALIDATE=Not Able to Proceed
					// Saving of Object {1} due to Validation
					// Error!
					GeneralMessage msgReset = new GeneralMessage("ERR_SAVE_VALIDATE", new Object[]
					{ rootObj.getClass().getSimpleName()
					});
					String msg = msgFormatter.generate_message_snippet(msgReset);
					throw new Exception(msg);
				}

			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void handleBeforeCommitValidator_Annotations(RootObject rootObj) throws Exception
	{
		if (this.getMethodAnnotations().size() > 0)
		{
			// Scan for matching modelframework.annotations.BeforeSaveValidator Annotations

			ArrayList<Method> commitBeforeValidators;
			try
			{
				commitBeforeValidators = AnnotationScanner.getMethodfromAnnotationList(this.getMethodAnnotations(),
				          modelframework.annotations.BeforeCommitValidator.class, true);
			}
			catch (EX_DuplicateAnnotationException e1)
			{
				// ERR_MULTI_COMMITVALDATORS=Only One Before Commit Validator Allowed for an Object!
				// {0} using annotation {1}
				GeneralMessage msgReset = new GeneralMessage("ERR_MULTI_COMMITVALDATORS", new Object[]
				{ rootObj.getClass().getSimpleName(), modelframework.annotations.BeforeCommitValidator.class.getSimpleName()
				});
				String msg = msgFormatter.generate_message_snippet(msgReset);
				this.setDonotCommit(true);
				throw new Exception(msg);
			}

			// Get the relevant first method
			try
			{
				if (commitBeforeValidators.size() > 0)
				{
					boolean commit = (boolean) commitBeforeValidators.get(0).invoke(rootObj, new Object[0]);
					if (commit == false)
					{
						// ERR_COMMIT_VALIDATE=Not Able to Proceed
						// Saving of Object {1} due to Validation
						// Error before Comitting!
						GeneralMessage msgReset = new GeneralMessage("ERR_COMMIT_VALIDATE", new Object[]
						{ rootObj.getClass().getSimpleName()
						});
						String msg = msgFormatter.generate_message_snippet(msgReset);
						this.setDonotCommit(true);
						throw new Exception(msg);
					}

				}
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void handleBeforeSaveHandler_Annotations(RootObject rootObj) throws Exception
	{
		if (this.getMethodAnnotations().size() > 0)
		{
			// Scan for matching modelframework.annotations.BeforeSaveValidator Annotations

			ArrayList<Method> saveBeforeHandlers;
			try
			{

				saveBeforeHandlers = AnnotationScanner.getMethodfromAnnotationList(this.getMethodAnnotations(),
				          modelframework.annotations.BeforeSaveHandler.class, true);

			}
			catch (EX_DuplicateAnnotationException e1)
			{
				// ERR_MULTI_SAVEHANDLERS=Only One Save Handler Allowed for an Object! {0} using
				// annotation {1}
				GeneralMessage msgReset = new GeneralMessage("ERR_MULTI_SAVEHANDLERS", new Object[]
				{ rootObj.getClass().getSimpleName(), modelframework.annotations.BeforeSaveHandler.class.getSimpleName()
				});
				String msg = msgFormatter.generate_message_snippet(msgReset);
				throw new Exception(msg);
			}

			// Get the relevant first method
			try
			{
				if (saveBeforeHandlers.size() > 0)
				{
					saveBeforeHandlers.get(0).invoke(rootObj, new Object[0]);
				}

			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				throw new Exception(e.getMessage());
			}

		}
	}

	private void handleAfterCommitHandler_Annotations(RootObject rootObj) throws Exception
	{
		if (this.getMethodAnnotations().size() > 0)
		{
			// Scan for matching modelframework.annotations.BeforeSaveValidator Annotations

			ArrayList<Method> afterCommitHandlers;

			afterCommitHandlers = AnnotationScanner.getMethodfromAnnotationList(this.getMethodAnnotations(),
			          modelframework.annotations.AfterCommitHandler.class, false);

			// Get the relevant first method
			try
			{
				if (afterCommitHandlers.size() > 0)
				{
					afterCommitHandlers.get(0).invoke(rootObj, new Object[0]);
				}

			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				throw new Exception(e.getMessage());
			}

		}
	}

}
