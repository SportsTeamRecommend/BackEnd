package org.example.backend.baseball;

import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.table.KboWeight;
import org.example.backend.baseball.team.Region;
import org.example.backend.baseball.weight.KboWeightRepository;
import org.example.backend.baseball.weight.KboWeightService;
import org.example.backend.baseball.weight.UserKboWeight;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kbo")
public class BaseballController {

    private final KboWeightService kboWeightService;
    private final KboWeightRepository kboWeightRepository;

    @PostMapping("/recommend")
    public ResponseEntity<List<Map.Entry<String, Double>>> recommendTeams(@RequestBody UserKboWeight userKboWeight, @RequestParam Region userRegion) {
        List<KboWeight> allTeams = kboWeightRepository.findAll();
        List<Map.Entry<String, Double>> result =  kboWeightService.getKboWeightRanks(allTeams, userKboWeight, userRegion);

        return ResponseEntity.ok(result);
    }


}
