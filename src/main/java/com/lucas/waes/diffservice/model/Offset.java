package com.lucas.waes.diffservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Object that will represent the offset differences between two DIFF directions
 * @author lucas
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Offset {
	
	private Integer offset;
	private Integer length;
	
}
