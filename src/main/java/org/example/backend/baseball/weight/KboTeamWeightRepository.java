package org.example.backend.baseball.weight;

import org.example.backend.baseball.table.KboTeamWeight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KboTeamWeightRepository extends JpaRepository<KboTeamWeight, Long> {
}
