package org.example.backend.baseball.weight;

import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.table.KboWeight;
import org.example.backend.baseball.table.KboRegionDistance;
import org.example.backend.baseball.table.KboTeam;
import org.example.backend.baseball.team.EntityCalculator;
import org.example.backend.baseball.team.Region;
import org.example.backend.baseball.team.RegionDistanceRepository;
import org.example.backend.common.weight.entity.WeightType;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * KBO 팀 가중치 기반 추천 서비스
 * - NONE: 완전히 제외 (분자/분모 모두)
 * - HIGH / LOW: 값(0~10)을 정규화(0~1) 후 필요시 반전
 * - importance: 각 항목의 영향력 (볼륨)
 * - 최종 점수 = (Σ(normalized * importance) / Σ(importance)) * (Σ(importance)/10) * 100
 */
@Service
@RequiredArgsConstructor
public class KboWeightService {

    private final RegionDistanceRepository regionDistanceRepository;
    private final EntityCalculator calculator;

    public List<Map.Entry<String, Double>> getKboWeightRanks(
            List<KboWeight> kboWeights,
            UserKboWeight userKboWeight,
            Region userRegion
    ) {
        Map<String, Double> scoreMap = new HashMap<>();
        double maxDistance = regionDistanceRepository.findMaxDistanceKm();

        for (KboWeight teamWeight : kboWeights) {
            double score = calculateTeamScore(teamWeight, userKboWeight, userRegion, maxDistance);
            scoreMap.put(teamWeight.getKboTeam().getTeamCode(), score); // teamName 써도 OK
        }

        // 내림차순 정렬
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
        double totalScore = 0.0;
        double totalImportance = 0.0;

        totalScore += getWeightedScore(team.getRecord(), user.getRecordPreference(), user.getRecordImportance());
        totalImportance += getEffectiveWeight(user.getRecordPreference(), user.getRecordImportance());

        totalScore += getWeightedScore(team.getLegacy(), user.getLegacyPreference(), user.getLegacyImportance());
        totalImportance += getEffectiveWeight(user.getLegacyPreference(), user.getLegacyImportance());

        totalScore += getWeightedScore(team.getFranchiseStar(), user.getFranchiseStarPreference(), user.getFranchiseStarImportance());
        totalImportance += getEffectiveWeight(user.getFranchiseStarPreference(), user.getFranchiseStarImportance());

        totalScore += getWeightedScore(team.getGrowth(), user.getGrowthPreference(), user.getGrowthImportance());
        totalImportance += getEffectiveWeight(user.getGrowthPreference(), user.getGrowthImportance());

        totalScore += getRegionWeightedScore(team.getKboTeam(), user.getRegionPreference(), user.getRegionImportance(), userRegion, maxDistance);
        totalImportance += getEffectiveWeight(user.getRegionPreference(), user.getRegionImportance());

        totalScore += getWeightedScore(team.getFandom(), user.getFandomPreference(), user.getFandomImportance());
        totalImportance += getEffectiveWeight(user.getFandomPreference(), user.getFandomImportance());

        if (totalImportance <= 0.0) return 0.0;

        // 가중평균은 그대로 0~1
        double weightedAvg01 = totalScore / totalImportance;

        // 중요도를 soft scaling: (10 / (10 + totalImportance))
        // 중요도가 커질수록 증가하되, 절대 1을 넘지 않음
        double softBoost = 1 - Math.exp(-totalImportance / 10.0); // sigmoid-like curve

        // 최종 점수 (0~100 보장)
        double scorePercent = weightedAvg01 * softBoost * 100.0;
        return scorePercent;
    }


    /**
     * 일반 항목(성적, 연혁, 스타, 성장, 팬덤)의 가중 점수 계산
     */
    private double getWeightedScore(double value10, WeightType preference, double importance) {
        if (preference == WeightType.NONE) return 0.0;

        double normalized = value10 / 10.0;
        if (preference == WeightType.LOW) {
            normalized = 1.0 - normalized;
        }
        return normalized * importance;
    }

    /**
     * 연고지(거리) 항목용 가중 점수 계산
     * - calculator.calculateRegionWeight(): 0~10 점수 반환 (가까울수록 높음)
     */
    private double getRegionWeightedScore(
            KboTeam kboTeam,
            WeightType preference,
            double importance,
            Region userRegion,
            double maxDistance
    ) {
        if (preference == WeightType.NONE) return 0.0;

        double regionScore10 = (userRegion == null)
                ? 5.0
                : calculateTeamRegionWeight(kboTeam, userRegion, maxDistance); // 0~10 점수

        double normalized = regionScore10 / 10.0;
        if (preference == WeightType.LOW) {
            normalized = 1.0 - normalized;
        }
        return normalized * importance;
    }

    /**
     * NONE인 항목은 중요도 0으로 처리 (분모에서 제외)
     */
    private double getEffectiveWeight(WeightType preference, double importance) {
        return preference == WeightType.NONE ? 0.0 : importance;
    }

    /**
     * 거리 기반 점수(0~10) 계산
     */
    public double calculateTeamRegionWeight(KboTeam kboTeam, Region userRegion, double maxDistance) {
        KboRegionDistance distance = regionDistanceRepository
                .findByRegionAndTeam(userRegion, kboTeam)
                .orElseThrow(() -> new IllegalStateException(
                        "거리 데이터 없음: " + userRegion + " - " + kboTeam.getTeamName()
                ));

        return calculator.calculateRegionWeight(distance.getDistanceKm(), maxDistance);
    }
}
