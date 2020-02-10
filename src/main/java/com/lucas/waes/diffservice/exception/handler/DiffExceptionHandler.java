package com.lucas.waes.diffservice.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lucas.waes.diffservice.util.DiffConstants;

/**
 * Class That will handle Exceptions from the application
 * @author lucas
 *
 */
@RestControllerAdvice
public class DiffExceptionHandler {
	
	/**
	 * Handles the exception HttpMessageNotReadableException that will usually happens when the client send a null payload 
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseEntity<String> exception(final HttpMessageNotReadableException e) {
		return new ResponseEntity<String>(DiffConstants.BODY_SHOULD_NOT_BE_NULL, HttpStatus.BAD_REQUEST);
	}
	
}
