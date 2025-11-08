package org.example.backend.f1.team.dto;

import java.util.List;
import org.example.backend.f1.driver.dto.DriverResponse;

public record F1TeamResponse(
        String name,
        String logoUrl,
        Integer seasonPosition,
        Integer seasonPoint,
        Integer seasonWins,
        Integer seasonPodiums,
        Integer careerWins,
        Integer careerPodiums,
        Integer worldChampionship,
        List<DriverResponse> drivers
) { }
