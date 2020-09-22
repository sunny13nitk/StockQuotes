/**
 * 
 */
package modelframework.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Usually for Fullfilment checks
 * 
 * Before triggering the Commit Process
 * Retuns - boolean function in extended root Object - commit value true by default
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BeforeCommitValidator
	{
		
	}
