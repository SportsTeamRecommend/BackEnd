package org.example.backend.baseball.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BallPark {
    JAMSIL("잠실", "LG/두산", Region.SEOUL),
    GOCHUK("고척", "키움", Region.SEOUL),
    MUNHAK("문학", "SSG", Region.INCHEON),
    SUWON("수원", "KT", Region.GYEONGGI),
    DAEJEON("대전", "한화", Region.DAEJEON),
    DAEGU("대구", "삼성", Region.DAEGU),
    GWANGJU("광주", "KIA", Region.GWANGJU),
    CHANGWON("창원", "NC", Region.GYEONGNAM),
    SAJIK("사직", "롯데", Region.BUSAN);

    private final String koreanName;
    private final String teamName;
    private final Region region;
}
