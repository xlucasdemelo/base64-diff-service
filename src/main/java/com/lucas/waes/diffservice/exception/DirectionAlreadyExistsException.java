package com.lucas.waes.diffservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The direction you are trying to insert already exists in diff")
public class DirectionAlreadyExistsException extends DiffException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8400649714577972844L;

}
