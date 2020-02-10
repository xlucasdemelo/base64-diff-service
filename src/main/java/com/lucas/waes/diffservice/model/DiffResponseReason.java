package com.lucas.waes.diffservice.model;

/**
 * Enum with the reason of a diff
 * 
 * @author lucas
 *
 */
public enum DiffResponseReason {
	EQUALS,
	NOT_EQUAL_SIZES,
	DIFFERENT_PAYLOADS
}
