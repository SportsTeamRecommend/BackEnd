package org.example.backend.common.weight.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.common.weight.entity.TeamWeight;
import org.example.backend.common.weight.repository.TeamWeightRepository;
import org.example.backend.common.weight.service.WeightService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class WeightController {

    private final WeightService weightService;
    private final TeamWeightRepository teamWeightRepository;

    @PostMapping("/result")
    public void receiveUserWeight(){
        List<TeamWeight> teamWeights = teamWeightRepository.findAll();

        double[] userPref = new double[]{8.0, 10.0, 7.0, 8.0, 10.0, 7.0}; // 사용자 성향

        List<Map.Entry<String, Double>> result = weightService.rankTeams(teamWeights, userPref);

        System.out.println("유사도 기반 팀 순위:");
        for (int i = 0; i < result.size(); i++) {
            Map.Entry<String, Double> entry = result.get(i);
            System.out.printf("%d위: %s (유사도 %.4f)\n", i + 1, entry.getKey(), entry.getValue());
        }
    }

}
