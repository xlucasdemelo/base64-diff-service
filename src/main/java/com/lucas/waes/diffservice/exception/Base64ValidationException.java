package com.lucas.waes.diffservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The payload must be a Base64 encoded string")
public class Base64ValidationException extends DiffException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 239522124447693581L;

}
