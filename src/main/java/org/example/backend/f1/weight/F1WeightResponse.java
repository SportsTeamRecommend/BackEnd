package org.example.backend.f1.weight;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class F1WeightResponse {

    private String teamName;
    private double teamRecord;
    private double driverRecord;
    private double legacy;
    private double franchiseStar;
    private double underdog;
    private double fandom;
}
