package root.validation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import root.validation.validators.ValidEmailValidator;

@Target(
    { ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE }
)
@Retention(
    RetentionPolicy.RUNTIME
)
@Constraint(
        validatedBy = ValidEmailValidator.class
)
public @interface ValidEmail
{
	public String message() default "Invalid Email!";
	
	Class<?>[] groups() default
	{};
	
	Class<? extends Payload>[] payload() default
	{};
}
