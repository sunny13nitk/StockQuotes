package modelframework.services;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import modelframework.definitions.LockObject;
import modelframework.usermanager.managers.UserManager;

@Service("LockManagerService")
public class LockManagerService
{
	private ArrayList<LockObject>	lockedEntities;

	@Autowired
	private UserManager			userManager;

	/**
	 * @return the lockedEntities
	 */
	public ArrayList<LockObject> getLockedEntities()
	{
		return lockedEntities;
	}

	/**
	 * Initailize blank Locked Entities Collection
	 */
	public LockManagerService()
	{
		this.lockedEntities = new ArrayList<LockObject>();
	}

	public LockObject isalreadyLocked(String objName, Object pkey)
	{
		LockObject lockObj = null;

		if (objName != null && pkey != null)
		{
			try
			{
				lockObj = lockedEntities.stream().filter(x -> x.getObjName().equals(objName)).filter(x -> x.getpKey().equals(pkey)).findFirst()
				          .get();
			}
			catch (NoSuchElementException e)
			{
				// do Nothing
			}

		}

		return lockObj;
	}

	public Boolean obtainLock(String objName, Object pKey)
	{
		Boolean lockobtained = false;

		if (objName != null && pKey != null && userManager != null)
		{
			LockObject lockObj = new LockObject(objName, pKey, userManager.Get_LoggedUser());
			this.lockedEntities.add(lockObj);
			lockobtained = true;
		}

		return lockobtained;
	}

	public Boolean releaseLock(String objName, Object pKey)
	{
		Boolean islockReleased = false;
		LockObject lockObj = null;

		if (objName != null && pKey != null && userManager != null)
		{
			lockObj = isalreadyLocked(objName, pKey);
			if (lockObj != null)
			{
				int oldsize = this.lockedEntities.size();
				this.lockedEntities.remove(lockObj);
				int newsize = this.lockedEntities.size();
				if (oldsize - newsize == 1)
				{
					islockReleased = true;
				}
			}
		}

		return islockReleased;
	}

}
