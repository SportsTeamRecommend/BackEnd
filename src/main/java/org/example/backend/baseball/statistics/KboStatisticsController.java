package org.example.backend.baseball.statistics;

import java.util.List;
import org.example.backend.baseball.team.KboTeamRepository;
import org.example.backend.f1.statistics.F1StatisticsResponse;
import org.example.backend.f1.statistics.F1StatisticsService;
import org.example.backend.f1.team.F1TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class KboStatisticsController {
    private final KboStatisticsService kboStatisticsService;
    private final KboTeamRepository kboTeamRepository;

    @Autowired
    public KboStatisticsController(final KboStatisticsService kboStatisticsService,
                                   final KboTeamRepository kboTeamRepository) {
        this.kboStatisticsService = kboStatisticsService;
        this.kboTeamRepository = kboTeamRepository;
    }

    @PostMapping("/kbo/{team}/recommended")
    public ResponseEntity<Void> addRecommendedToTeam(@PathVariable String team) {
        kboStatisticsService.addRecommended(team);
        return ResponseEntity.ok().build(); // 성공 응답 반환
    }

    @PostMapping("/kbo/{team}/like")
    public ResponseEntity<Void> addLikeToTeam(@PathVariable String team) {
        kboStatisticsService.addLiked(team);
        return ResponseEntity.ok().build(); // 성공 응답 반환
    }

    @GetMapping("/kbo/statistics")
    public ResponseEntity<List<KboStatisticsResponse>> getTeamRankings() {
        List<KboStatisticsResponse> rankings = kboStatisticsService.getTeamRankings();
        return ResponseEntity.ok(rankings);
    }
}
