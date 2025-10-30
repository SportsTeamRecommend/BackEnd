package org.example.backend.common.weight.dto;

import org.example.backend.common.weight.entity.WeightType;

public record UserKboRecommendRequest(
        WeightType recordPreference,
        double recordImportance,

        WeightType legacyPreference,
        double legacyImportance,

        WeightType franchiseStarPreference,
        double franchiseStarImportance,

        WeightType growthPreference,
        double growthImportance,

        WeightType regionPreference,
        double regionImportance,

        WeightType fandomPreference,
        double fandomImportance
) { }
