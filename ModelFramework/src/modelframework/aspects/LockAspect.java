package modelframework.aspects;

import java.util.ArrayList;
import java.util.Locale;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import modelframework.definitions.EntityMetadata;
import modelframework.definitions.LockObject;
import modelframework.enums.system.modelEnums.entityMode;
import modelframework.implementations.DependantObject;
import modelframework.implementations.GeneralMessage;
import modelframework.implementations.MessagesFormatter;
import modelframework.implementations.RootObject;
import modelframework.services.LockManagerService;

@Component
@Aspect
public class LockAspect
{
	@Autowired
	private MessagesFormatter	msgFormatter;

	@Autowired
	private LockManagerService	lockManager;

	@Autowired
	private MessageSource		messageSource;

	/**
	 * How Locking works
	 * 1- Root Object
	 * GEt the Entity Metadata from the Entity Manager
	 * Scan LockManager Servie with Entity Metadata for a lock conflict
	 * If No conflict set the lock entity and return true
	 * If Conflict return false and log lock conflict info in Log file
	 * 
	 * 2- Dependant Object
	 * Implicitly All Dependants for Root are Locked that are present in Entity Manager for the Root Object
	 * 
	 * @param pjp
	 * @return
	 */

	@Around("lockmethod() && target_Root()")
	public Boolean Lock(ProceedingJoinPoint pjp)
	{
		Boolean islocked = false;

		if (pjp != null)
		{
			if (pjp.getTarget() instanceof RootObject)
			{

				RootObject rootObj = (RootObject) pjp.getTarget();
				if (rootObj != null)
				{
					/**
					 * Get Lock Object and also post appropriate messages in logger
					 */
					LockObject lockObj = getLockObjectfroRootObject(rootObj, true);

					// Lock Conflict

					if (lockObj != null)
					{
						islocked = false;
					}
					// Lock Obtained Successfully
					else
					{
						islocked = true;

					}

				}
			}

		}

		return islocked;
	}

	private LockObject getLockObjectfroRootObject(RootObject rootObj, Boolean logmode)
	{
		LockObject lockObj = null;
		if (rootObj != null)
		{
			if (rootObj.getEntityManager() != null)
			{
				// GEt the Entity Metadata from the Entity Manager
				EntityMetadata entMdt = rootObj.getEntityManager().getRootMetadata(rootObj);
				if (entMdt != null)
				{
					lockObj = lockManager.isalreadyLocked(entMdt.getObjectName(), entMdt.getPrimaryKey().getValue());

					if (lockObj != null)
					{
						if (logmode != false)
						{
							if (msgFormatter != null)
							{
								// Entity {0} with Key {1} could not be locked successfully!! It was
								// already locked by {2} since {3}
								GeneralMessage msgLockErr = new GeneralMessage("ERR_LOCKING_OBJ", new Object[]
								{ entMdt.getObjectName(), entMdt.getPrimaryKey().getValue().toString(), lockObj.getUserName(),
								          lockObj.getTimestamp()
								});
								msgFormatter.generate_message_snippet(msgLockErr);
							}
						}

					}
					else
					{
						/**
						 * Create a new lock Object Entry in Lock Manager
						 * Set EntityMetadata Mode to LOCKED
						 */

						if (lockManager.obtainLock(entMdt.getObjectName(), entMdt.getPrimaryKey().getValue()))
						{

							/**
							 * If the Root is Locked then all the dependant Entities in the Session for the Root
							 * must also be locked - Lock should percolate from Root to Dependants
							 */

							ArrayList<EntityMetadata> root_entMdtColl = rootObj.getEntityManager().getEntityMetadataColl_RootObject(rootObj);
							if (root_entMdtColl != null)
							{
								if (root_entMdtColl.size() > 0)
								{
									for ( EntityMetadata entityMetadata : root_entMdtColl )
									{
										entityMetadata.setEntityMode(entityMode.LOCKED);
									}
								}

							}

							if (logmode != false)
							{
								// SUCC_LOCKING_OBJ=Entity {0} with Key {1} locked successfully!!

								GeneralMessage msgLockSucc = new GeneralMessage("SUCC_LOCKING_OBJ", new Object[]
								{ entMdt.getObjectName(), entMdt.getPrimaryKey().getValue().toString()
								});
								msgFormatter.generate_message_snippet(msgLockSucc);
							}

						}

					}
				}
			}
		}

		return lockObj;
	}

	@Around("lockdetails() && target_Root()")
	public String getLockDetails(ProceedingJoinPoint pjp)
	{
		String msg = null;

		if (pjp != null)
		{
			if (pjp.getTarget() instanceof RootObject)
			{
				RootObject rootObj = (RootObject) pjp.getTarget();
				if (rootObj != null)
				{
					/**
					 * Get Lock Object and also post appropriate messages in logger
					 */
					LockObject lockObj = getLockObjectfroRootObject(rootObj, false);

					if (lockObj != null)
					{
						// Entity {0} with Key {1} could not be locked successfully!! It was
						// already locked by {2} since {3}
						msg = messageSource.getMessage("ERR_LOCKING_CURROBJ", new Object[]
						{ rootObj.getClass().getSimpleName(), lockObj.getUserName(), lockObj.getTimestamp()
						}, null, Locale.ENGLISH);

					}
				}
			}

			else if (pjp.getTarget() instanceof DependantObject)
			{

				DependantObject depObj = (DependantObject) pjp.getTarget();
				if (depObj != null)
				{

				}
			}

		}

		return msg;

	}

	/**
	 * For Root Object
	 * Check the Root Entity Metadata is locked or not -> Set to Change mode if yes and return true
	 * For Dependant Object
	 * Check for Dependant Entity Metadata is locked or not -> Set to Change mode if yes and return true
	 * 
	 * @param pjp
	 * @return
	 */
	@Around("switchtoChangeModemethod() && target_RootDependant()")
	public boolean swithtoChangeMode(ProceedingJoinPoint pjp)
	{
		boolean ischangeable = false;

		if (pjp != null)
		{
			if (pjp.getTarget() instanceof RootObject)
			{
				RootObject rootObj = (RootObject) pjp.getTarget();
				if (rootObj != null)
				{
					if (rootObj.getEntityManager() != null)
					{
						// GEt the Entity Metadata from the Entity Manager
						EntityMetadata entMdt = rootObj.getEntityManager().getRootMetadata(rootObj);
						if (entMdt != null)
						{
							if (entMdt.getEntityMode() == entityMode.LOCKED)
							{
								entMdt.setEntityMode(entityMode.CHANGE);
								ischangeable = true;
							}
							else
							{
								// ERR_CHANGING_OBJ=Entity {0} with Key {1} could not be switched to change mode
								// since it is not locked!!
								GeneralMessage msgChgErr = new GeneralMessage("ERR_CHANGING_OBJ", new Object[]
								{ entMdt.getObjectName(), entMdt.getPrimaryKey().getValue().toString()
								});
								msgFormatter.generate_message_snippet(msgChgErr);
							}
						}
					}

				}
			}

			if (pjp.getTarget() instanceof DependantObject)
			{
				DependantObject depObj = (DependantObject) pjp.getTarget();
				if (depObj != null)

				{
					if (depObj.getParentRoot().getEntityManager() != null)
					{
						// GEt the Entity Metadata from the Entity Manager
						EntityMetadata entMdt = depObj.getParentRoot().getEntityManager().getDependantMetadata(depObj);
						if (entMdt != null)
						{
							if (entMdt.getEntityMode() == entityMode.LOCKED)
							{
								entMdt.setEntityMode(entityMode.CHANGE);
								ischangeable = true;
							}
							else
							{
								/**
								 * Give a try to Edit Mode of Root Entity Also - It might happen someone locked
								 * root and fetched relations later
								 */

								if (entMdt.getEntityMode() == entityMode.REFRESHED)
								{
									RootObject rootObj = depObj.getParentRoot();
									if (rootObj != null)
									{
										EntityMetadata entRootMdt = depObj.getParentRoot().getEntityManager().getRootMetadata(rootObj);
										if (entRootMdt != null)
										{
											if (entRootMdt.getEntityMode() == entityMode.LOCKED
											          || entRootMdt.getEntityMode() == entityMode.CHANGE)
											{
												entMdt.setEntityMode(entityMode.CHANGE);
												ischangeable = true;
											}
										}

									}
									else
									{
										// Get Entity Mode from Dependant Parent
										DependantObject depobjParent = depObj.getParentDependant();
										if (depobjParent != null)
										{
											EntityMetadata entdepParentMdt = depObj.getParentRoot().getEntityManager()
											          .getDependantMetadata(depobjParent);
											if (entdepParentMdt != null)
											{
												if (entdepParentMdt.getEntityMode() == entityMode.LOCKED
												          || entdepParentMdt.getEntityMode() == entityMode.CHANGE)
												{
													entMdt.setEntityMode(entityMode.CHANGE);
													ischangeable = true;
												}
											}

										}
									}
								}

								// ERR_CHANGING_OBJ=Entity {0} with Key {1} could not be switched to change mode
								// since it is not locked!!
								GeneralMessage msgChgErr = new GeneralMessage("ERR_CHANGING_OBJ", new Object[]
								{ entMdt.getObjectName(), entMdt.getPrimaryKey().getValue().toString()
								});
								msgFormatter.generate_message_snippet(msgChgErr);
							}
						}

					}
				}
			}

		}

		return ischangeable;
	}

	@Around("deletemethod() && target_RootDependant()")
	public boolean delete(ProceedingJoinPoint pjp)
	{
		boolean isdeletable = false;

		if (pjp != null)
		{
			if (pjp.getTarget() instanceof RootObject)
			{
				RootObject rootObj = (RootObject) pjp.getTarget();
				if (rootObj != null)
				{
					if (rootObj.getEntityManager() != null)
					{
						// GEt the Entity Metadata from the Entity Manager
						EntityMetadata entMdt = rootObj.getEntityManager().getRootMetadata(rootObj);
						if (entMdt != null)
						{
							if (entMdt.getEntityMode() == entityMode.LOCKED)
							{
								entMdt.setEntityMode(entityMode.DELETE);
								isdeletable = true;
							}
							else
							{
								// ERR_DELETING_OBJ=Entity {0} with Key {1} could not be switched to delete mode
								// since it is not locked!!
								GeneralMessage msgChgErr = new GeneralMessage("ERR_DELETING_OBJ", new Object[]
								{ entMdt.getObjectName(), entMdt.getPrimaryKey().getValue().toString()
								});
								msgFormatter.generate_message_snippet(msgChgErr);
							}
						}
					}

				}
			}

			if (pjp.getTarget() instanceof DependantObject)
			{
				DependantObject depObj = (DependantObject) pjp.getTarget();
				if (depObj != null)

				{
					if (depObj.getParentRoot().getEntityManager() != null)
					{
						// GEt the Entity Metadata from the Entity Manager
						EntityMetadata entMdt = depObj.getParentRoot().getEntityManager().getDependantMetadata(depObj);
						if (entMdt != null)
						{
							if (entMdt.getEntityMode() == entityMode.LOCKED)
							{
								entMdt.setEntityMode(entityMode.DELETE);
								isdeletable = true;
							}
							else
							{
								// ERR_DELETING_OBJ=Entity {0} with Key {1} could not be switched to delete mode
								// since it is not locked!!
								GeneralMessage msgChgErr = new GeneralMessage("ERR_DELETING_OBJ", new Object[]
								{ entMdt.getObjectName(), entMdt.getPrimaryKey().getValue().toString()
								});
								msgFormatter.generate_message_snippet(msgChgErr);
							}
						}

					}
				}
			}

		}

		return isdeletable;
	}

	@Around("islockedmethod() && target_RootDependant()")
	public boolean isLocked(ProceedingJoinPoint pjp)
	{
		boolean islocked = false;

		if (pjp != null)
		{
			islocked = isinRequestedMode(pjp, entityMode.LOCKED);
		}

		return islocked;
	}

	@Around("isChangeablemethod() && target_RootDependant()")
	public boolean isChangeable(ProceedingJoinPoint pjp)
	{
		boolean ischangeable = false;

		if (pjp != null)
		{
			ischangeable = isinRequestedMode(pjp, entityMode.CHANGE);
		}

		return ischangeable;
	}

	private boolean isinRequestedMode(ProceedingJoinPoint pjp, entityMode reqMode)
	{
		boolean isinreqMode = false;

		if (pjp != null && reqMode != null)
		{
			if (pjp.getTarget() instanceof RootObject)
			{
				RootObject rootObj = (RootObject) pjp.getTarget();
				if (rootObj != null)
				{
					if (rootObj.getEntityManager() != null)
					{
						// GEt the Entity Metadata from the Entity Manager
						EntityMetadata entMdt = rootObj.getEntityManager().getRootMetadata(rootObj);
						if (entMdt != null)
						{
							if (entMdt.getEntityMode() == reqMode)
							{
								isinreqMode = true;
							}
						}
					}
				}
			}

			if (pjp.getTarget() instanceof DependantObject)
			{
				DependantObject depObj = (DependantObject) pjp.getTarget();
				if (depObj != null)

				{
					if (depObj.getParentRoot().getEntityManager() != null)
					{
						// GEt the Entity Metadata from the Entity Manager
						EntityMetadata entMdt = depObj.getParentRoot().getEntityManager().getDependantMetadata(depObj);
						if (entMdt != null)
						{
							if (entMdt.getEntityMode() == reqMode)
							{
								isinreqMode = true;
							}
						}
					}
				}
			}

		}

		return isinreqMode;
	}

	/********************************************************************************************************************
	 **************************************** POINTCUT DEFINITIONS SECTION ********************************************
	 ********************************************************************************************************************/

	/**
	 * Pointcut - Boolean Return Param and Lock Method without any Params
	 */
	@Pointcut("execution(public boolean * .lock())")
	public void lockmethod()
	{

	}

	/**
	 * Pointcut - Boolean Return Param and Delete Method without any Params
	 */
	@Pointcut("execution(public boolean * .delete())")
	public void deletemethod()
	{

	}

	/**
	 * Pointcut - Boolean Return Param and isLocked Method without any Params
	 */
	@Pointcut("execution(public boolean * .isLocked())")
	public void islockedmethod()
	{

	}

	/**
	 * Pointcut - Boolean Return Param and isChangeable Method without any Params
	 */
	@Pointcut("execution(public boolean * .isChangebale())")
	public void isChangeablemethod()
	{

	}

	/**
	 * Pointcut - Boolean Return Param and switchtoChangeMode Method without any Params
	 */
	@Pointcut("execution(public boolean * .switchtoChangeMode())")
	public void switchtoChangeModemethod()
	{

	}

	/**
	 * Pointcut - String getlockDetails
	 **/
	@Pointcut("execution(public String * .getCurrentLockDetails())")
	public void lockdetails()
	{

	}

	/**
	 * Pointcut - Target is Root Object
	 */
	@Pointcut("target(modelframework.implementations.RootObject)")
	public void target_Root()
	{

	}

	/**
	 * Pointcut - Target is Root/Dependant Object
	 */
	@Pointcut("target(modelframework.implementations.RootObject) || target(modelframework.implementations.DependantObject)  )")
	public void target_RootDependant()
	{

	}

}
