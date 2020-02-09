package com.lucas.waes.diffservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception to be throw when trying to perform a diff from a null direction
 * @author lucas
 *
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "One of the directions for the given id is not registered")
public class DirectionIsNullException extends DiffException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4099114444015603311L;

}
