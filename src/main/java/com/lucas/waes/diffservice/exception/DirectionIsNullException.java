package com.lucas.waes.diffservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.lucas.waes.diffservice.util.DiffConstants;

/**
 * Exception to be throw when trying to perform a diff from a null direction
 * @author lucas
 *
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = DiffConstants.ONE_OF_THE_DIRECTIONS_FOR_THE_GIVEN_ID_IS_NOT_REGISTERED)
public class DirectionIsNullException extends DiffException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4099114444015603311L;

}
