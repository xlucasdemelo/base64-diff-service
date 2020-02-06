package com.lucas.waes.diffservice.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiffResponse {
	
	private DiffResponseReason reason;
	
	private List<DiffOffset> diffOffsets = new ArrayList<DiffOffset>();
	private Integer length;
	
	public DiffResponse(DiffResponseReason reason) {
		super();
		this.reason = reason;
	}
}
