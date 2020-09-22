package root.busslogic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import root.enums.FundLineOrigins;

/*
 * Fund Line Balance Annotation- to be marked on methods where the Fund Line Balance needs an Updation
 * Handled centrally via an Aspect that uses this Annotation in PointCut Expressions
 */
@Target(
    ElementType.METHOD
)
@Retention(
    RetentionPolicy.RUNTIME
)
public @interface FundLineBalance
{
	/*
	 * Attribute Origin Added to leave a scope of Cross Cutting Concern to update Fund Line from Any other Utlity in
	 * future that can be handled in Aspect using Case
	 */
	FundLineOrigins origin() default FundLineOrigins.FundLine;
}
