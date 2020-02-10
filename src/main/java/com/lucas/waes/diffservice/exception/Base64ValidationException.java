package com.lucas.waes.diffservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.lucas.waes.diffservice.util.DiffConstants;

/**
 * Exception to be throw when the client send a request with invalid base64 encoded data
 * @author lucas
 *
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = DiffConstants.THE_PAYLOAD_MUST_BE_A_BASE_64_ENCODED_STRING)
public class Base64ValidationException extends DiffException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 239522124447693581L;

}
