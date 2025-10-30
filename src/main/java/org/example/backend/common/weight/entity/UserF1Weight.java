package org.example.backend.common.weight.entity;

import org.example.backend.common.weight.dto.UserF1RecommendRequest;

public class UserF1Weight {

    WeightType teamRecordPreference;
    double teamRecordImportance;

    WeightType driverRecordPreference;
    double driverRecordImportance;

    WeightType heritagePreference;
    double heritageImportance;

    WeightType underdogSpiritPreference;
    double underdogSpiritImportance;

    WeightType franchiseStarPreference;
    double franchiseStarImportance;

    WeightType fandomPreference;
    double fandomImportance;

    public UserF1Weight(UserF1RecommendRequest userF1RecommendRequest) {
        this.teamRecordPreference = userF1RecommendRequest.teamRecordPreference();
        this.teamRecordImportance = userF1RecommendRequest.teamRecordImportance();
        this.driverRecordPreference = userF1RecommendRequest.driverRecordPreference();
        this.driverRecordImportance = userF1RecommendRequest.driverRecordImportance();
        this.heritagePreference = userF1RecommendRequest.heritagePreference();
        this.heritageImportance = userF1RecommendRequest.heritageImportance();
        this.underdogSpiritPreference = userF1RecommendRequest.underdogSpiritPreference();
        this.underdogSpiritImportance = userF1RecommendRequest.underdogSpiritImportance();
        this.franchiseStarPreference = userF1RecommendRequest.franchiseStarPreference();
        this.franchiseStarImportance = userF1RecommendRequest.franchiseStarImportance();
        this.fandomPreference = userF1RecommendRequest.fandomPreference();
        this.fandomImportance = userF1RecommendRequest.fandomImportance();
    }

}
