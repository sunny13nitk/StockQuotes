/**
 * 
 */
package modelframework.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Before SAve Validator Annotation
 * To be added to methods that simply Return Boolean Value for Value of Error as true/false for Root/Model
 * Objects
 * Retunrs- error boolean value false by default
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BeforeSaveValidator
	{
		
	}
