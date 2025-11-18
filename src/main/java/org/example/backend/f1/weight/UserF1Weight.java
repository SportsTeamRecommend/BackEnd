package org.example.backend.f1.weight;

import lombok.Getter;
import org.example.backend.common.weight.WeightType;

@Getter
public class UserF1Weight {

    private WeightType teamRecordPreference;
    private double teamRecordImportance;

    private WeightType driverRecordPreference;
    private double driverRecordImportance;

    private WeightType legacyPreference;
    private double legacyImportance;

    private WeightType underdogPreference;
    private double underdogImportance;

    private WeightType franchiseStarPreference;
    private double franchiseStarImportance;

    private WeightType fandomPreference;
    private double fandomImportance;

}
