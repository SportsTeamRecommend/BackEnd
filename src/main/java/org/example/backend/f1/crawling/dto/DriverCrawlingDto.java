package org.example.backend.f1.crawling.dto;

import org.example.backend.f1.team.F1Team;

public record DriverCrawlingDto(
        String name,
        F1Team team,
        String dateOfBirth,
        String imageUrl,
        String seasonPosition,
        String seasonPoint,
        String seasonWins,
        String seasonPodiums,
        String careerWins,
        String careerPodiums,
        String worldChampionship,
        Double avgPoints
)
{ }
