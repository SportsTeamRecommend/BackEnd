package org.example.backend.f1.table;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class F1Metrics {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double teamPoint;
    private double teamRank;

    @OneToOne(fetch = FetchType.LAZY)
    private DriverMetrics driver1;
    @OneToOne(fetch = FetchType.LAZY)
    private DriverMetrics driver2;

    private Integer legacy;
    private Double fandom;
    private Double lastYearPoint;
    private Double currentYearPoint;

    public void update(
            Double teamRank,
            Double teamPoint,
            DriverMetrics driver1,
            DriverMetrics driver2,
            Integer legacy,
            Double lastYearPoint,
            Double fandom,
            Double currentYearPoint
    ) {
        this.teamRank = teamRank;
        this.teamPoint = teamPoint;
        this.driver1 = driver1;
        this.driver2 = driver2;
        this.legacy = legacy;
        this.lastYearPoint = lastYearPoint;
        this.fandom = fandom;
        this.currentYearPoint = currentYearPoint;
    }
}
