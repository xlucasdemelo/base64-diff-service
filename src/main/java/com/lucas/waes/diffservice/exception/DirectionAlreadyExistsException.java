package com.lucas.waes.diffservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.lucas.waes.diffservice.util.DiffConstants;

/**
 * Exception to be throw when the client try to override a direction already existent for a Diff object
 * @author lucas
 *
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = DiffConstants.THE_DIRECTION_YOU_ARE_TRYING_TO_INSERT_ALREADY_EXISTS_IN_DIFF)
public class DirectionAlreadyExistsException extends DiffException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8400649714577972844L;

}
