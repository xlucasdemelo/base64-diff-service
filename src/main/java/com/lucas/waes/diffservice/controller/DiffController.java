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

import com.lucas.waes.diffservice.domain.DiffOffset;
import com.lucas.waes.diffservice.domain.DiffResponseOffset;
import com.lucas.waes.diffservice.exception.DiffException;
import com.lucas.waes.diffservice.service.DiffOffsetService;
import com.lucas.waes.diffservice.util.DiffConstants;

@RestController
@RequestMapping(value = "/v1/diff")
public class DiffController {
	@Autowired
	private DiffOffsetService diffService;
	
	@PostMapping(value = "/{id}/left", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DiffOffset> saveLeft( @PathVariable("id") Long id, @RequestBody(required = true) String payload) throws DiffException{
		MDC.put("diffId", id.toString());
		final DiffOffset diffResult = this.diffService.saveDiff(id, payload, DiffConstants.LEFT);
		return new ResponseEntity<DiffOffset>(diffResult, HttpStatus.OK);
	}
	
	@PostMapping(value= "/{id}/right", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DiffOffset> saveRight( @PathVariable("id") Long id, @RequestBody(required = true) String payload) throws DiffException{
		MDC.put("diffId", id.toString());
		final DiffOffset diffResult = this.diffService.saveDiff(id, payload, DiffConstants.RIGHT);
		return new ResponseEntity<DiffOffset>(diffResult, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DiffResponseOffset> diff(@PathVariable("id") Long id) throws DiffException{
		MDC.put("diffId", id.toString());
		final DiffResponseOffset diffResult = this.diffService.performDiff(id);
		return new ResponseEntity<DiffResponseOffset>(diffResult, HttpStatus.OK);
	}
}	
