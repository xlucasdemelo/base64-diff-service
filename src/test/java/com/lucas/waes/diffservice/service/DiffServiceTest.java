package com.lucas.waes.diffservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.lucas.waes.diffservice.exception.Base64ValidationException;
import com.lucas.waes.diffservice.exception.DiffException;
import com.lucas.waes.diffservice.exception.DirectionAlreadyExistsException;
import com.lucas.waes.diffservice.exception.DirectionIsNullException;
import com.lucas.waes.diffservice.model.DiffOffset;
import com.lucas.waes.diffservice.model.DiffOffsetResponseDTO;
import com.lucas.waes.diffservice.model.DiffResponseReason;
import com.lucas.waes.diffservice.model.Direction;
import com.lucas.waes.diffservice.repository.DiffRepository;

/**
 * Class that will test the logic of Service layer
 * 
 * @author lucas
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DiffServiceTest {

	@Spy
	@InjectMocks
	private DiffOffsetService diffService;
	
	@Mock
	private DiffRepository diffRepository;
	
	@Test
	public void testSaveNewDiffOnlyWithLeft() throws DiffException {
		final DiffOffset diff = new DiffOffset(100L, "abc", null);

		Mockito.when(diffRepository.save(any(DiffOffset.class))).thenReturn(diff);
		this.diffService.saveDiff(100L, "abc", Direction.LEFT);

		assertThat(diff, notNullValue());
		assertThat(diff.getLeftDirection(), notNullValue());
		assertThat(diff.getRightDirection(), is(IsNull.nullValue()));
	}
	
	@Test(expected = DirectionAlreadyExistsException.class)
	public void testOverrideLeftOfExistentDiff_ShouldThrowException() throws DiffException {
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new DiffOffset(100L, "abcde", null )));
		this.diffService.saveDiff(100L, "abc", Direction.LEFT);
	}
	
	@Test(expected = DirectionAlreadyExistsException.class)
	public void testOverrideRightOfExistentDiff_ShouldThrowException() throws DiffException {
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new DiffOffset(100L, null, "abcde")));
		this.diffService.saveDiff(100L, "abc", Direction.RIGHT);
	}
	
	@Test
	public void testSaveRightOnExistentDiff() throws DiffException {
		Mockito.when(diffRepository.save(any(DiffOffset.class))).thenReturn(new DiffOffset(100L, "abc", "abc"));
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new DiffOffset(100L, "abcde", null)));
		final DiffOffset diff = (DiffOffset) this.diffService.saveDiff(100L, "abc", Direction.RIGHT);
		
		assertThat(diff, notNullValue());
		assertThat(diff.getRightDirection(), notNullValue());
		assertThat(diff.getLeftDirection(), notNullValue());
	}
	
	@Test(expected = Base64ValidationException.class)
	public void testSaveInvalidBase64() throws DiffException {
		this.diffService.saveDiff(100L, "	的", Direction.RIGHT);
	}
	
	@Test
	public void testSaveNewDiffOnlyWithRight() throws DiffException {
		final DiffOffset diff = new DiffOffset(100L, null, "abc");

		Mockito.when(diffRepository.save(any(DiffOffset.class))).thenReturn(diff);
		this.diffService.saveDiff(100L, "abc", Direction.RIGHT);

		assertThat(diff, notNullValue());
		assertThat(diff.getRightDirection(), notNullValue());
		assertThat(diff.getLeftDirection(), is(IsNull.nullValue()));
	}
	
	@Test
	public void testEqualStrings_ShouldReturnEmptyOffsetList() throws DiffException {
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new DiffOffset(100L, "abcde", "abcde")));
		final DiffOffsetResponseDTO diffResponse = this.diffService.performDiff(100L);

		assertThat(diffResponse, notNullValue());
		assertThat(diffResponse.getReason(), is(DiffResponseReason.EQUALS));
		assertThat(diffResponse.getOffsets(), is(empty()));
	}
	
	@Test(expected = DirectionIsNullException.class)
	public void testPErformDiffWithNullLeft_ShouldThrowException() throws DiffException {
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new DiffOffset(100L, null, "abcde")));
		this.diffService.performDiff(100L);
	}
	
	@Test(expected = DirectionIsNullException.class)
	public void testPErformDiffWithNullRight_ShouldThrowException() throws DiffException {
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new DiffOffset(100L, "abcde", null)));
		this.diffService.performDiff(100L);
	}
	
	@Test
	public void testDifferenteSizesPayloads() throws DiffException {
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class)))
				.thenReturn(Optional.of(new DiffOffset(100L, "abcdeabcde", "abcde")));
		final DiffOffsetResponseDTO diffResponse = this.diffService.performDiff(100L);

		assertThat(diffResponse, notNullValue());
		assertThat(diffResponse.getReason(), is(DiffResponseReason.NOT_EQUAL_SIZES));
		assertThat(diffResponse.getOffsets(), is(empty()));
	}
	
	@Test
	public void testDifferentStrings_ShouldReturnTwoOffsets() throws DiffException {
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new DiffOffset(100L, "aXcXe", "abcde")));
		
		final DiffOffsetResponseDTO diffResponse = this.diffService.performDiff(100L);

		assertThat(diffResponse, notNullValue());
		assertThat(diffResponse.getReason(), is(DiffResponseReason.DIFFERENT_PAYLOADS));

		assertThat(diffResponse.getOffsets(),
				allOf(hasSize(equalTo(2)),
						hasItem(allOf(hasProperty("length", equalTo(1)), hasProperty("offset", equalTo(1)))),
						hasItem(allOf(hasProperty("length", equalTo(1)), hasProperty("offset", equalTo(1))))
					));

	}
	
	@Test
	public void testDifferentStrings_ShouldReturnOneOffsetOfLength3() throws DiffException {
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new DiffOffset(100L, "aXXXe", "abcde")));
		
		final DiffOffsetResponseDTO diffResponse = this.diffService.performDiff(100L);

		assertThat(diffResponse, notNullValue());
		assertThat(diffResponse.getReason(), is(DiffResponseReason.DIFFERENT_PAYLOADS));

		assertThat(diffResponse.getOffsets(),
				allOf(hasSize(equalTo(1)),
						hasItem(allOf(hasProperty("length", equalTo(3)), hasProperty("offset", equalTo(1))))
					));

	}
	
	@Test(expected = DiffException.class)
	public void testDiffNotExistentValue_ShouldThrowException() throws DiffException {
		this.diffService.performDiff(100L);
	}
}
