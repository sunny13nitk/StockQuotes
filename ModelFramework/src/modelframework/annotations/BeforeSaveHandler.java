/**
 * 
 */
package modelframework.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Before the generation of prepared statements
 * e.g some conversions are to be performed on object underlying so that prepared statements substitute
 * accordingly
 * e.g Curreny/Value Conversions
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BeforeSaveHandler
	{
		
	}
