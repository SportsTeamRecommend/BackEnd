package org.example.backend.f1.team;

import org.example.backend.f1.table.DriverMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DriverMetricsRepository extends JpaRepository<DriverMetrics, Long> {
    @Query("SELECT MIN(d.tenure) FROM DriverMetrics d")
    int findMinTenure();
    @Query("SELECT MAX(d.tenure) FROM DriverMetrics d")
    int findMaxTenure();

    @Query("SELECT MIN(d.record) FROM DriverMetrics d")
    int findMinDriverRecord();
    @Query("SELECT MAX(d.record) FROM DriverMetrics d")
    int findMaxDriverRecord();

}
