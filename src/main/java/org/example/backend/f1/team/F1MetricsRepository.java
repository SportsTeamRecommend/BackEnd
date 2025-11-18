package org.example.backend.f1.team;

import org.example.backend.f1.table.F1Metrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface F1MetricsRepository extends JpaRepository<F1Metrics, Long> {
    Optional<F1Metrics> findByName(String name);

    @Query("SELECT MIN(m.teamRecord) FROM F1Metrics m")
    int findMinTeamRecord();

    @Query("SELECT MAX(m.teamRecord) FROM F1Metrics m")
    int findMaxTeamRecord();

    @Query("SELECT MIN(m.legacy) FROM F1Metrics m")
    int findMinLegacy();

    @Query("SELECT MAX(m.legacy) FROM F1Metrics m")
    int findMaxLegacy();

    @Query("SELECT MIN(m.fandom) FROM F1Metrics m")
    double findMinFandom();

    @Query("SELECT MAX(m.fandom) FROM F1Metrics m")
    double findMaxFandom();

    @Query("""
                SELECT MIN((m.currentYearPoint - m.lastYearPoint) * 1.0 / m.lastYearPoint)
                FROM F1Metrics m
                WHERE m.lastYearPoint <> 0
            """)
    double findMinUnderdog();
    @Query("""
                SELECT MAX((m.currentYearPoint - m.lastYearPoint) * 1.0 / m.lastYearPoint)
                FROM F1Metrics m
                WHERE m.lastYearPoint <> 0
            """)
    double findMaxUnderdog();
}

