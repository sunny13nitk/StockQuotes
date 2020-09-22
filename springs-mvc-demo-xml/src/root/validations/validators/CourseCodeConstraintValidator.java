package root.validations.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import root.validations.defn.CourseCode;

public class CourseCodeConstraintValidator implements ConstraintValidator<CourseCode, String>
{
	
	private String coursePrefix;
	private String message;
	
	@Override
	public void initialize(
	        CourseCode theCourseCode
	)
	{
		this.coursePrefix = theCourseCode.value();
		this.message      = theCourseCode.message();
	}
	
	@Override
	public boolean isValid(
	        String theCourseCode, ConstraintValidatorContext validCtxt
	)
	{
		
		boolean result = true;
		if (theCourseCode != null)
		{
			result = theCourseCode.startsWith(coursePrefix);
			
		}
		
		/*
		 * Populate Custom Message on Failure of Validation Constraint disabling the default One
		 */
		if (result == false)
		{
			validCtxt.disableDefaultConstraintViolation();
			validCtxt.buildConstraintViolationWithTemplate(this.message + theCourseCode).addConstraintViolation();
			
		}
		
		// TODO Auto-generated method stub
		return result;
	}
	
}
