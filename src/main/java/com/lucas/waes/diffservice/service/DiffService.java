package com.lucas.waes.diffservice.service;

import com.lucas.waes.diffservice.domain.Diff;
import com.lucas.waes.diffservice.domain.DiffResponse;
import com.lucas.waes.diffservice.exception.DiffException;

public interface DiffService {
	public Diff saveDiff(Diff diff, String direction) throws DiffException;
	public DiffResponse performDiff( Long diffId ) throws DiffException;
}
