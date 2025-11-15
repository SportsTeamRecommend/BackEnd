package org.example.backend.f1.driver;

public record DriverResponse(
        String name,
        String dateOfBirth,
        String imageUrl,
        String nationality,
        Integer debutYear,
        Integer seasonPolls,
        Integer seasonPosition,
        Integer seasonPoint,
        Integer seasonWins,
        Integer seasonPodiums,
        Integer careerWins,
        Integer careerPodiums,
        Integer driverChampionship
)
{ }
