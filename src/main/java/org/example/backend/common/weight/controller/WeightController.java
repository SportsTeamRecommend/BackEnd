package org.example.backend.common.weight.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.common.weight.dto.UserF1RecommendRequest;
import org.example.backend.f1.weight.F1TeamWeight;
import org.example.backend.f1.weight.F1TeamWeightRepository;
import org.example.backend.baseball.weight.KboTeamWeight;
import org.example.backend.baseball.weight.UserKboWeight;
import org.example.backend.baseball.weight.KboTeamWeightRepository;
import org.example.backend.baseball.weight.KboWeightService;
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
    private final KboTeamWeightRepository kboTeamWeightRepository;
    private final F1TeamWeightRepository f1TeamWeightRepository;

    @GetMapping("/recommend/kbo")
    public ResponseEntity<List<Map.Entry<String, Double>>> recommendKboTeams(@RequestBody UserKboWeight userKboWeight) {

        List<KboTeamWeight> teamWeights = kboTeamWeightRepository.findAll();
        List<Map.Entry<String, Double>> result = kboWeightService.kboRankTeams(teamWeights, userKboWeight);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/f1/recommend")
    public ResponseEntity<List<Map.Entry<String, Double>>> recommendF1Teams(@RequestBody UserF1RecommendRequest userF1RecommendRequest) {

        List<F1TeamWeight> teamWeights = f1TeamWeightRepository.findAll();
        List<Map.Entry<String, Double>> result = f1WeightService.f1RankTeams(teamWeights, userF1RecommendRequest);

        return ResponseEntity.status(200).body(result);
    }

}
