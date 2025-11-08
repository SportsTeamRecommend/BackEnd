package org.example.backend.baseball.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Region {
    SEOUL("서울"),
    INCHEON("인천"),
    GYEONGGI("경기"),
    GANGWON("강원"),
    CHUNGBUK("충북"),
    CHUNGNAM("충남"),
    SEJONG("세종"),
    DAEJEON("대전"),
    DAEGU("대구"),
    GWANGJU("광주"),
    JEONBUK("전북"),
    JEONNAM("전남"),
    GYEONGBUK("경북"),
    GYEONGNAM("경남"),
    ULSAN("울산"),
    BUSAN("부산"),
    JEJU("제주");

    private final String koreanName;
}
