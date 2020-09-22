package root.validation.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import root.validation.annotations.ValidEmail;

public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String>
{
	
	private Pattern             pattern;
	private Matcher             matcher;
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	@Override
	public boolean isValid(
	        String email, ConstraintValidatorContext context
	)
	{
		pattern = Pattern.compile(EMAIL_PATTERN);
		if (email == null)
		{
			return false;
		}
		matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
}
