package com.dtechideas.restapitesting.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GloablExceptionHandler extends ResponseEntityExceptionHandler {

	/* Handling specific custom exception */
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetail> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest)
	{
		ErrorDetail errorDetail = new ErrorDetail(new Date(),exception.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<ErrorDetail>(errorDetail,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ErrorDetail> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception, WebRequest webRequest)
	{
		ErrorDetail errorDetail = new ErrorDetail(new Date(),exception.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<ErrorDetail>(errorDetail,HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	/* Handling global exception */

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetail> handleGlobalException(Exception exception, WebRequest request) {
		ErrorDetail errorDetail = new ErrorDetail(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetail>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
