package org.example.backend.baseball.team;

import org.springframework.stereotype.Component;

/**
 * KBO 구단 데이터 기반 가중치 계산 클래스
 * 공식 출처: (현재연도 - 창단연도) / (현재연도 - 최대연도) × 9 + 1 등
 */
@Component
public class EntityCalculator {

    private static final int CURRENT_YEAR = 2025;
    private static final int MAX_FOUNDED_YEAR = 2013;  // 가장 최근 창단 (KT)
    private static final int MIN_FOUNDED_YEAR = 1982;  // 가장 오래된 팀 (롯데, LG 등)

    /**
     * 팀 성적 가중치 (11 - 팀 성적)
     */
    public double calculateRecordWeight(double avgRank) {
        return 11 - avgRank;
    }

    /**
     * 근본 가중치
     * (현재연도 - 창단연도) / (현재연도 - 최대연도) × 9 + 1
     */
    public double calculateLegacyWeight(int foundedYear) {
        return ((double) (CURRENT_YEAR - foundedYear) / (CURRENT_YEAR - MAX_FOUNDED_YEAR)) * 9 + 1;
    }

    /**
     * 프랜차이즈 스타 가중치
     * (해당팀 스타 수 - 최소 스타 수) / (최대 스타 수 - 최소 스타 수) × 9 + 1
     */
    public double calculateFranchiseStarWeight(int starCount, int minStar, int maxStar) {
        if (maxStar == minStar) return 5.0; // division by zero 방지용
        return ((double) (starCount - minStar) / (maxStar - minStar)) * 9 + 1;
    }

    /**
     * 성장 가능성 가중치
     * (최대 평균 나이 - 해당팀 평균 나이) / (최대 평균 나이 - 최소 평균 나이) × 9 + 1
     */
    public double calculateGrowthWeight(double avgAge, double minAge, double maxAge) {
        if (maxAge == minAge) return 5.0;
        return ((maxAge - avgAge) / (maxAge - minAge)) * 9 + 1;
    }

    /**
     * 연고지 가중치
     * (최대거리 - 사용자 거리) / 최대거리 × 9 + 1
     * → 예: 서울-부산 거리 기준 10.0
     */
    public double calculateRegionWeight(double distance, double maxDistance) {
        if (maxDistance == 0) return 5.0;
        return ((maxDistance - distance) / maxDistance) * 9 + 1;
    }

    /**
     * 팬덤 가중치
     * (팔로워 수 - 최소 팔로워 수) / (최대 - 최소) × 9 + 1
     */
    public double calculateFandomWeight(double followers, double minFollower, double maxFollower) {
        if (maxFollower == minFollower) return 5.0;
        return ((followers - minFollower) / (maxFollower - minFollower)) * 9 + 1;
    }
}
