package com.lucas.waes.diffservice.controller;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucas.waes.diffservice.domain.Diff;
import com.lucas.waes.diffservice.domain.DiffResponse;
import com.lucas.waes.diffservice.exception.DiffException;
import com.lucas.waes.diffservice.service.DiffServiceImpl;
import com.lucas.waes.diffservice.util.DiffConstants;

@RestController
@RequestMapping(value = "/v1/diff")
public class DiffController {
	@Autowired
	private DiffServiceImpl diffService;
	
	@PostMapping(value = "/{id}/left", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Diff> saveLeft( @PathVariable("id") Long id, @RequestBody(required = true) String payload) throws DiffException{
		MDC.put("diffId", id.toString());
		final Diff diffResult = this.diffService.saveDiff(id, payload, DiffConstants.LEFT);
		return new ResponseEntity<Diff>(diffResult, HttpStatus.OK);
	}
	
	@PostMapping(value= "/{id}/right", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Diff> saveRight( @PathVariable("id") Long id, @RequestBody(required = true) String payload) throws DiffException{
		MDC.put("diffId", id.toString());
		final Diff diffResult = this.diffService.saveDiff(id, payload, DiffConstants.RIGHT);
		return new ResponseEntity<Diff>(diffResult, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DiffResponse> diff(@PathVariable("id") Long id) throws DiffException{
		MDC.put("diffId", id.toString());
		final DiffResponse diffResult = this.diffService.performDiff(id);
		return new ResponseEntity<DiffResponse>(diffResult, HttpStatus.OK);
	}
}	
