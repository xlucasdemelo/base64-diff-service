package com.lucas.waes.diffservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiffOffset {
	
	private Integer offset;
	private Integer length;
	
}
