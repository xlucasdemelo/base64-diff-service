package com.lucas.waes.diffservice.repository;

import java.util.Optional;

import com.lucas.waes.diffservice.model.DiffOffset;

/**
 * Interface that will define the contract with the methods for Repository access for a DiffObject
 *  
 * @author lucas
 *
 */
public interface DiffRepository {
	public DiffOffset save(DiffOffset diff);
	public Optional<DiffOffset> findById(Long id);
}
