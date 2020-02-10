package com.lucas.waes.diffservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.lucas.waes.diffservice.util.DiffConstants;

/**
 * Exception to be throw when a diff object does not exist in the database
 * 
 * @author lucas
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = DiffConstants.THE_GIVEN_ID_DOES_NOT_MATCH_ANY_DATA_IN_THE_SYSTEM)
public class DiffNotFoundException extends DiffException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -864616067175567243L;

}
