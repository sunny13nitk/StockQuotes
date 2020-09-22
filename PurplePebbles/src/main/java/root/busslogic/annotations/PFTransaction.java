package root.busslogic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import root.enums.PFTransactionOrigin;

/*
 * Portfolio Transaction Annotation- to be marked on methods where the Portolio Holdings and Subsequent Fundline
 * needs an Updation with propoer Contextual Validation Enforced
 * Handled centrally via an Aspect that uses this Annotation in PointCut Expressions
 */

@Target(
    ElementType.METHOD
)
@Retention(
    RetentionPolicy.RUNTIME
)
public @interface PFTransaction
{
	/*
	 * Attribute Origin Added to leave a scope of Cross Cutting Concern to update Fund Line from Any other Utlity in
	 * future that can be handled in Aspect using Case
	 */
	PFTransactionOrigin origin() default PFTransactionOrigin.Buy_Sell;
}
