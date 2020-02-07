package com.lucas.waes.diffservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucas.waes.diffservice.domain.Diff;
import com.lucas.waes.diffservice.domain.DiffOffset;
import com.lucas.waes.diffservice.domain.DiffResponse;
import com.lucas.waes.diffservice.domain.DiffResponseReason;
import com.lucas.waes.diffservice.exception.DiffException;
import com.lucas.waes.diffservice.exception.DiffNotFoundException;
import com.lucas.waes.diffservice.exception.DirectionAlreadyExistsException;
import com.lucas.waes.diffservice.exception.DirectionIsNullException;
import com.lucas.waes.diffservice.repository.DiffRepository;
import com.lucas.waes.diffservice.util.DiffConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DiffServiceImpl implements DiffService{
	
	@Autowired
	private DiffRepository diffRepository;
	
	/**
	 * 
	 */
	public Diff saveDiff(Long id, String payload, String direction) throws DiffException {
		/**
		 * I am assuming the directions of a diff cannot be changed
		 * This method will check if the request is trying to override a direction of a existent diff
		 */
		this.checkDirectionAlreadyExistsInDiff(id, direction);
		
		final Diff diff = createDiffWithDirection(id, payload, direction);
		
		log.info("Saving... {}", diff.toString());
		return this.diffRepository.save(diff);
	}
	
	private Diff createDiffWithDirection(Long id, String payload, String direction) {
		final Diff diff = new Diff(id);
		
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
	private void checkDirectionAlreadyExistsInDiff(Long id, String direction) throws DiffException {
		final Optional<Diff> optionalDiff = this.diffRepository.findById(id);
				
		if (optionalDiff.isPresent()) {
			final Diff diff = optionalDiff.get();
			
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
	public DiffResponse performDiff( Long diffId ) throws DiffException {
		log.info("Performing diff...", diffId);
		final Diff diff = this.diffRepository.findById(diffId).orElseThrow( () -> new DiffNotFoundException() );
		
		//Only will perform the diff if both directions are registered
		this.validateCanPerformDiff(diff);
		
		if (this.leftAndRightEquals(diff)) {
			return new DiffResponse(DiffResponseReason.EQUALS);
		}
		else if (!this.leftAndRightSizeEquals(diff)){
			return new DiffResponse(DiffResponseReason.NOT_EQUAL_SIZES);
		}
		
		return this.calculateOffset(diff);
	}
	
	/**
	 * 
	 * @param diff
	 * @return
	 */
	private boolean leftAndRightEquals(Diff diff) {
		return diff.getRightDirection().equals(diff.getLeftDirection());
	}
	
	/**
	 * 
	 * @param diff
	 * @return
	 */
	private boolean leftAndRightSizeEquals(Diff diff) {
		return diff.getRightDirection().length() == diff.getLeftDirection().length();
	}
	
	/**
	 * 
	 * @param diff
	 * @throws DiffException
	 */
	private void validateCanPerformDiff(Diff diff) throws DiffException {
		if (diff.getLeftDirection() == null || diff.getRightDirection() == null ) {
			log.info("Any of the directions is null, cannot perform the diff");
			throw new DirectionIsNullException();
		}
	}
	
	/**
	 * 
	 * @param diff
	 * @return
	 */
	private DiffResponse calculateOffset(Diff diff) {
		log.info("Calculating offset for id: {} ", diff.getId());
		final DiffResponse diffResult = new DiffResponse(DiffResponseReason.DIFFERENT_PAYLOADS);
		
		Integer offset = null;
        Integer length = 0;
        
        List<DiffOffset> diffOffsets = new ArrayList<DiffOffset>();
        
        for(int i = 0; i < diff.getLeftDirection().length(); i++){
            if(diff.getLeftDirection().charAt(i) != diff.getRightDirection().charAt(i)){
                if(offset == null){
                    offset = i;
                }
                length++;
            } else {
            	if (offset != null) {
            		diffOffsets.add( new DiffOffset(offset, length) );
            	}
                offset = null;
                length = 0;
            }
        }
		
        diffResult.setDiffOffsets(diffOffsets);
        log.info("Offset list of diff: {} is {}", diff.getId(), diffResult.toString());
		return diffResult;
	}
	
}
