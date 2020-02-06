package com.lucas.waes.diffservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucas.waes.diffservice.domain.Diff;

@Repository
public interface DiffRepository extends JpaRepository<Diff, Long> {

}
