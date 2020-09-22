package root.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import root.enums.Enums;

/*
 * Annotation to Mark DAO Operations/Methods that need to be tracked via DBMarker ASpect
 */
@Retention(
    RetentionPolicy.RUNTIME
)
@Target(
    ElementType.METHOD
)
public @interface DBMark
{
	Enums.dbOperation dbOperation() default Enums.dbOperation.None;
	
	String msg() default "";
	
}
