package com.lucas.waes.diffservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucas.waes.diffservice.domain.DiffOffset;

/**
 * Repository of {@link DiffOffset}
 * @author lucas
 *
 */
@Repository
public interface DiffOffsetRepository extends JpaRepository<DiffOffset, Long> {

}
