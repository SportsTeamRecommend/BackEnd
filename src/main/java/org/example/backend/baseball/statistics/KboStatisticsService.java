package org.example.backend.baseball.statistics;

import java.util.List;
import org.example.backend.baseball.table.KboTeam;
import org.example.backend.baseball.team.KboTeamRepository;
import org.example.backend.f1.statistics.F1Statistics;
import org.example.backend.f1.statistics.F1StatisticsRepository;
import org.example.backend.f1.statistics.F1StatisticsResponse;
import org.example.backend.f1.team.F1Team;
import org.example.backend.f1.team.F1TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KboStatisticsService {
    private final KboStatisticsRepository kboStatisticsRepository;
    private final KboTeamRepository kboTeamRepository;

    @Autowired
    public KboStatisticsService(KboStatisticsRepository kboStatisticsRepository, KboTeamRepository kboTeamRepository) {
        this.kboStatisticsRepository = kboStatisticsRepository;
        this.kboTeamRepository = kboTeamRepository;
    }

    @Transactional
    public void InitStatistics(String... args) throws Exception {
        List<KboTeam> allTeams = kboTeamRepository.findAll();

        for (KboTeam team : allTeams) {
            if (!kboStatisticsRepository.existsByTeam(team)) {
                KboStatistics newStat = new KboStatistics(team, 0.0, 0L, 0L);
                kboStatisticsRepository.save(newStat);
                System.out.println(team.getTeamName() + " 팀의 통계 데이터를 새로 생성했습니다.");
            }
        }
    }

    @Transactional
    public void addLiked(String teamName) {
        KboTeam team = kboTeamRepository.findByTeamName(teamName);
        KboStatistics statistics = kboStatisticsRepository.findByTeam(team);
        statistics.incrementLiked();

        kboStatisticsRepository.save(statistics);
    }

    @Transactional
    public void addRecommended(String teamName) {
        KboTeam team = kboTeamRepository.findByTeamName(teamName);
        KboStatistics stats = kboStatisticsRepository.findByTeam(team);
        stats.incrementRecommended();

        kboStatisticsRepository.save(stats);
    }

    public List<KboStatisticsResponse> getTeamRankings() {
        List<KboStatistics> sortedStats = kboStatisticsRepository.findAllByOrderByRecommendedDesc();

        return sortedStats.stream()
                .map(stat -> new KboStatisticsResponse(
                        stat.getTeam().getTeamName(),
                        stat.getRecommended(),
                        stat.getLikedPercentage()
                )).toList();
    }
}
