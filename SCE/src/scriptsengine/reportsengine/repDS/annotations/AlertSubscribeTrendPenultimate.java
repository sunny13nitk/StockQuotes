package scriptsengine.reportsengine.repDS.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import scriptsengine.enums.SCEenums;

/**
 * 
 * TO Subscribe for Alerts on Reporting Field(s)
 * Message Variables will be handled from ArrayList DElta itself in aspect implementation using occurances and
 * cooresponding delta values
 * Here Occurance would always be one - penultimate
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AlertDeltaPenultimateContainer.class)
@Target(ElementType.FIELD)
public @interface AlertSubscribeTrendPenultimate
{
	// Here Occurance would always be one - penultimate

	SCEenums.direction TrendDirection() default SCEenums.direction.NONE;

	double valueToCompare() default 0;

	SCEenums.alertMode alertMode() default SCEenums.alertMode.AGAINST;

	String msgID() default "AL_GENERIC_PENULTIMATE";

	boolean isMsgGeneric() default true;
}
