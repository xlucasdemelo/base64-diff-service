package com.lucas.waes.diffservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucas.waes.diffservice.domain.DiffOffset;
import com.lucas.waes.diffservice.domain.DiffResponseOffset;
import com.lucas.waes.diffservice.domain.DiffResponseReason;
import com.lucas.waes.diffservice.exception.DiffException;
import com.lucas.waes.diffservice.exception.DiffNotFoundException;
import com.lucas.waes.diffservice.exception.DirectionAlreadyExistsException;
import com.lucas.waes.diffservice.exception.DirectionIsNullException;
import com.lucas.waes.diffservice.repository.DiffOffsetRepository;
import com.lucas.waes.diffservice.util.DiffConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DiffOffsetService{
	
	@Autowired
	private DiffOffsetRepository diffRepository;
	
	/**
	 * 
	 */
	public DiffOffset saveDiff(Long id, String payload, String direction) throws DiffException {
		
		final Optional<DiffOffset> dbDiff = this.diffRepository.findById(id);
		
		/**
		 * I am assuming the directions of a diff cannot be changed
		 * This method will check if the request is trying to override a direction of a existent diff
		 */
		this.checkDirectionAlreadyExistsInDiff(dbDiff, id, direction);
		
		final DiffOffset diff = createDiffWithDirection(dbDiff, id, payload, direction);
		
		log.info("Saving... {}", diff.toString());
		return this.diffRepository.save(diff);
	}
	
	private DiffOffset createDiffWithDirection(Optional<DiffOffset> optionalDiff, Long id, String payload, String direction) {
		DiffOffset diff = new DiffOffset(id);
		
		if (optionalDiff.isPresent()) {
			diff = optionalDiff.get();
		}
		
		if (direction == DiffConstants.LEFT) {
			diff.setLeftDirection(payload);
		}
		else if (direction == DiffConstants.RIGHT) {
			diff.setRightDirection(payload);
		}
		
		return diff;
	}
	
	/**
	 * @param diff
	 * @param direction
	 */
	private void checkDirectionAlreadyExistsInDiff(Optional<DiffOffset> optionalDiff, Long id, String direction) throws DiffException {
		if (optionalDiff.isPresent()) {
			final DiffOffset diff = optionalDiff.get();
			
			if (direction == DiffConstants.LEFT) {
				if (diff.getLeftDirection() != null ) {
					log.info("Cannot override a direction");
					throw new DirectionAlreadyExistsException();
				}
			}
			else if (direction == DiffConstants.RIGHT) {
				if (diff.getRightDirection() != null ) {
					log.info("Cannot override a direction");
					throw new DirectionAlreadyExistsException();
				}
			}
		}
	}
	
	/**
	 * @throws DiffException 
	 * @throws DiffNotFoundException 
	 * 
	 */
	public DiffResponseOffset performDiff( Long diffId ) throws DiffException {
		log.info("Performing diff...", diffId);
		final DiffOffset diff = this.diffRepository.findById(diffId).orElseThrow( () -> new DiffNotFoundException() );
		
		//Only will perform the diff if both directions are registered
		this.validateCanPerformDiff(diff);
		
		if (this.leftAndRightEquals(diff)) {
			return new DiffResponseOffset(DiffResponseReason.EQUALS);
		}
		else if (!this.leftAndRightSizeEquals(diff)){
			return new DiffResponseOffset(DiffResponseReason.NOT_EQUAL_SIZES);
		}
		
		return diff.performDiff();
	}
	
	/**
	 * 
	 * @param diff
	 * @return
	 */
	private boolean leftAndRightEquals(DiffOffset diff) {
		return diff.getRightDirection().equals(diff.getLeftDirection());
	}
	
	/**
	 * 
	 * @param diff
	 * @return
	 */
	private boolean leftAndRightSizeEquals(DiffOffset diff) {
		return diff.getRightDirection().length() == diff.getLeftDirection().length();
	}
	
	/**
	 * 
	 * @param diff
	 * @throws DiffException
	 */
	private void validateCanPerformDiff(DiffOffset diff) throws DiffException {
		if (diff.getLeftDirection() == null || diff.getRightDirection() == null ) {
			log.info("Any of the directions is null, cannot perform the diff");
			throw new DirectionIsNullException();
		}
	}
	
}
