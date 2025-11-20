package org.example.backend.f1.statistics;

import java.util.List;
import lombok.Getter;
import org.example.backend.f1.team.F1TeamRepository;
import org.example.backend.f1.team.F1Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Service
public class F1StatisticsService {

    private final F1StatisticsRepository f1StatisticsRepository;
    private final F1TeamRepository f1TeamRepository;

    @Autowired
    public F1StatisticsService(F1StatisticsRepository f1StatisticsRepository, F1TeamRepository f1TeamRepository) {
        this.f1StatisticsRepository = f1StatisticsRepository;
        this.f1TeamRepository = f1TeamRepository;
    }

    @Transactional
    public void InitStatistics(String... args) throws Exception {
        List<F1Team> allTeams = f1TeamRepository.findAll();

        for (F1Team team : allTeams) {
            if (!f1StatisticsRepository.existsById(team.getId())) {
                F1Statistics newStat = new F1Statistics(team, 0.0, 0L, 0L);
                f1StatisticsRepository.save(newStat);
                System.out.println(team.getTeamName() + " 팀의 통계 데이터를 새로 생성했습니다.");
            }
        }
    }

    @Transactional
    public void addLiked(String teamName) {
        F1Team team = f1TeamRepository.findByTeamName(teamName);
        F1Statistics statistics = f1StatisticsRepository.findById(team.getId()).orElseThrow();
        statistics.incrementLiked();

        f1StatisticsRepository.save(statistics);
    }

    @Transactional
    public void addRecommended(String teamName) {
        F1Team team = f1TeamRepository.findByTeamName(teamName);
        F1Statistics stats = f1StatisticsRepository.findById(team.getId())
                .orElseGet(() -> new F1Statistics(team));

        stats.incrementRecommended();

        f1StatisticsRepository.save(stats);
    }

    public List<F1StatisticsResponse> getTeamRankings() {
        List<F1Statistics> sortedStats = f1StatisticsRepository.findAllByOrderByRecommendedDesc();

        return sortedStats.stream()
                .map(stat -> new F1StatisticsResponse(
                        stat.getTeam().getTeamName(),
                        stat.getRecommended(),
                        stat.getLikedPercentage()
                )).toList();
    }

}
