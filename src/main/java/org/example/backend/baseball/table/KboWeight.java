package org.example.backend.baseball.table;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class KboWeight {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_code", referencedColumnName = "teamCode", nullable = false, unique = true)
    private KboTeam kboTeam;

    private double record;        // 팀 성적
    private double legacy;        // 근본
    private double franchiseStar; // 프랜차이즈 스타
    private double growth;        // 성장 가능성
    private double homeGround;        // 연고지
    private double fandom;        // 팬덤 규모

}
