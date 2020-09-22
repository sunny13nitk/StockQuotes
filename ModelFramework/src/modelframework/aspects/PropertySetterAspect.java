/**
 * 
 */
package modelframework.aspects;

import java.util.NoSuchElementException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import modelframework.definitions.Changed_Property;
import modelframework.definitions.EntityManager;
import modelframework.definitions.EntityMetadata;
import modelframework.definitions.Object_Info;
import modelframework.enums.system.modelEnums.entityMode;
import modelframework.enums.system.modelEnums.objectType;
import modelframework.exceptions.EX_CHANGEMODE_KEYVIOLATION;
import modelframework.exposed.FrameworkManager;
import modelframework.implementations.DependantObject;
import modelframework.implementations.GeneralMessage;
import modelframework.implementations.MessagesFormatter;
import modelframework.implementations.RootObject;

/**
 * Aspect for Property Setting in Root/Dependant Objects to update the Changed Proerties Collection of Entity
 * Metadata- Triggered after Every Set On Every Public Setter that returns void and the Target is Root or
 * Dependant Object Bean
 */

@Component
@Aspect
public class PropertySetterAspect
{

	@Autowired
	private MessagesFormatter	msgFormatter;

	@Autowired
	private EntityManager		entManager;

	@Autowired
	private FrameworkManager		frameworkManager;

	// All Setters that take one argument of type Object i.e. property value to be set and target is (Root or
	// Dependant Object)
	@After("All_Setters() && args(propValue) && (target(modelframework.implementations.RootObject) || target(modelframework.implementations.DependantObject))")
	public void Properties_Setters_Handler(JoinPoint jp, Object propValue) throws Exception
	{
		if (jp != null && propValue != null)
		{
			Object target = jp.getTarget();
			objectType oType = null;

			if (target instanceof RootObject || target instanceof DependantObject)
			{

				if (target instanceof RootObject)
				{
					oType = objectType.ROOT_OBJECT; // for Faster Execution

				}
				else if (target instanceof DependantObject)
				{
					oType = objectType.DEPENDANT_OBJECT; // for Faster Execution

				}
				/**
				 * Get Unique ID for Target Object in Entity Manager
				 */
				String selfID = entManager.getselfID(target, oType);

				if (selfID != null)
				{
					update_EntityManager(selfID, target, propValue, jp);
				}
			}

		}
	}

	@Pointcut("execution(public * set*(*))")
	public void All_Setters()
	{

	}

	private int get_matchingFieldIndex(EntityMetadata entMdt, String prop)
	{
		int idx = -1;
		Changed_Property propFound = null;
		for ( int i = 0; i < (entMdt.getChangedProperties().size()); i++ )
		{
			propFound = entMdt.getChangedProperties().get(i);
			if (propFound.getFieldName().equals(prop))
			{
				idx = i;
				return idx;
			}
		}
		return idx;
	}

	private String get_propName(String methodName)
	{
		String prop = null;
		prop = methodName.substring(3);
		return prop;
	}

	@SuppressWarnings("static-access")
	private Boolean isPropertyPkeyorFkey(String invokerTypeName, String propName)
	{
		Boolean iskey = false;

		// First get the Object Information from ObjectInfoFactory
		Object_Info objInfo = frameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(invokerTypeName);
		if (objInfo != null)
		{
			/**
			 * Pkey check from Object Info
			 */
			if (objInfo.getPKey_Name().equals(propName))
			{
				iskey = true;
				// Return and Exit - no need to check for Foreign Key as Property to set
				return iskey;
			}

			else
			{
				// Now check for foreign key in Dependant Metadata
				if (objInfo.getDep_Metadata() != null)
				{
					if (objInfo.getDep_Metadata().getForeignkeyname().equals(propName))
					{
						iskey = true;
					}
				}
			}

		}

		return iskey;
	}

	private void update_EntityManager(String selfID, Object target, Object propValue, JoinPoint jp) throws Exception
	{

		String msg;
		int idx = 0;
		Changed_Property propFound = null;
		try
		{
			/**
			 * For Performance reasons first filter by Type and then compare the Entity
			 */
			EntityMetadata entMdt = entManager.getMetadataColl().stream().filter(x -> x.getSelfID().equals(selfID)).findFirst().get();
			/**
			 * Only Update Change Properties if Entity is in Create or Change Mode Is particularly
			 * Useful while you are constructing object bean back from resultset as you would not
			 * want to invoke change propertis update while creating object there in refresh mode
			 */
			if (entMdt != null && (entMdt.getEntityMode() == entityMode.CHANGE || entMdt.getEntityMode() == entityMode.CREATE))
			{
				// Determine Property Name
				String propName = get_propName(jp.getSignature().getName());

				/**
				 * It should not be possible to alter the Primary Key or the Foreign Key in Change Mode for Data
				 * consistency
				 * EX_CHANGEMODE_KEYVIOLATION would be thrown in this case
				 */

				if (entMdt.getEntityMode() == entityMode.CHANGE)
				{
					boolean isKey = isPropertyPkeyorFkey(target.getClass().getSimpleName(), propName);
					if (isKey)
					{
						// Throw Exception that the Primary Key or Foreign Key could not be set in Change Mode for
						// an entity
						// Raise Message
						if (msgFormatter != null)
						{
							EX_CHANGEMODE_KEYVIOLATION exKey = new EX_CHANGEMODE_KEYVIOLATION(new Object[]
							{ propName, target.getClass().getSimpleName()
							});

							msg = msgFormatter.generate_message_snippet(exKey);
							throw exKey;
						}

					}
				}

				// Check if Property Already exists in EntityManager for Entity Metadata & get Index
				// to Update in Collection
				if (entMdt.getChangedProperties().size() > 0)
				{
					idx = get_matchingFieldIndex(entMdt, propName);
					if (idx >= 0)
					{
						propFound = entMdt.getChangedProperties().get(idx);
						if (propFound != null)
						{
							Object prevValue = propFound.getValue();
							// Property Found- Change the Value only
							propFound.setValue(propValue);
							// Updated Changed Properties collection in Entity Metadata
							entMdt.getChangedProperties().set(idx, propFound);
							// SUCC_PROPRESET= Property {0} of Entity Type {1} set to Value {2}
							// from Value {3}
							GeneralMessage msgReset = new GeneralMessage("SUCC_PROPRESET", new Object[]
							{ propName, target.getClass().getSimpleName(), propValue, prevValue
							});
							msgFormatter.generate_message_snippet(msgReset);

						}
					}
					else
					{
						// Changed Properties Collection Initialized but property not found
						// Entity Metadata Blank - Just add the Changed_Property
						Changed_Property newChProp = new Changed_Property(propName, propValue.getClass().getSimpleName(), propValue);
						entMdt.getChangedProperties().add(newChProp);
						// SUCC_PROPSET= Property {0} of Entity Type {1} set to Value {2}
						GeneralMessage msgSet = new GeneralMessage("SUCC_PROPSET", new Object[]
						{ propName, target.getClass().getSimpleName(), propValue
						});
						msgFormatter.generate_message_snippet(msgSet);
					}
				}
				else
				{
					// Entity Metadata Blank - Just add the Changed_Property
					Changed_Property newChProp = new Changed_Property(propName, propValue.getClass().getSimpleName(), propValue);
					entMdt.getChangedProperties().add(newChProp);
					// SUCC_PROPSET= Property {0} of Entity Type {1} set to Value {2}
					GeneralMessage msgSet = new GeneralMessage("SUCC_PROPSET", new Object[]
					{ propName, target.getClass().getSimpleName(), propValue
					});
					msgFormatter.generate_message_snippet(msgSet);
				}
			}
		}
		catch (NoSuchElementException NE)
		{
			// Raise Message
			if (msgFormatter != null)
			{
				GeneralMessage msgEntMdtErr = new GeneralMessage("ERR_ENTMDATA", new Object[]
				{ jp.getSignature().getName()
				});
				msg = msgFormatter.generate_message_snippet(msgEntMdtErr);
				throw new Exception(msg);
			}
		}
	}
}
