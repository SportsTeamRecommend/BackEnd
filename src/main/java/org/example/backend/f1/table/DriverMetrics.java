package org.example.backend.f1.table;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class DriverMetrics {
    @Id
    private Long id;

    private String name;
    private double record;

    private Boolean isFranchise;
    private Integer tenure;

    public void update(double record, Boolean isFranchise, Integer tenure) {
        this.record = record;
        this.isFranchise = isFranchise;
        this.tenure = tenure;
    }
}

