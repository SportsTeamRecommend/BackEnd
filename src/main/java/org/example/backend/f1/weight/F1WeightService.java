package org.example.backend.f1.weight;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.example.backend.common.weight.dto.F1RecommendResponse;
import org.example.backend.common.weight.WeightType;
import org.example.backend.f1.table.F1Weight;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class F1WeightService {

    public List<F1RecommendResponse> getF1WeightRanks(
            List<F1Weight> teamWeights,
            UserF1Weight userWeight
    ) {
        List<F1RecommendResponse> rankedList = new ArrayList<>();

        for (F1Weight weight : teamWeights) {
            double score = calculateScore(weight, userWeight);
            rankedList.add(new F1RecommendResponse(weight.getMetrics().getName(), score));
        }

        rankedList.sort((a, b) -> Double.compare(b.result(), a.result()));
        return rankedList;
    }

    private double calculateScore(F1Weight t, UserF1Weight u) {

        double totalScore = 0.0;
        double totalImportance = 0.0;

        // 팀 성적
        totalScore += weighted(t.getTeamRecord(), u.getTeamRecordPreference(), u.getTeamRecordImportance());
        totalImportance += importance(u.getTeamRecordPreference(), u.getTeamRecordImportance());

        // 드라이버 성적
        totalScore += weighted(t.getDriverRecord(), u.getDriverRecordPreference(), u.getDriverRecordImportance());
        totalImportance += importance(u.getDriverRecordPreference(), u.getDriverRecordImportance());

        // 근본
        totalScore += weighted(t.getLegacy(), u.getLegacyPreference(), u.getLegacyImportance());
        totalImportance += importance(u.getLegacyPreference(), u.getLegacyImportance());

        // 프랜차이즈 스타
        totalScore += weighted(t.getFranchiseStar(), u.getFranchiseStarPreference(), u.getFranchiseStarImportance());
        totalImportance += importance(u.getFranchiseStarPreference(), u.getFranchiseStarImportance());

        // 언더독
        totalScore += weighted(t.getUnderdog(), u.getUnderdogPreference(), u.getUnderdogImportance());
        totalImportance += importance(u.getUnderdogPreference(), u.getUnderdogImportance());

        // 팬덤
        totalScore += weighted(t.getFandom(), u.getFandomPreference(), u.getFandomImportance());
        totalImportance += importance(u.getFandomPreference(), u.getFandomImportance());

        if (totalImportance == 0.0) return 0.0;

        double weightedAvg = totalScore / totalImportance;
        double softBoost = 1 - Math.exp(-totalImportance / 10.0);

        return weightedAvg * softBoost * 100.0;
    }

    /** 0~10 점수 → 가중 normalized 계산 */
    private double weighted(double value10, WeightType preference, double importance) {
        if (preference == WeightType.NONE) return 0.0;

        double normalized = value10 / 10.0;

        if (preference == WeightType.LOW)
            normalized = 1.0 - normalized;

        return normalized * importance;
    }

    /** NONE이면 중요도 0 처리 */
    private double importance(WeightType preference, double importance) {
        return preference == WeightType.NONE ? 0.0 : importance;
    }
}
