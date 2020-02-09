package com.lucas.waes.diffservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception to be throw when the client send a request with invalid base64 encoded data
 * @author lucas
 *
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The payload must be a Base64 encoded string")
public class Base64ValidationException extends DiffException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 239522124447693581L;

}
