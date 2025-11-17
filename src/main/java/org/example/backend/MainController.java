package org.example.backend;

import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.table.KboWeight;
import org.example.backend.baseball.team.Region;
import org.example.backend.baseball.weight.KboWeightRepository;
import org.example.backend.common.weight.dto.F1RecommendResponse;
import org.example.backend.common.weight.dto.KboRecommendResponse;
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
public class MainController {

    private final KboWeightService kboWeightService;
    private final F1WeightService f1WeightService;
    private final F1TeamWeightRepository f1TeamWeightRepository;
    private final KboWeightRepository kboWeightRepository;

    @PostMapping("/kbo/recommend")
    public ResponseEntity<List<KboRecommendResponse>> recommendTeams(@RequestBody UserKboWeight userKboWeight, @RequestParam Region userRegion) {
        List<KboWeight> teamWeights = kboWeightRepository.findAll();
        List<KboRecommendResponse> result =  kboWeightService.getKboWeightRanks(teamWeights, userKboWeight, userRegion);

        return ResponseEntity.status(200).body(result);
    }

    @PostMapping("/f1/recommend")
    public ResponseEntity<List<F1RecommendResponse>> recommendF1Teams(@RequestBody UserF1RecommendRequest userF1RecommendRequest) {
        List<F1TeamWeight> teamWeights = f1TeamWeightRepository.findAll();
        List<F1RecommendResponse> result = f1WeightService.f1RankTeams(teamWeights, userF1RecommendRequest);

        return ResponseEntity.status(200).body(result);
    }

//    @PostMapping("/kbo/compare")
//    public ResponseEntity<?> compareKboTeams(@RequestBody KboWeight request) {
//        var result = kboComparisonService.compareTeams(request);
//        return ResponseEntity.ok(result);
//    }

}
