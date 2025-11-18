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

    private double normalize(double value, double min, double max) {
        if (max == min) return 5.0; // 모두 동일한 값일 경우
        return ((value - min) / (max - min)) * 9 + 1;  // 1~10
    }

    public double calculateFandom(double followers, double minFollower, double maxFollower) {
        return normalize(followers, minFollower, maxFollower);
    }

    public double calculateKboRecord(double avgRank) {
        return 11 - avgRank;
    }

    public double calculateKboLegacy(int foundedYear, int currentYear, int minFoundedYear) {
        return normalize(currentYear - foundedYear, currentYear - minFoundedYear, currentYear - minFoundedYear);
    }

    public double calculateKboFranchise(int starCount, int minStar, int maxStar) {
        return normalize(starCount, minStar, maxStar);
    }

    public double calculateKboGrowth(double avgAge, double minAge, double maxAge) {
        if (minAge == maxAge) return 5.0;
        return normalize(maxAge - avgAge, 0, maxAge - minAge);
    }

    public double calculateKboRegion(double distance, double maxDistance) {
        return maxDistance == 0 ? 5.0 : normalize(maxDistance - distance, 0, maxDistance);
    }

    public double calculateF1TeamRecord(double totalPoints, double min, double max) {
        return normalize(totalPoints, min, max);
    }

    public double calculateF1DriverRecord(double driverAvgPoints, double min, double max) {
        return normalize(driverAvgPoints, min, max);
    }

    public double calculateF1Legacy(int joinYears, int minYears, int maxYears) {
        return normalize(joinYears, minYears, maxYears);
    }

    public double calculateF1FranchiseStar(double tenure, double minTenure, double maxTenure) {
        if (minTenure == maxTenure) return 5.0;
        double ratio = (tenure - minTenure) / (maxTenure - minTenure);
        return 5 + (ratio * 5);
    }

    public double calculateF1Underdog(double increase, double minIncrease, double maxIncrease) {
        if (increase < 0) return 1.0;
        if (minIncrease == maxIncrease) return 5.0;
        double ratio = (increase - minIncrease) / (maxIncrease - minIncrease);
        return 1 + (ratio * 9);
    }

    public double calculateF1Fandom(double followers, double min, double max) {
        return normalize(followers, min, max);
    }
}
