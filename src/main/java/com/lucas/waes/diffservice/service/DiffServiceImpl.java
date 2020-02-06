package com.lucas.waes.diffservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucas.waes.diffservice.domain.Diff;
import com.lucas.waes.diffservice.domain.DiffOffset;
import com.lucas.waes.diffservice.domain.DiffResponse;
import com.lucas.waes.diffservice.domain.DiffResponseReason;
import com.lucas.waes.diffservice.repository.DiffRepository;

@Service
public class DiffServiceImpl implements DiffService{
	
	@Autowired
	private DiffRepository diffRepository;
	
	/**
	 * 
	 */
	public Diff saveDiff(Diff diff) {
		return this.diffRepository.save(diff);
	}
	
	/**
	 * 
	 */
	public DiffResponse diff( Long diffId ) {
		final Diff diff = this.diffRepository.findById(diffId).get();
		
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
		return diff.getRight().equals(diff.getLeft());
	}
	
	/**
	 * 
	 * @param diff
	 * @return
	 */
	private boolean leftAndRightSizeEquals(Diff diff) {
		return diff.getRight().length() == diff.getLeft().length();
	}
	
	/**
	 * 
	 * @param diff
	 * @return
	 */
	private DiffResponse calculateOffset(Diff diff) {
		final DiffResponse diffResult = new DiffResponse(DiffResponseReason.DIFFERENT_PAYLOADS);
		
		Integer offset = null;
        Integer length = 0;
        
        List<DiffOffset> diffOffsets = new ArrayList<DiffOffset>();
        
        for(int i = 0; i < diff.getLeft().length(); i++){
            if(diff.getLeft().charAt(i) != diff.getRight().charAt(i)){
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
		return diffResult;
	}
	
}
