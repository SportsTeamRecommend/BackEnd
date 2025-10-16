package org.example.backend.common.weight.service;

import java.util.Random;
import org.example.backend.common.weight.dto.UserF1RecommendRequest;
import org.example.backend.common.weight.entity.F1TeamWeight;
import org.example.backend.common.weight.entity.KboTeamWeight;
import org.example.backend.common.weight.entity.UserF1Weight;
import org.example.backend.common.weight.entity.UserKboWeight;
import org.example.backend.common.weight.entity.WeightType;
import org.example.backend.f1.team.F1TeamRepository;
import org.example.backend.f1.team.entity.F1Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeightService {

    F1TeamRepository f1TeamRepository;

    @Autowired
    public WeightService(F1TeamRepository f1TeamRepository) {
        this.f1TeamRepository = f1TeamRepository;
    }

    public List<Map.Entry<String, Double>> kboRankTeams(List<KboTeamWeight> kboTeamWeights, UserKboWeight userKboWeight) {
        Map<String, Double> scoreMap = new HashMap<>();

        for (KboTeamWeight teamWeight : kboTeamWeights) {
            double score = calculateTeamScore(teamWeight, userKboWeight);
            scoreMap.put(teamWeight.getTeam().getTeamName(), score);
        }

        List<Map.Entry<String, Double>> rankedList = new ArrayList<>(scoreMap.entrySet());
        rankedList.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        return rankedList;
    }

    public List<Map.Entry<String, Double>> f1RankTeams(List<F1TeamWeight> f1TeamWeights, UserF1RecommendRequest userF1RecommendRequest) {
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

        return rankedList;
    }

    private double calculateTeamScore(KboTeamWeight team, UserKboWeight user) {
        double totalScore = 0;
        double totalWeight = 0;

        totalScore += calculateAttributeScore(team.getRecord(), user.getRecordPreference(), user.getRecordImportance());
        totalWeight += getEffectiveWeight(user.getRecordPreference(), user.getRecordImportance());

        totalScore += calculateAttributeScore(team.getLegacy(), user.getLegacyPreference(), user.getLegacyImportance());
        totalWeight += getEffectiveWeight(user.getLegacyPreference(), user.getLegacyImportance());
        totalScore += calculateAttributeScore(team.getFranchiseStar(), user.getFranchiseStarPreference(), user.getFranchiseStarImportance());
        totalWeight += getEffectiveWeight(user.getFranchiseStarPreference(), user.getFranchiseStarImportance());

        totalScore += calculateAttributeScore(team.getGrowth(), user.getGrowthPreference(), user.getGrowthImportance());
        totalWeight += getEffectiveWeight(user.getGrowthPreference(), user.getGrowthImportance());

        totalScore += calculateAttributeScore(team.getRegion(), user.getRegionPreference(), user.getRegionImportance());
        totalWeight += getEffectiveWeight(user.getRegionPreference(), user.getRegionImportance());

        totalScore += calculateAttributeScore(team.getFandom(), user.getFandomPreference(), user.getFandomImportance());
        totalWeight += getEffectiveWeight(user.getFandomPreference(), user.getFandomImportance());

        return totalWeight > 0 ? totalScore / totalWeight : 0;
    }

    private double calculateAttributeScore(double teamValue, WeightType preference, double importance) {
        if (preference == WeightType.NONE) {
            return 0;
        }

        double normalizedValue;
        if (preference == WeightType.HIGH) {
            normalizedValue = teamValue / 10.0;  // 0~1 사이로 정규화
        } else {
            normalizedValue = (10.0 - teamValue) / 10.0;  // 가중치 점수 뒤집고 정규화
        }

        return normalizedValue * importance;  // 정규화된 값에 중요도 곱하기
    }

    private double getEffectiveWeight(WeightType preference, double importance) {
        return preference == WeightType.NONE ? 0 : importance;
    }
}
