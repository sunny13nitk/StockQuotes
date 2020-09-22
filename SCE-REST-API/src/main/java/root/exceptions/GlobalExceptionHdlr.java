package root.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
		
		if (ex instanceof ScripUploadException)
		{
			errResp.setStatus(HttpStatus.NOT_FOUND.value());
			return new ResponseEntity<ErrorResponsePOJO>(errResp, HttpStatus.NOT_FOUND);
		} else if (ex instanceof java.lang.Exception)
		{
			errResp.setStatus(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<ErrorResponsePOJO>(errResp, HttpStatus.BAD_REQUEST);
		}
		return null;
		
	}
}
