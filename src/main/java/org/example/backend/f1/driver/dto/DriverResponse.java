package org.example.backend.f1.driver.dto;

public record DriverResponse(
        String name,
        String dateOfBirth,
        String imageUrl,
        String seasonPosition,
        String seasonPoint,
        String seasonWins,
        String seasonPodiums,
        String careerWins,
        String careerPodiums,
        String driverChampionship
)
{ }
