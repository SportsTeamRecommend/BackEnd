package org.example.backend.baseball.team;

import org.example.backend.baseball.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, String> {
}
