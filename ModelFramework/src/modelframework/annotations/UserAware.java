/**
 * 
 */
package modelframework.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * User Aware Annotation to be set on Root Objects
 * Implicitly trickled down to Lower Hieracrhy Relations for Root Object
 * Used for Prepared Statement Generation during Save/Query of Root Object
 * Is Aided by UserManager Service to retrive User Details
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface UserAware
	{
		
	}
