/*
 * Copyright 2013-2015, Teradata, Inc. All rights reserved.
 */
package com.teradata.benchmark.service.repo;

import com.teradata.benchmark.service.model.BenchmarkRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenchmarkRunRepo
        extends JpaRepository<BenchmarkRun, String>
{
    BenchmarkRun findByNameAndSequenceId(String benchmarkName, String sequenceId);

    List<BenchmarkRun> findByName(String benchmarkName);

    @Query(value = "" +
            "WITH summary AS ( " +
            "  SELECT " +
            "    b.id, " +
            "    b.name, " +
            "    b.sequence_id, " +
            "    b.status, " +
            "    b.version, " +
            "    b.started, " +
            "    b.ended, " +
            "    b.environment_id, " +
            "    rank() " +
            "    OVER (PARTITION BY b.name " +
            "      ORDER BY b.sequence_id DESC) AS rk " +
            "  FROM benchmark_runs b " +
            ") " +
            "SELECT s.* " +
            "FROM summary s " +
            "WHERE s.rk = 1 " +
            "ORDER BY s.started DESC ",
            nativeQuery = true)
    List<BenchmarkRun> findLatest();
}
