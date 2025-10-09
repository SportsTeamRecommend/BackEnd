package org.example.backend.f1.team.dto;

public record F1TeamCrawlingDto(
        String name,
        String logoUrl,
        String seasonPosition,
        String seasonPoint,
        String seasonWins,
        String seasonPodiums,
        String careerWins,
        String careerPodiums,
        String worldChampionship,
        Double avgRank
)
{ }
