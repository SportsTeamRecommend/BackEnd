package org.example.backend.baseball.statistics;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.table.KboTeam;
import org.example.backend.baseball.team.KboTeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KboStatisticsService {
    private final KboStatisticsRepository kboStatisticsRepository;
    private final KboTeamRepository kboTeamRepository;

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
