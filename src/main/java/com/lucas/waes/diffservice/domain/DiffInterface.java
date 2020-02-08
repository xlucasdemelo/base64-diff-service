package com.lucas.waes.diffservice.domain;

import com.lucas.waes.diffservice.exception.DiffException;

public interface DiffInterface {
	
	public DiffResponseOffset performDiff() throws DiffException;
	
}
