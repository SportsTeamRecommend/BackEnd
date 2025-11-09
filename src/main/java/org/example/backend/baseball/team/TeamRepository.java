package org.example.backend.baseball.team;

import org.example.backend.baseball.table.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Team, String> {
    // 창단연도
    @Query("SELECT MIN(t.foundedYear) FROM Team t")
    Integer findOldestFoundedYear();

    // 프랜차이즈 스타 수
    @Query("SELECT MIN(t.starPlayerCount) FROM Team t")
    Integer findMinStarPlayerCount();
    @Query("SELECT MAX(t.starPlayerCount) FROM Team t")
    Integer findMaxStarPlayerCount();

    // 평균 나이
    @Query("SELECT MIN(t.averageAge) FROM Team t")
    Double findMinAverageAge();
    @Query("SELECT MAX(t.averageAge) FROM Team t")
    Double findMaxAverageAge();

    // 팬 규모
    @Query("SELECT MIN(t.fanScale) FROM Team t")
    Double findMinFanScale();
    @Query("SELECT MAX(t.fanScale) FROM Team t")
    Double findMaxFanScale();
}
