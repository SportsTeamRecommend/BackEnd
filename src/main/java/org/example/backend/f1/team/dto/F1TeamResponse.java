package org.example.backend.f1.team.dto;

import java.util.List;
import org.example.backend.f1.driver.dto.DriverResponse;

public record F1TeamResponse(
        String name,
        String logoUrl,
        String seasonPosition,
        String seasonPoint,
        String seasonWins,
        String seasonPodiums,
        String careerWins,
        String careerPodiums,
        String worldChampionship,
        List<DriverResponse> drivers
) { }
