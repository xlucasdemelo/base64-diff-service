package com.lucas.waes.diffservice.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity that will store two directions to be diff-ed
 * 
 * @author lucas
 *
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DiffOffset {
	
	@Id
	private Long id;
	
	private String leftDirection;
	private String rightDirection;
	
	public DiffOffset(Long id) {
		this.id = id;
	}
	
}
