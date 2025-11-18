package org.example.backend.f1.table;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor @NoArgsConstructor
public class DriverMetrics {
    @Id
    private Long id;

    private String name;
    private double record;

    private Boolean isFranchise;
    private Integer tenure;
}

