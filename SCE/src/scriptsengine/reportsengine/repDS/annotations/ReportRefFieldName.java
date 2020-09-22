package scriptsengine.reportsengine.repDS.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to Mark/Associate a Report Data Source Field to corresponding Field Metadata defined in SS.xml in
 * classpath
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ReportRefFieldName
{
	String attrName() default "";

}
