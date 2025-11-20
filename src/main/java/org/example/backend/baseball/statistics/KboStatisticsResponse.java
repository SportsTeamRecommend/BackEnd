package org.example.backend.baseball.statistics;

public record KboStatisticsResponse(
        String team,
        Long recommended,
        Double likedPercentage
) {
}
