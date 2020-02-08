package com.lucas.waes.diffservice.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiffResponseOffset {
	
	private DiffResponseReason reason;
	
	private List<Offset> Offsets = new ArrayList<Offset>();
	
	public DiffResponseOffset(DiffResponseReason reason) {
		super();
		this.reason = reason;
	}
}
