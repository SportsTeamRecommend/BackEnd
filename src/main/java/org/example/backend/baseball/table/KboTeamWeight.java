package org.example.backend.baseball.table;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class KboTeamWeight {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "team_code", nullable = false, unique = true)   // Team의 PK를 FK로 씀
    private Team team;

    private double record;        // 팀 성적
    private double legacy;        // 근본
    private double franchiseStar; // 프랜차이즈 스타
    private double growth;        // 성장 가능성
    private double region;        // 연고지
    private double fandom;        // 팬덤 규모

}
