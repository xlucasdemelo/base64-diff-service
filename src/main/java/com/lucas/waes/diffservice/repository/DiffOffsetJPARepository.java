package com.lucas.waes.diffservice.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lucas.waes.diffservice.model.DiffOffset;

/**
 * JPA implementation of repository methods for a Diff object 
 * 
 * @author lucas
 *
 */
@Component
public class DiffOffsetJPARepository implements DiffRepository{
	
	@Autowired
	private DiffOffsetRepository diffRepository;
	
	@Override
	public DiffOffset save(DiffOffset diff) {
		return this.diffRepository.save( (DiffOffset) diff);
	}

	@Override
	public Optional<DiffOffset> findById(Long id) {
		return this.diffRepository.findById(id);
	}
	
}
