package org.example.backend.baseball.weight;

import org.example.backend.baseball.table.KboWeight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KboWeightRepository extends JpaRepository<KboWeight, Long> {
    Optional<KboWeight> findByTeam_TeamCode(String teamCode);
}
