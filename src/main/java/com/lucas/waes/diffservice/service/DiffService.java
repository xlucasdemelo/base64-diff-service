package com.lucas.waes.diffservice.service;

import com.lucas.waes.diffservice.domain.Diff;
import com.lucas.waes.diffservice.domain.DiffOffsetResponseDTO;
import com.lucas.waes.diffservice.domain.Direction;
import com.lucas.waes.diffservice.exception.DiffException;

/**
 * Interface that defines the contract for DiffService Methods
 * @author lucas
 *
 */
public interface DiffService {
	
	/**
	 * This method is responsible to save a Diff object data to the specified direction
	 * 
	 * @param id  - Unique identifier of the Diff object
	 * @param payload - Base64 encoded binary data
	 * @param direction - RIGHT or LEFT
	 * @return {@link Diff}- Diff object created
	 * @throws DiffException
	 */
	public Diff saveDiff(Long id, String payload, Direction direction) throws DiffException;
	
	/**
	 * This method is responsible to check differences between LEFT and RIGHT direction of the diff with provided id 
	 * 
	 * @param id - Unique identifier of the Diff
	 * @return {@link DiffOffsetResponseDTO}
	 * @throws DiffException
	 */
	public DiffOffsetResponseDTO performDiff(Long id) throws DiffException;
}
