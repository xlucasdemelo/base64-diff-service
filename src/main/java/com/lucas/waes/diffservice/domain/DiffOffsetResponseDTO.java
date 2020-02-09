package com.lucas.waes.diffservice.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO that will return the result of diff operation to the client
 * @author lucas
 *
 */
@Data
@AllArgsConstructor
public class DiffOffsetResponseDTO {
	
	private DiffResponseReason reason;
	
	private List<Offset> Offsets = new ArrayList<Offset>();
	
	public DiffOffsetResponseDTO(DiffResponseReason reason) {
		super();
		this.reason = reason;
	}
}
