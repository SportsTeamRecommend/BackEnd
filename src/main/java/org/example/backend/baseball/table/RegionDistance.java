package org.example.backend.baseball.table;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.baseball.team.BallPark;
import org.example.backend.baseball.team.Region;

/**
 * 지역(Region) ↔ 구장(BallPark) 간 거리 데이터
 * 예: 서울 - 잠실 15km, 서울 - 사직 394km
 */
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

    @Enumerated(EnumType.STRING)
    private BallPark ballPark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_code", nullable = false)
    private Team team;

    @Column(nullable = false)
    private Double distanceKm;
}
