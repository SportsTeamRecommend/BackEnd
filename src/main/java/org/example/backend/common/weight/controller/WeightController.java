package org.example.backend.common.weight.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.table.KboWeight;
import org.example.backend.baseball.table.Team;
import org.example.backend.baseball.team.Region;
import org.example.backend.baseball.weight.KboWeightRepository;
import org.example.backend.common.weight.dto.F1RecommendResponse;
import org.example.backend.common.weight.dto.UserF1RecommendRequest;
import org.example.backend.f1.weight.F1TeamWeight;
import org.example.backend.baseball.weight.UserKboWeight;
import org.example.backend.baseball.weight.KboWeightService;
import org.example.backend.f1.weight.F1TeamWeightRepository;
import org.example.backend.f1.weight.F1WeightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WeightController {

    private final KboWeightService kboWeightService;
    private final F1WeightService f1WeightService;
    private final F1TeamWeightRepository f1TeamWeightRepository;
    private final KboWeightRepository kboWeightRepository;

    @PostMapping("/kbo/recommend")
    public ResponseEntity<List<Map.Entry<String, Double>>> recommendTeams(@RequestBody UserKboWeight userKboWeight, @RequestParam Region userRegion) {
        List<KboWeight> allTeams = kboWeightRepository.findAll();
        // TODO : 디버깅 용
        for (KboWeight allTeam : allTeams) {
            Team team = allTeam.getTeam();
            System.out.println(
                    "Weight ID: " + allTeam.getId() +
                            ", team_code(FK): " + (team != null ? team.getTeamCode() : "❌ NULL") +
                            ", teamName: " + (team != null ? team.getTeamName() : "❌ NULL")
            );
        }
        List<Map.Entry<String, Double>> result =  kboWeightService.getKboWeightRanks(allTeams, userKboWeight, userRegion);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/f1/recommend")
    public ResponseEntity<List<F1RecommendResponse>> recommendF1Teams(@RequestBody UserF1RecommendRequest userF1RecommendRequest) {

        List<F1TeamWeight> teamWeights = f1TeamWeightRepository.findAll();
        List<F1RecommendResponse> result = f1WeightService.f1RankTeams(teamWeights, userF1RecommendRequest);

        return ResponseEntity.status(200).body(result);
    }

}
