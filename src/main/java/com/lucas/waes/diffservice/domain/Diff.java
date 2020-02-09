package com.lucas.waes.diffservice.domain;

import com.lucas.waes.diffservice.exception.DiffException;

/**
 * Contract that will define common methods for Diff classes
 * @author lucas
 *
 */
public interface Diff {
	
	/**
	 * Method that held the logic of diff
	 * 
	 * @return
	 * @throws DiffException
	 */
	public DiffOffsetResponseDTO performDiff() throws DiffException;
	
}
