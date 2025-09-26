package org.example.backend.common.weight.repository;

import org.example.backend.common.weight.entity.TeamWeight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamWeightRepository extends JpaRepository<TeamWeight, Long> {
}
