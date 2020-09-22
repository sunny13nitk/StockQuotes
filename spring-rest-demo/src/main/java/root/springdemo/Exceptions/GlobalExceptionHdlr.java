package root.springdemo.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHdlr
{
	@ExceptionHandler
	public ResponseEntity<ErrorResponsePOJO> handleException(
	        Exception ex
	)
	{
		ErrorResponsePOJO errResp = new ErrorResponsePOJO();
		
		errResp.setMessage(ex.getMessage());
		errResp.setTimeStamp(System.currentTimeMillis());
		
		if (ex.getClass().getName() == StudentException.class.getName())
		{
			errResp.setStatus(HttpStatus.NOT_FOUND.value());
			return new ResponseEntity<ErrorResponsePOJO>(errResp, HttpStatus.NOT_FOUND);
		} else if (ex.getClass().getName() == MethodArgumentTypeMismatchException.class.getName())
		{
			errResp.setStatus(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<ErrorResponsePOJO>(errResp, HttpStatus.BAD_REQUEST);
		}
		return null;
		
	}
}
