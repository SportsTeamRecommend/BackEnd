package org.example.backend.f1.table;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class F1Weight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "metrics_id", nullable = false, unique = true)
    private F1Metrics metrics;

    private double teamRecord;
    private double driverRecord;
    private double legacy;
    private double franchiseStar;
    private double underdog;
    private double fandom;
}
