package org.example.backend.baseball.weight;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
@AllArgsConstructor
public class KboWeightResponse {

    private String teamName;
    private double record;        // 팀 성적
    private double legacy;        // 근본
    private double franchiseStar; // 프랜차이즈 스타
    private double growth;        // 성장 가능성
    private double fandom;        // 팬덤 규모
}
