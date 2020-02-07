package com.lucas.waes.diffservice.controller;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	@PostMapping("/{id}/left")
	public ResponseEntity<Diff> saveLeft( @PathVariable("id") Long id, @RequestBody String payload) throws DiffException{
		MDC.put("diffId", id.toString());
		final Diff diffResult = this.diffService.saveDiff(id, payload, DiffConstants.LEFT);
		return new ResponseEntity<Diff>(diffResult, HttpStatus.OK);
	}
	
	@PostMapping("/{id}/right")
	public ResponseEntity<Diff> saveRight( @PathVariable("id") Long id, @RequestBody() String payload) throws DiffException{
		MDC.put("diffId", id.toString());
		final Diff diffResult = this.diffService.saveDiff(id, payload, DiffConstants.RIGHT);
		return new ResponseEntity<Diff>(diffResult, HttpStatus.OK);
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<DiffResponse> diff(@PathVariable("id") Long id) throws DiffException{
		MDC.put("diffId", id.toString());
		final DiffResponse diffResult = this.diffService.performDiff(id);
		return new ResponseEntity<DiffResponse>(diffResult, HttpStatus.OK);
	}
}	
