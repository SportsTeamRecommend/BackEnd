package org.example.backend.f1.statistics;

public record F1StatisticsResponse(
        String team,
        Long recommended,
        Double likedPercentage
) { }
