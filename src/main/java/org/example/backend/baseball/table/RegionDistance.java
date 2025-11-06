package org.example.backend.baseball.table;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.baseball.team.Region;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegionDistance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_code", nullable = false)
    private Team team;

    @Column(nullable = false)
    private Double distanceKm;
}
