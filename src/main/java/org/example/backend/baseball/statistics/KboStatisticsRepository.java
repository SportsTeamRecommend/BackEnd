package org.example.backend.baseball.statistics;

import java.util.List;
import org.example.backend.baseball.table.KboTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KboStatisticsRepository extends JpaRepository<KboStatistics, Integer> {
    List<KboStatistics> findAllByOrderByRecommendedDesc();
    
    boolean existsByTeam(KboTeam team);

    KboStatistics findByTeam(KboTeam team);
}
