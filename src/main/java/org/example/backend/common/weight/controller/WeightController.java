package org.example.backend.common.weight.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.common.weight.dto.UserF1RecommendRequest;
import org.example.backend.common.weight.entity.F1TeamWeight;
import org.example.backend.common.weight.entity.KboTeamWeight;
import org.example.backend.common.weight.entity.UserKboWeight;
import org.example.backend.common.weight.repository.F1TeamWeightRepository;
import org.example.backend.common.weight.repository.KboTeamWeightRepository;
import org.example.backend.common.weight.service.WeightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WeightController {

    private final WeightService weightService;
    private final KboTeamWeightRepository kboTeamWeightRepository;
    private final F1TeamWeightRepository f1TeamWeightRepository;

    @PostMapping("/kbo/recommend")
    public ResponseEntity<List<Map.Entry<String, Double>>> recommendTeams(@RequestBody UserKboWeight userKboWeight) {

        List<KboTeamWeight> teamWeights = kboTeamWeightRepository.findAll();
        List<Map.Entry<String, Double>> result = weightService.kboRankTeams(teamWeights, userKboWeight);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/f1/recommend")
    public ResponseEntity<List<Map.Entry<String, Double>>> recommendF1Teams(@RequestBody UserF1RecommendRequest userF1RecommendRequest) {

        List<F1TeamWeight> teamWeights = f1TeamWeightRepository.findAll();
        List<Map.Entry<String, Double>> result = weightService.f1RankTeams(teamWeights, userF1RecommendRequest);

        return ResponseEntity.status(200).body(result);
    }

}
