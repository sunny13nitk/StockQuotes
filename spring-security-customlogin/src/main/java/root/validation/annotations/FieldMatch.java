package root.validation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import root.validation.validators.FieldMatchValidator;

@Target(
    { ElementType.ANNOTATION_TYPE, ElementType.TYPE }
)
@Retention(
    RetentionPolicy.RUNTIME
)
@Constraint(
        validatedBy = FieldMatchValidator.class
)
@Repeatable(
    FieldMatchList.class
)
public @interface FieldMatch
{
	
	String message() default "Fields: do not match! >> ";
	
	Class<?>[] groups() default
	{};
	
	Class<? extends Payload>[] payload() default
	{};
	
	public String field1();
	
	public String field2();
	
}
