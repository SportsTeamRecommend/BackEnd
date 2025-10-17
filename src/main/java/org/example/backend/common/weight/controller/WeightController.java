package org.example.backend.common.weight.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.weight.KboTeamWeight;
import org.example.backend.baseball.weight.UserKboWeight;
import org.example.backend.baseball.weight.KboTeamWeightRepository;
import org.example.backend.baseball.weight.KboWeightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WeightController {

    private final KboWeightService kboWeightService;
    private final KboTeamWeightRepository kboTeamWeightRepository;

    @GetMapping("/recommend/kbo")
    public ResponseEntity<List<Map.Entry<String, Double>>> recommendKboTeams(@RequestBody UserKboWeight userKboWeight) {

        List<KboTeamWeight> teamWeights = kboTeamWeightRepository.findAll();
        List<Map.Entry<String, Double>> result = kboWeightService.kboRankTeams(teamWeights, userKboWeight);

        return ResponseEntity.ok(result);
    }
}