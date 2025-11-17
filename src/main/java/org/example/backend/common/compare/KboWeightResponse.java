package org.example.backend.common.compare;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KboWeightResponse {

    private String teamName;
    private double record;        // 팀 성적
    private double legacy;        // 근본
    private double franchiseStar; // 프랜차이즈 스타
    private double growth;        // 성장 가능성
    private double fandom;        // 팬덤 규모
}
