package com.lucas.waes.diffservice.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.lucas.waes.diffservice.exception.DiffException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Entity that will perform a diff based on the offset of two directions: LEFT and RIGHT
 * 
 * @author lucas
 *
 */
@Slf4j
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class DiffOffset implements Diff{
	
	@Id
	private Long id;
	
	private String leftDirection;
	private String rightDirection;
	
	public DiffOffset(Long id) {
		this.id = id;
	}
	
	/**
	 * Logic of the diff based on the offset and length of two directions strings
	 */
	public DiffOffsetResponseDTO performDiff() throws DiffException {
		
		log.info("Calculating offset for id: {} ", this.id);
		final DiffOffsetResponseDTO diffResult = new DiffOffsetResponseDTO(DiffResponseReason.DIFFERENT_PAYLOADS);
		
		Integer offset = null;
        Integer length = 0;
        
        List<Offset> diffOffsets = new ArrayList<Offset>();
        
        for(int i = 0; i <= this.leftDirection.length(); i++){
            if( i < this.leftDirection.length() && this.leftDirection.charAt(i) != this.rightDirection.charAt(i)
            	){
                if(offset == null){
                    offset = i;
                }
                length++;
            } else {
            	if (offset != null) {
            		diffOffsets.add( new Offset(offset, length) );
            	}
                offset = null;
                length = 0;
            }
        }
		
        diffResult.setOffsets(diffOffsets);;
        log.info("Offset list of diff: {} is {}", this.id, diffResult.toString());
		return diffResult;
	}
}
