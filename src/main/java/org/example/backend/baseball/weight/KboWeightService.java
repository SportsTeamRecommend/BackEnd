package org.example.backend.baseball.weight;

import org.example.backend.baseball.table.KboTeamWeight;
import org.example.backend.common.weight.entity.WeightType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KboWeightService {

    // TODO: 동점자 처리

    public List<Map.Entry<String, Double>> kboRankTeams(List<KboTeamWeight> kboTeamWeights, UserKboWeight userKboWeight) {
        Map<String, Double> scoreMap = new HashMap<>();

        for (KboTeamWeight teamWeight : kboTeamWeights) {
            double score = calculateTeamScore(teamWeight, userKboWeight);
            scoreMap.put(teamWeight.getTeam().getTeamName(), score);
        }

        // 점수 기준 내림차순 정렬
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