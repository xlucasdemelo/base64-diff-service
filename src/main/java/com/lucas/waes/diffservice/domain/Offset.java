package com.lucas.waes.diffservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Object that will represent the offset differences between two DIFF directions
 * @author lucas
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Offset {
	
	private Integer offset;
	private Integer length;
	
}
