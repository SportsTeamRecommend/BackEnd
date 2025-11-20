package org.example.backend.baseball.statistics;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.team.KboTeamRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class KboStatisticsController {
    private final KboStatisticsService kboStatisticsService;
    private final KboTeamRepository kboTeamRepository;

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
