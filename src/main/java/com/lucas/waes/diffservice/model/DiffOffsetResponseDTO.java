package com.lucas.waes.diffservice.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO that will return the result of diff operation to the client
 * @author lucas
 *
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class DiffOffsetResponseDTO {
	
	private DiffResponseReason reason;
	
	private List<Offset> Offsets = new ArrayList<Offset>();
	
	public DiffOffsetResponseDTO(DiffResponseReason reason) {
		super();
		this.reason = reason;
	}
}
