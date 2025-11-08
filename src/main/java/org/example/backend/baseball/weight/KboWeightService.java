package org.example.backend.baseball.weight;

import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.table.KboWeight;
import org.example.backend.baseball.table.RegionDistance;
import org.example.backend.baseball.table.Team;
import org.example.backend.baseball.team.EntityCalculator;
import org.example.backend.baseball.team.Region;
import org.example.backend.baseball.team.RegionDistanceRepository;
import org.example.backend.common.weight.entity.WeightType;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class KboWeightService {

    private final RegionDistanceRepository regionDistanceRepository;
    private final EntityCalculator calculator;

    // TODO: 동점자 처리

    public List<Map.Entry<String, Double>> getKboWeightRanks(List<KboWeight> kboWeights, UserKboWeight userKboWeight, Region userRegion) {
        Map<String, Double> scoreMap = new HashMap<>();

        double maxDistance = regionDistanceRepository.findMaxDistanceKm();

        for (KboWeight teamWeight : kboWeights) {
            double score = calculateTeamScore(teamWeight, userKboWeight, userRegion, maxDistance);
            scoreMap.put(teamWeight.getTeam().getTeamName(), score);
        }

        // 점수 기준 내림차순 정렬
        List<Map.Entry<String, Double>> rankedList = new ArrayList<>(scoreMap.entrySet());
        rankedList.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        return rankedList;
    }



    private double calculateTeamScore(
            KboWeight team,
            UserKboWeight user,
            Region userRegion,
            double maxDistance
    ) {
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

        if (user.getRegionPreference() != WeightType.NONE) {
            double regionWeight = (userRegion == null)
                    ? 5.0
                    : calculateTeamRegionWeight(team.getTeam(), userRegion, maxDistance);
            totalScore += (regionWeight / 10.0) * user.getRegionImportance();
            totalWeight += user.getRegionImportance();
        }

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

    public double calculateTeamRegionWeight(Team team, Region userRegion, double maxDistance) {
        RegionDistance distance = regionDistanceRepository
                .findByRegionAndTeam(userRegion, team)
                .orElseThrow(() -> new IllegalStateException(
                        "거리 데이터 없음: " + userRegion + " - " + team.getTeamName()
                ));

        return calculator.calculateRegionWeight(distance.getDistanceKm(), maxDistance);
    }

}
