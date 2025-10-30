package org.example.backend.common.weight.dto;

import org.example.backend.common.weight.entity.WeightType;

public record UserF1RecommendRequest(
        WeightType teamRecordPreference,
        double teamRecordImportance,

        WeightType driverRecordPreference,
        double driverRecordImportance,

        WeightType heritagePreference,
        double heritageImportance,

        WeightType underdogSpiritPreference,
        double underdogSpiritImportance,

        WeightType franchiseStarPreference,
        double franchiseStarImportance,

        WeightType fandomPreference,
        double fandomImportance
) { }
