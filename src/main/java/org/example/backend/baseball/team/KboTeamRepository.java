package org.example.backend.baseball.team;

import org.example.backend.baseball.table.KboTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KboTeamRepository extends JpaRepository<KboTeam, String> {
    // 창단연도
    @Query("SELECT MIN(t.foundedYear) FROM KboTeam t")
    Integer findOldestFoundedYear();

    // 프랜차이즈 스타 수
    @Query("SELECT MIN(t.starPlayerCount) FROM KboTeam t")
    Integer findMinStarPlayerCount();
    @Query("SELECT MAX(t.starPlayerCount) FROM KboTeam t")
    Integer findMaxStarPlayerCount();

    // 평균 나이
    @Query("SELECT MIN(t.averageAge) FROM KboTeam t")
    Double findMinAverageAge();
    @Query("SELECT MAX(t.averageAge) FROM KboTeam t")
    Double findMaxAverageAge();

    // 팬 규모
    @Query("SELECT MIN(t.fanScale) FROM KboTeam t")
    Double findMinFanScale();
    @Query("SELECT MAX(t.fanScale) FROM KboTeam t")
    Double findMaxFanScale();
}
