package org.example.backend.f1.statistics;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.backend.f1.team.F1TeamRepository;
import org.example.backend.f1.team.entity.F1Team;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class F1StatisticsController {

    private final F1StatisticService f1StatisticService;
    private final F1TeamRepository f1TeamRepository;

    @PostMapping("/f1/{team}/like")
    public ResponseEntity<Void> addLikeToTeam(@PathVariable String team) {
        f1StatisticService.addLiked(team);
        return ResponseEntity.ok().build(); // 성공 응답 반환
    }

    @GetMapping("/f1/statistics")
    public ResponseEntity<List<F1StatisticsResponse>> getTeamRankings() {
        List<F1StatisticsResponse> rankings = f1StatisticService.getTeamRankings();
        return ResponseEntity.ok(rankings);
    }

}
