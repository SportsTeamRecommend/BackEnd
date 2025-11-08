package org.example.backend.f1.driver;

public record DriverResponse(
        String name,
        String dateOfBirth,
        String imageUrl,
        Integer seasonPosition,
        Integer seasonPoint,
        Integer seasonWins,
        Integer seasonPodiums,
        Integer careerWins,
        Integer careerPodiums,
        Integer driverChampionship
)
{ }
