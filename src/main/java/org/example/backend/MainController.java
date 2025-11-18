package org.example.backend;

import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.table.KboWeight;
import org.example.backend.baseball.team.Region;
import org.example.backend.baseball.weight.KboWeightRepository;
import org.example.backend.common.compare.CompareService;
import org.example.backend.common.compare.F1WeightResponse;
import org.example.backend.common.compare.KboWeightResponse;
import org.example.backend.common.weight.dto.F1RecommendResponse;
import org.example.backend.common.weight.dto.KboRecommendResponse;
import org.example.backend.f1.table.F1Weight;
import org.example.backend.baseball.weight.UserKboWeight;
import org.example.backend.baseball.weight.KboWeightService;
import org.example.backend.f1.weight.F1WeightRepository;
import org.example.backend.f1.weight.F1WeightService;
import org.example.backend.f1.weight.UserF1Weight;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {

    private final KboWeightService kboWeightService;
    private final F1WeightService f1WeightService;
    private final F1WeightRepository f1WeightRepository;
    private final KboWeightRepository kboWeightRepository;
    private final CompareService compareService;

    @PostMapping("/kbo/recommend")
    public ResponseEntity<List<KboRecommendResponse>> recommendTeams(@RequestBody UserKboWeight userKboWeight, @RequestParam Region userRegion) {
        List<KboWeight> teamWeights = kboWeightRepository.findAll();
        List<KboRecommendResponse> result =  kboWeightService.getKboWeightRanks(teamWeights, userKboWeight, userRegion);

        return ResponseEntity.status(200).body(result);
    }

    @PostMapping("/f1/recommend")
    public ResponseEntity<List<F1RecommendResponse>> recommendF1Teams(@RequestBody UserF1Weight userF1Weight) {
        List<F1Weight> teamWeights = f1WeightRepository.findAll();
        List<F1RecommendResponse> result = f1WeightService.getF1WeightRanks(teamWeights, userF1Weight);

        return ResponseEntity.status(200).body(result);
    }

    @GetMapping("/kbo/compare")
    public ResponseEntity<KboWeightResponse> getKboWeight(@RequestParam String teamName) {

        KboWeightResponse response = compareService.getKboWeightByTeamName(teamName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/f1/compare")
    public ResponseEntity<F1WeightResponse> getF1Weight(@RequestParam String teamName) {

        F1WeightResponse response = compareService.getF1WeightByTeamName(teamName);
        return ResponseEntity.ok(response);
    }

}