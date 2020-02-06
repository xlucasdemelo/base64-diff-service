package com.lucas.waes.diffservice.service;

import java.util.Optional;

import org.hamcrest.core.IsNot;
import org.junit.Assert;
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
import com.lucas.waes.diffservice.repository.DiffRepository;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class DiffServiceTest {

	@Spy
	@InjectMocks
	private DiffServiceImpl diffService;

	@Mock
	private DiffRepository diffRepository;

	@Test
	public void testEqualStringsShouldReturnEmptyOffsetList() {
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class)))
				.thenReturn(Optional.of(new Diff(100L, "abcde", "abcde")));
		final DiffResponse diffResponse = this.diffService.diff(100L);

		assertThat(diffResponse, notNullValue());
		assertThat(diffResponse.getReason(), is(DiffResponseReason.EQUALS));
		assertThat(diffResponse.getDiffOffsets(), is(empty()));
	}

	@Test
	public void testDifferentStringsShouldReturnTwoOffsets() {
		Mockito.when(diffRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(new Diff(100L, "aXcXe", "abcde")));
		
		final DiffResponse diffResponse = this.diffService.diff(100L);

		assertThat(diffResponse, notNullValue());
		assertThat(diffResponse.getReason(), is(DiffResponseReason.DIFFERENT_PAYLOADS));

		assertThat(diffResponse.getDiffOffsets(),
				allOf(hasSize(equalTo(2)),
						hasItem(allOf(hasProperty("length", equalTo(1)), hasProperty("offset", equalTo(1)))),
						hasItem(allOf(hasProperty("length", equalTo(1)), hasProperty("offset", equalTo(1))))
					));

	}
}
