package org.example.backend.f1.weight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import org.example.backend.common.weight.dto.UserF1RecommendRequest;
import org.example.backend.f1.statistics.F1StatisticsService;
import org.example.backend.f1.team.F1TeamRepository;
import org.example.backend.f1.team.F1Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class F1WeightService {

    F1TeamRepository f1TeamRepository;
    F1StatisticsService f1StatisticsService;

    @Autowired
    public void F1WeightService(F1TeamRepository f1TeamRepository, F1StatisticsService f1StatisticsService) {
        this.f1TeamRepository = f1TeamRepository;
        this.f1StatisticsService = f1StatisticsService;
    }

    public List<Entry<String, Double>> f1RankTeams(List<F1TeamWeight> f1TeamWeights, UserF1RecommendRequest userF1RecommendRequest) {
        Map<String, Double> scoreMap = new HashMap<>();
        UserF1Weight userF1Weight = new UserF1Weight(userF1RecommendRequest);
        List<F1Team> f1Teams = f1TeamRepository.findAll();

        for (F1Team f1Team : f1Teams) {
            Random random = new Random();
            double score = random.nextDouble();
            scoreMap.put(f1Team.getName(), score);
        }

        List<Map.Entry<String, Double>> rankedList = new ArrayList<>(scoreMap.entrySet());
        rankedList.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        String name = rankedList.get(0).getKey();
        F1Team recommended = f1TeamRepository.findByName(name);
        f1StatisticsService.addRecommended(recommended);

        return rankedList;
    }
}
