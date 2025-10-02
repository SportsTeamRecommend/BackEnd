package org.example.backend.common.weight.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.common.weight.entity.KboTeamWeight;
import org.example.backend.common.weight.entity.UserKboWeight;
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

    @PostMapping("/recommend")
    public ResponseEntity<List<Map.Entry<String, Double>>> recommendTeams(@RequestBody UserKboWeight userKboWeight) {


        List<KboTeamWeight> teamWeights = kboTeamWeightRepository.findAll();
        List<Map.Entry<String, Double>> result = weightService.kboRankTeams(teamWeights, userKboWeight);

        return ResponseEntity.ok(result);
    }
}