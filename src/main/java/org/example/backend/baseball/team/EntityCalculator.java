package org.example.backend.baseball.team;

import org.springframework.stereotype.Component;

// 모든 팀의 해당 값이 같을 때 5.0를 반환
@Component
public class EntityCalculator {

    public double calculateRecordWeight(double avgRank) {
        return 11 - avgRank;
    }

    public double calculateLegacyWeight(int foundedYear, int currentYear, int maxFoundedYear) {
        if (currentYear == maxFoundedYear) return 5.0;
        return ((double) (currentYear - foundedYear) / (currentYear - maxFoundedYear)) * 9 + 1;
    }

    public double calculateFranchiseStarWeight(int starCount, int minStar, int maxStar) {
        if (maxStar == minStar) return 5.0;
        return ((double) (starCount - minStar) / (maxStar - minStar)) * 9 + 1;
    }

    public double calculateGrowthWeight(double avgAge, double minAge, double maxAge) {
        if (maxAge == minAge) return 5.0;
        return ((maxAge - avgAge) / (maxAge - minAge)) * 9 + 1;
    }

    public double calculateRegionWeight(double distance, double maxDistance) {
        if (maxDistance == 0) return 5.0;
        return ((maxDistance - distance) / maxDistance) * 9 + 1;
    }

    public double calculateFandomWeight(double followers, double minFollower, double maxFollower) {
        if (maxFollower == minFollower) return 5.0;
        return ((followers - minFollower) / (maxFollower - minFollower)) * 9 + 1;
    }
}
