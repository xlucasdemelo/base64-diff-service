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

import com.lucas.waes.diffservice.domain.Diff;
import com.lucas.waes.diffservice.domain.DiffResponse;
import com.lucas.waes.diffservice.domain.DiffResponseReason;
import com.lucas.waes.diffservice.exception.DiffException;
import com.lucas.waes.diffservice.exception.DirectionAlreadyExistsException;
import com.lucas.waes.diffservice.exception.DirectionIsNullException;
import com.lucas.waes.diffservice.repository.DiffRepository;
import com.lucas.waes.diffservice.util.DiffConstants;

@RunWith(MockitoJUnitRunner.class)
public class DiffServiceTest {

	@Spy
	@InjectMocks
	private DiffServiceImpl diffService;
	
	@Mock
	private DiffRepository diffRepository;
	
	@Test
	public void testSaveNewDiffOnlyWithLeft() throws DiffException {
		final Diff diff = new Diff(100L, "abc", null);

		Mockito.when(diffRepository.save(any(Diff.class))).thenReturn(diff);
		this.diffService.saveDiff(100L, "abc", DiffConstants.LEFT);

		assertThat(diff, notNullValue());
		assertThat(diff.getLeftDirection(), notNullValue());
		assertThat(diff.getRightDirection(), is(IsNull.nullValue()));
	}
	
	@Test(expected = DirectionAlreadyExistsException.class)
	public void testOverrideLeftOfExistentDiffShouldThrowException() throws DiffException {
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new Diff(100L, "abcde", null )));
		this.diffService.saveDiff(100L, "abc", DiffConstants.LEFT);
	}
	
	@Test(expected = DirectionAlreadyExistsException.class)
	public void testOverrideRightOfExistentDiffShouldThrowException() throws DiffException {
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new Diff(100L, null, "abcde")));
		this.diffService.saveDiff(100L, "abc", DiffConstants.RIGHT);
	}
	
	@Test
	public void testSaveNewDiffOnlyWithRight() throws DiffException {
		final Diff diff = new Diff(100L, null, "abc");

		Mockito.when(diffRepository.save(any(Diff.class))).thenReturn(diff);
		this.diffService.saveDiff(100L, "abc", DiffConstants.RIGHT);

		assertThat(diff, notNullValue());
		assertThat(diff.getRightDirection(), notNullValue());
		assertThat(diff.getLeftDirection(), is(IsNull.nullValue()));
	}
	
	@Test
	public void testEqualStringsShouldReturnEmptyOffsetList() throws DiffException {
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new Diff(100L, "abcde", "abcde")));
		final DiffResponse diffResponse = this.diffService.performDiff(100L);

		assertThat(diffResponse, notNullValue());
		assertThat(diffResponse.getReason(), is(DiffResponseReason.EQUALS));
		assertThat(diffResponse.getDiffOffsets(), is(empty()));
	}
	
	@Test(expected = DirectionIsNullException.class)
	public void testNullStringShouldThrowExceptiont() throws DiffException {
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new Diff(100L, null, "abcde")));
		this.diffService.performDiff(100L);
	}
	
	@Test
	public void testDifferenteSizesPayloads() throws DiffException {
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class)))
				.thenReturn(Optional.of(new Diff(100L, "abcdeabcde", "abcde")));
		final DiffResponse diffResponse = this.diffService.performDiff(100L);

		assertThat(diffResponse, notNullValue());
		assertThat(diffResponse.getReason(), is(DiffResponseReason.NOT_EQUAL_SIZES));
		assertThat(diffResponse.getDiffOffsets(), is(empty()));
	}
	
	@Test
	public void testDifferentStringsShouldReturnTwoOffsets() throws DiffException {
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new Diff(100L, "aXcXe", "abcde")));
		
		final DiffResponse diffResponse = this.diffService.performDiff(100L);

		assertThat(diffResponse, notNullValue());
		assertThat(diffResponse.getReason(), is(DiffResponseReason.DIFFERENT_PAYLOADS));

		assertThat(diffResponse.getDiffOffsets(),
				allOf(hasSize(equalTo(2)),
						hasItem(allOf(hasProperty("length", equalTo(1)), hasProperty("offset", equalTo(1)))),
						hasItem(allOf(hasProperty("length", equalTo(1)), hasProperty("offset", equalTo(1))))
					));

	}
	
	@Test
	public void testDifferentStringsShouldReturnOneOffsetOfLength3() throws DiffException {
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new Diff(100L, "aXXXe", "abcde")));
		
		final DiffResponse diffResponse = this.diffService.performDiff(100L);

		assertThat(diffResponse, notNullValue());
		assertThat(diffResponse.getReason(), is(DiffResponseReason.DIFFERENT_PAYLOADS));

		assertThat(diffResponse.getDiffOffsets(),
				allOf(hasSize(equalTo(1)),
						hasItem(allOf(hasProperty("length", equalTo(3)), hasProperty("offset", equalTo(1))))
					));

	}
	
	@Test(expected = DiffException.class)
	public void testDiffNotExistentValueShouldThrowException() throws DiffException {
		this.diffService.performDiff(100L);
	}
}
