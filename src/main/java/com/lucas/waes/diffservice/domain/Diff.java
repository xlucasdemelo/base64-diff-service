package com.lucas.waes.diffservice.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Diff {
	
	@Id
	private Long id;
	
	private String leftDirection;
	private String rightDirection;
	
	public Diff(Long id) {
		this.id = id;
	}
}
