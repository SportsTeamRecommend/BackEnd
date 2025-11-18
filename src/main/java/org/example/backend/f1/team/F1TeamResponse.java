package org.example.backend.f1.team;

import java.util.List;
import org.example.backend.f1.driver.DriverResponse;

public record F1TeamResponse(
        String name,
        String logoUrl,
        String videoUrl,
        Integer seasonPosition,
        Double seasonPoint,
        Integer seasonWins,
        Integer seasonPodiums,
        Integer careerWins,
        Integer careerPodiums,
        Integer worldChampionship,
        List<DriverResponse> drivers
) { }
