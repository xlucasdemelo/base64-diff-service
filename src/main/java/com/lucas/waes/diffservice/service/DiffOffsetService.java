package com.lucas.waes.diffservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucas.waes.diffservice.exception.Base64ValidationException;
import com.lucas.waes.diffservice.exception.DiffException;
import com.lucas.waes.diffservice.exception.DiffNotFoundException;
import com.lucas.waes.diffservice.exception.DirectionAlreadyExistsException;
import com.lucas.waes.diffservice.exception.DirectionIsNullException;
import com.lucas.waes.diffservice.model.DiffOffset;
import com.lucas.waes.diffservice.model.DiffOffsetResponseDTO;
import com.lucas.waes.diffservice.model.DiffResponseReason;
import com.lucas.waes.diffservice.model.Direction;
import com.lucas.waes.diffservice.model.Offset;
import com.lucas.waes.diffservice.repository.DiffRepository;
import com.lucas.waes.diffservice.util.DiffConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link DiffService}
 * 
 * Class that held the business logic of DiffOffset methods
 * 
 * @author lucas
 *
 */
@Slf4j
@Service
public class DiffOffsetService implements DiffService{
	
	@Autowired
	private DiffRepository diffRepository;
	
	/**
	 * Method that will save a Diff data accordling to specified direction
	 */
	public DiffOffset saveDiff(Long id, String payload, Direction direction) throws DiffException {
		this.checkStringIsBase64(payload);
		final Optional<DiffOffset> dbDiff = this.diffRepository.findById(id);

		/**
		 * I am assuming the directions of a diff cannot be changed.
		 * This method will check if the request is trying to override a direction of a existent diff
		 */
		this.checkDirectionAlreadyExistsInDiff(dbDiff, direction);
		
		final DiffOffset diff = createDiffWithDirection(dbDiff, id, payload, direction);
		
		log.info(DiffConstants.SAVING_DIFF, diff.toString());
		
		return this.diffRepository.save(diff);
	}
	
	/**
	 * This method checks if the payload is a Base 64 valid data 
	 * 
	 * @param payload
	 * @throws DiffException
	 */
	private void checkStringIsBase64(String payload) throws DiffException {
		if (!Base64.isBase64(payload)){
			log.info(DiffConstants.INVALID_BASE64_PAYLOAD);
			throw new Base64ValidationException();
		}
	}
	
	/**
	 * This method creates a Diff object for the specified direction 
	 * 
	 * @param optionalDiff
	 * @param id
	 * @param payload
	 * @param direction
	 * @return
	 */
	private DiffOffset createDiffWithDirection(Optional<DiffOffset> optionalDiff, Long id, String payload, Direction direction) {
		DiffOffset diff = new DiffOffset(id);
		
		if (optionalDiff.isPresent()) {
			diff = optionalDiff.get();
		}
		
		if (direction.equals(Direction.LEFT)) {
			diff.setLeftDirection(payload);
		}
		else if (direction.equals(Direction.RIGHT)) {
			diff.setRightDirection(payload);
		}
		
		return diff;
	}
	
	/**
	 * I am assuming the directions of a diff cannot be changed.
     * This method will check if the request is trying to override a direction of a existent diff
	 * 
	 * @param optionalDiff
	 * @param direction
	 * @throws DiffException
	 */
	private void checkDirectionAlreadyExistsInDiff(Optional<DiffOffset> optionalDiff, Direction direction) throws DiffException {
		if (optionalDiff.isPresent()) {
			final DiffOffset diff = optionalDiff.get();
			
			if ( (direction.equals(Direction.LEFT) && diff.getLeftDirection() != null)
					|| (direction.equals(Direction.RIGHT) && diff.getRightDirection() != null) )
			{
				log.info(DiffConstants.CANNOT_OVERRIDE_A_DIRECTION);
				throw new DirectionAlreadyExistsException();
			}
		}
	}
	
	/**
	 * This method will check the differences of LEFT and RIGHT directions
	 * 
	 */
	public DiffOffsetResponseDTO performDiff( Long diffId ) throws DiffException {
		log.info(DiffConstants.PERFORMING_DIFF);
		
		final DiffOffset diff = this.diffRepository.findById(diffId).orElseThrow( () -> new DiffNotFoundException() );
		
		//Only will perform the diff if both directions are registered
		this.validateCanPerformDiff(diff);
		
		if (this.leftAndRightEquals(diff)) {
			return new DiffOffsetResponseDTO(DiffResponseReason.EQUALS);
		}
		else if (!this.leftAndRightSizeEquals(diff)){
			return new DiffOffsetResponseDTO(DiffResponseReason.NOT_EQUAL_SIZES);
		}
		
		return this.calculateOffsets(diff);
	}
	
	/**
	 * Check if LEFT and RIGHT directions of a Diff object are equals
	 * 
	 * @param diff
	 * @return
	 */
	private boolean leftAndRightEquals(DiffOffset diff) {
		return diff.getRightDirection().equals(diff.getLeftDirection());
	}
	
	/**
	 * Check if left and right are of equal size
	 * 
	 * @param diff
	 * @return
	 */
	private boolean leftAndRightSizeEquals(DiffOffset diff) {
		return diff.getRightDirection().length() == diff.getLeftDirection().length();
	}
	
	/**
	 * Only will perform the diff if both directions are set
	 * 
	 * @param diff
	 * @throws DiffException
	 */
	private void validateCanPerformDiff(DiffOffset diff) throws DiffException {
		if (diff.getLeftDirection() == null || diff.getRightDirection() == null ) {
			log.info(DiffConstants.ANY_OF_THE_DIRECTIONS_IS_NULL);
			throw new DirectionIsNullException();
		}
	}
	
	/**
	 * Logic of the diff based on the offset and length of two directions strings;
	 * 
	 * The Diff will be on the characters of the Base64 encoded string, there will be no decode.
	 */
	private DiffOffsetResponseDTO calculateOffsets(DiffOffset diff) throws DiffException {
		
		log.info("Calculating offset for id: {} ", diff.getId());
		final DiffOffsetResponseDTO diffResult = new DiffOffsetResponseDTO(DiffResponseReason.DIFFERENT_PAYLOADS);
		
		Integer mismatchOffset = null;
        Integer length = 0;
        
        List<Offset> diffOffsets = new ArrayList<Offset>();
        
        for(int i = 0; i <= diff.getLeftDirection().length(); i++){
            if( i < diff.getLeftDirection().length() && diff.getLeftDirection().charAt(i) != diff.getRightDirection().charAt(i)
            	){
                if(mismatchOffset == null){
                    mismatchOffset = i;
                }
                length++;
            } else {
            	if (mismatchOffset != null) {
            		diffOffsets.add( new Offset(mismatchOffset, length) );
            	}
                mismatchOffset = null;
                length = 0;
            }
        }
		
        diffResult.setOffsets(diffOffsets);;
        log.info("Offset list of diff is: {}", diff.getId(), diffResult.toString());
		return diffResult;
	}
}
