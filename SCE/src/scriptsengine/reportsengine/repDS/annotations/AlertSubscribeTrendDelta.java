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
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AlertDeltaTrendsContainer.class)
@Target(ElementType.FIELD)
public @interface AlertSubscribeTrendDelta
{
	int Occurances() default 2;

	SCEenums.direction TrendDirection() default SCEenums.direction.NONE;

	double valueToCompare() default 0;

	SCEenums.alertMode alertMode() default SCEenums.alertMode.AGAINST;

	String msgID() default "AL_GENERIC_TREND";

	boolean isMsgGeneric() default true;

}
