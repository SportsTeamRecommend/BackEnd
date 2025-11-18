package org.example.backend.common.compare;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.backend.f1.table.F1Metrics;

@Getter
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
