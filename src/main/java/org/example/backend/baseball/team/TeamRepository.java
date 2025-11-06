package org.example.backend.baseball.team;

import org.example.backend.baseball.table.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Team, String> {
    @Query("SELECT MIN(t.foundedYear) FROM Team t")
    Integer findOldestFoundedYear();  // 가장 오래된 팀의 창단연도
}
