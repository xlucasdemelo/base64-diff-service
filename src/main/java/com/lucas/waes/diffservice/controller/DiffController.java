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

import com.lucas.waes.diffservice.exception.DiffException;
import com.lucas.waes.diffservice.model.DiffOffset;
import com.lucas.waes.diffservice.model.DiffOffsetResponseDTO;
import com.lucas.waes.diffservice.model.Direction;
import com.lucas.waes.diffservice.service.DiffService;
import com.lucas.waes.diffservice.util.DiffConstants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This class is the controller that exposes public endpoints 
 * 		in order to the application could be consumed
 * 
 * @author lucas
 *
 */
@RestController
@RequestMapping(value = "/v1/diff")
public class DiffController {
	
	@Autowired
	private DiffService diffService; 
	
	/**
	 * 
	 * @param id
	 * @param payload
	 * @return
	 * @throws DiffException
	 */
	@ApiOperation("Stores the LEFT direction to Diff with provided id")
	@ApiResponses({
        @ApiResponse(code = 200, message = "It will return the saved diff entity"),
        @ApiResponse(code = 400, message = "It will return a BAD_REQUEST if the payload is not a valid Base64 encoded string and if trying to update a already existent direction")
	})
	@PostMapping(value = "/{id}/left", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DiffOffset> saveLeft( @PathVariable("id") Long id, @RequestBody(required = true) String payload) throws DiffException{
		
		/**
		 * MDC.put will inject the diff id into the main thread 
		 * so it will be available to be logged at the LOG_PATTERN defined in log4j2.xml
		 */
		MDC.put(DiffConstants.DIFF_ID, id.toString());
		
		final DiffOffset diffResult = this.diffService.saveDiff( id, payload, Direction.LEFT);
		return new ResponseEntity<DiffOffset>(diffResult, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @param payload
	 * @return
	 * @throws DiffException
	 */
	@ApiOperation("Stores the RIGHT direction to Diff with provided id")
	@ApiResponses({
        @ApiResponse(code = 200, message = "It will return the saved diff entity"),
        @ApiResponse(code = 400, message = "It will return a BAD_REQUEST if the payload is not a valid Base64 encoded string and if trying to update a already existent direction")
	})
	@PostMapping(value= "/{id}/right", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DiffOffset> saveRight( @PathVariable("id") Long id, @RequestBody(required = true) String payload) throws DiffException{
		
		/**
		 * MDC.put will inject the diff id into the main thread 
		 * so it will be available to be logged at the LOG_PATTERN defined in log4j2.xml
		 */
		MDC.put(DiffConstants.DIFF_ID, id.toString());
		
		final DiffOffset diffResult = this.diffService.saveDiff(id, payload, Direction.RIGHT);
		return new ResponseEntity<DiffOffset>(diffResult, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DiffException
	 */
	@ApiOperation("Perform a diff of LEFT and RIGHT directions for the diff with the provided ID")
	@ApiResponses({
        @ApiResponse(code = 200, message = "It will return the diff comparison"),
        @ApiResponse(code = 404, message = "It will return a NOT_FOUND if a DIFF with the provided id does not exist"),
        @ApiResponse(code = 400, message = "It will return a BAD_REQUEST if one of the directions is null")
	})
	@GetMapping("/{id}")
	public ResponseEntity<DiffOffsetResponseDTO> diff(@PathVariable("id") Long id) throws DiffException{
		
		/**
		 * MDC.put will inject the diff id into the main thread 
		 * so it will be available to be logged at the LOG_PATTERN defined in log4j2.xml
		 */
		MDC.put(DiffConstants.DIFF_ID, id.toString());
		
		final DiffOffsetResponseDTO diffResult = this.diffService.performDiff(id);
		return new ResponseEntity<DiffOffsetResponseDTO>(diffResult, HttpStatus.OK);
	}
}	
