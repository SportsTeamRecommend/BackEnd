package org.example.backend.baseball.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BallPark {
    JAMSIL("잠실", "LG/두산"),
    GOCHUK("고척", "키움"),
    MUNHAK("문학", "SSG"),
    SUWON("수원", "KT"),
    DAEJEON("대전", "한화"),
    DAEGU("대구", "삼성"),
    GWANGJU("광주", "KIA"),
    CHANGWON("창원", "NC"),
    SAJIK("사직", "롯데");

    private final String koreanName;
    private final String teamName;
}
