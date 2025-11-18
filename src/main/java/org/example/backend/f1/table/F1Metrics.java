package org.example.backend.f1.table;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor @NoArgsConstructor
public class F1Metrics {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double teamRecord;

    @OneToOne(fetch = FetchType.LAZY)
    private DriverMetrics driver1;
    @OneToOne(fetch = FetchType.LAZY)
    private DriverMetrics driver2;

    private Integer legacy;
    private Double fandom;
    private Double lastYearPoint;
    private Double currentYearPoint;
}
