package org.example.backend.f1.statistics;

import java.util.List;
import lombok.Getter;
import org.example.backend.f1.team.F1TeamRepository;
import org.example.backend.f1.team.entity.F1Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Service
public class F1StatisticService {

    private final F1StatisticsRepository f1StatisticsRepository;
    private final F1TeamRepository f1TeamRepository;

    @Autowired
    public F1StatisticService(F1StatisticsRepository f1StatisticsRepository, F1TeamRepository f1TeamRepository) {
        this.f1StatisticsRepository = f1StatisticsRepository;
        this.f1TeamRepository = f1TeamRepository;
    }

    @Transactional
    public void addLiked(String teamName) {
        F1Team team = f1TeamRepository.findByName(teamName);
        F1Statistics statistics = f1StatisticsRepository.findById(team.getId()).orElseThrow();
        statistics.incrementLiked();

        f1StatisticsRepository.save(statistics);
    }

    @Transactional
    public void addRecommended(F1Team team) {
        F1Statistics stats = f1StatisticsRepository.findById(team.getId())
                .orElseGet(() -> new F1Statistics(team));

        stats.incrementRecommended();

        f1StatisticsRepository.save(stats);
    }

    public List<F1StatisticsResponse> getTeamRankings() {
        List<F1Statistics> sortedStats = f1StatisticsRepository.findAllByOrderByRecommendedDesc();

        return sortedStats.stream()
                .map(stat -> new F1StatisticsResponse(
                        stat.getTeam().getName(),
                        stat.getRecommended(),
                        stat.getLikedPercentage()
                )).toList();
    }

}
