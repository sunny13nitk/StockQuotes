package root.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

import root.validation.annotations.FieldMatch;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object>
{
	
	private String field1;
	private String field2;
	private String message;
	
	/*
	 * Initialize the Validator Attributes as per Constraint Data before starting the actual Validation
	 */
	@Override
	public void initialize(
	        FieldMatch constraintAnnotation
	)
	{
		field1  = constraintAnnotation.field1();
		field2  = constraintAnnotation.field2();
		message = constraintAnnotation.message();
	}
	
	@Override
	public boolean isValid(
	        Object value, ConstraintValidatorContext context
	)
	{
		
		boolean valid = true;
		
		final Object firstObj  = new BeanWrapperImpl(value).getPropertyValue(field1);
		final Object secondObj = new BeanWrapperImpl(value).getPropertyValue(field2);
		
		valid = firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
		if (!valid)
		{
			context.buildConstraintViolationWithTemplate(message + firstObj + "," + secondObj).addPropertyNode(field1)
			        .addConstraintViolation().disableDefaultConstraintViolation();
		}
		
		return valid;
	}
	
}
