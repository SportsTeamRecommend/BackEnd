package org.example.backend.baseball.weight;

import org.example.backend.baseball.table.KboWeight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KboWeightRepository extends JpaRepository<KboWeight, Long> {
    Optional<KboWeight> findByKboTeam_TeamCode(String teamCode);

    Optional<KboWeight> findByKboTeam_TeamName(String teamName);
}
