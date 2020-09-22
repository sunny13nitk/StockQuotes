package modelframework.aspects;

import java.util.ArrayList;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import modelframework.JAXB.definitions.models.Model;
import modelframework.JAXB.definitions.models.Model_Assembly;
import modelframework.JAXB.definitions.objschemas.ObjectSchema;
import modelframework.JAXB.implementations.Models_JAXB;
import modelframework.JAXB.implementations.ObjectSchemas_JAXB;
import modelframework.definitions.Object_Info;
import modelframework.exposed.FrameworkManager;
import modelframework.interfaces.IBussFxn;

/**
 * Business Function Aspect - Triggered during call of activateBussFxn
 * on any of the Business function bean which implements an interface
 * IBussFxn
 */
@Aspect
@Component
public class BussFxnAspect
{

	/**
	 * Around Advice for an Object that Implements business fxn Interface (Buss. Fxn. Bean) and a call is made to that
	 * object's activate
	 * business function method
	 * 
	 * @throws Exception
	 */

	@Around("BussFxnInterface() && activateBussFxnMethod()")
	public boolean isBussFxnActivated(ProceedingJoinPoint pjp) throws Exception
	{
		boolean fxnActivated = false;
		if (pjp != null)
		{
			if (pjp.getTarget() instanceof IBussFxn)
			{
				IBussFxn BussFxn = (IBussFxn) pjp.getTarget();
				/**
				 * Determine if Model and Schema Locations are maintianed on business fxn. to activate
				 */

				if (!(StringUtils.isEmpty(BussFxn.getSchemaLoc())) && (!StringUtils.isEmpty(BussFxn.getModelLoc())))
				{
					/**
					 * Load Schemas
					 */
					ObjectSchemas_JAXB Objects_JAXB = new ObjectSchemas_JAXB();
					ArrayList<ObjectSchema> objSchemas = Objects_JAXB.Load_XML_to_ObjectsforBussFxn(BussFxn.getSchemaLoc());
					if (objSchemas != null)
					{
						if (objSchemas.size() > 0)
						{
							for ( ObjectSchema objectSchema : objSchemas )
							{
								FrameworkManager.getObjectSchemaFactory().getObjectSchemas().add(objectSchema);
							}
						}
					}

					/**
					 * Load Object Info Factory From Model XML of Business Function
					 */
					Models_JAXB models_JAXB = new Models_JAXB();
					Model bussFxmModel = (Model) models_JAXB.Load_Model_forBussFxn(BussFxn.getModelLoc());
					if (bussFxmModel != null)
					{

						// Looping Over Individual Assemblies
						for ( Model_Assembly assm : bussFxmModel.getAssemblies() )
						{
							// Get Object Info for Each Assembly
							Object_Info new_obj_info = new Object_Info(assm.getAssembly());
							FrameworkManager.getObjectsInfoFactory().Add_ObjectInfo_ToModel(new_obj_info);
						}

					}
				}

			}
		}

		return fxnActivated;

	}

	/********************************************************************************************************************
	 **************************************** POINTCUT DEFINITIONS SECTION ********************************************
	 ********************************************************************************************************************/

	/**
	 * Pointcut for Implementing Interface 'IBussFxn'
	 */
	@Pointcut("target(modelframework.interfaces.IBussFxn)")
	public void BussFxnInterface()
	{

	}

	/**
	 * PointCut for activateBussFxn
	 */
	@Pointcut("execution(public boolean *.activateBussFxn())")
	public void activateBussFxnMethod()
	{

	}
}
