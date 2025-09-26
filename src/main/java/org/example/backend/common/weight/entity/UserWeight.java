package org.example.backend.common.weight.entity;

public class UserWeight {

    private double record;        // 팀 성적 선호도
    private double legacy;        // 근본 선호도
    private double franchiseStar; // 프랜차이즈 스타 선호도
    private double growth;        // 성장 가능성 선호도
    private double region;        // 연고지 선호도
    private double fandom;        // 팬덤 규모 선호도

    public double[] toVector() {
        return new double[]{record, legacy, franchiseStar, growth, region, fandom};
    }

}
