package org.example.backend.f1.driver.dto;

import org.example.backend.f1.team.entity.F1Team;

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
