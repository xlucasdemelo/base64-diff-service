package com.lucas.waes.diffservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucas.waes.diffservice.domain.Diff;
import com.lucas.waes.diffservice.domain.DiffResponse;
import com.lucas.waes.diffservice.service.DiffServiceImpl;

@RestController
@RequestMapping(value = "/v1/diff")
public class DiffController {
	@Autowired
	private DiffServiceImpl diffService;
	
	@GetMapping("/{id}/left")
	public ResponseEntity<Diff> saveLeft( @PathVariable("id") Long id, String payload){
		return null;
	}
	
	@GetMapping("/{id}/left")
	public ResponseEntity<Diff> saveRight( @PathVariable("id") Long id, String payload){
		return null;
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<DiffResponse> diff(@PathVariable("id") Long id){
		final DiffResponse diffResult = this.diffService.diff(id);
		return new ResponseEntity<DiffResponse>(diffResult, HttpStatus.OK);
	}
}	
