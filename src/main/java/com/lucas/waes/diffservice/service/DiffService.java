package com.lucas.waes.diffservice.service;

import com.lucas.waes.diffservice.domain.Diff;
import com.lucas.waes.diffservice.domain.DiffResponse;

public interface DiffService {
	public Diff saveDiff(Diff diff);
	public DiffResponse diff( Long diffId );
}
