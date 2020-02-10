package com.lucas.waes.diffservice.util;

/**
 * Class with the constants used in the application
 * 
 * @author lucas
 *
 */
public final class DiffConstants {
	public static final String DIFF_ID  = "diffId";
	
	//Log messages
	public static final String INVALID_BASE64_PAYLOAD = "Invalid Base64 payload";
	public static final String CANNOT_OVERRIDE_A_DIRECTION = "Cannot override a direction";
	public static final String SAVING_DIFF = "Saving DIFF... {}";
	public static final String PERFORMING_DIFF = "Performing diff...";
	public static final String ANY_OF_THE_DIRECTIONS_IS_NULL = "Any of the directions is null, cannot perform the diff";
	
	//Exception Messages
	public static final String BODY_SHOULD_NOT_BE_NULL = "The body should not be null";
	public static final String THE_PAYLOAD_MUST_BE_A_BASE_64_ENCODED_STRING = "The payload must be a Base64 encoded string";
	public static final String THE_GIVEN_ID_DOES_NOT_MATCH_ANY_DATA_IN_THE_SYSTEM = "The given id does not match any data in the system";
	public static final String THE_DIRECTION_YOU_ARE_TRYING_TO_INSERT_ALREADY_EXISTS_IN_DIFF = "The direction you are trying to insert already exists in diff";
	public static final String ONE_OF_THE_DIRECTIONS_FOR_THE_GIVEN_ID_IS_NOT_REGISTERED = "One of the directions for the given id is not registered";
}
