package com.lucas.waes.diffservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The given id does not match any data in the system")
public class DiffNotFoundException extends DiffException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -864616067175567243L;

}
