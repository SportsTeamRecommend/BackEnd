package org.example.backend.common.weight;

import org.springframework.stereotype.Component;

/**
 * KBO / F1 공통 계산 로직을 모두 담당하는 계산기
 * - normalize(): 1~10 범위로 정규화 (공통)
 * - KBO 전용 계산 로직 (kboXXX)
 * - F1 전용 계산 로직 (f1XXX)
 */
@Component
public class WeightCalculator {

    private double normalize10(double value, double min, double max) {
        if (max == min) return 5.0; // 모두 동일한 값일 경우
        return (value - min) / (max - min) * 10.0;  // 0~10
    }

    private double normalize5(double value, double min, double max) {
        if (max == min) return 2.5; // 모두 동일한 값일 경우
        return (value - min) / (max - min) * 5.0;  // 0~5
    }

    public double calculateFandom(double followers, double minFollower, double maxFollower) {
        return normalize10(followers, minFollower, maxFollower);
    }

    public double calculateKboRecord(double avgRank) {
        return 11 - avgRank;
    }

    public double calculateKboLegacy(int foundedYear, int currentYear, int minFoundedYear) {
        return normalize10(currentYear - foundedYear, currentYear - minFoundedYear, currentYear - minFoundedYear);
    }

    public double calculateKboFranchise(int starCount, int minStar, int maxStar) {
        return normalize10(starCount, minStar, maxStar);
    }

    public double calculateKboGrowth(double avgAge, double minAge, double maxAge) {
        if (minAge == maxAge) return 5.0;
        return normalize10(maxAge - avgAge, 0, maxAge - minAge);
    }

    public double calculateKboRegion(double distance, double maxDistance) {
        if (maxDistance == 0) return 2.5;
        return normalize5(maxDistance - distance, 0, maxDistance);
    }

    public double calculateF1TeamRecord(double totalPoints, double min, double max) {
        return normalize10(totalPoints, min, max);
    }

    public double calculateF1DriverRecord(double driverAvgPoints, double min, double max) {
        return normalize10(driverAvgPoints, min, max);
    }

    public double calculateF1Legacy(int joinYears, int minYears, int maxYears) {
        return normalize10(joinYears, minYears, maxYears);
    }

    public double calculateF1FranchiseStar(double tenure, double minTenure, double maxTenure) {
        if (minTenure == maxTenure) return 2.5;
        return 5 + normalize5(tenure, minTenure, maxTenure);
    }

    public double calculateF1Underdog(double increase, double minIncrease, double maxIncrease) {
        if (increase < 0) return 1.0;
        return normalize10(increase, minIncrease, maxIncrease);
    }

    public double calculateF1Fandom(double followers, double min, double max) {
        return normalize10(followers, min, max);
    }
}
