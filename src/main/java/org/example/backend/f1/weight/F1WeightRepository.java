package org.example.backend.f1.weight;

import org.example.backend.f1.table.F1Metrics;
import org.example.backend.f1.table.F1Weight;
import org.example.backend.f1.team.F1Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface F1WeightRepository extends JpaRepository<F1Weight, Integer> {
    Optional<F1Weight> findByMetrics(F1Metrics metrics);

    Optional<F1Weight> findByMetrics_Name(String teamName);
}
