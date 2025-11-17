package org.example.backend.baseball.table;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend.baseball.crawling.dto.CrawledPlayer;
import org.example.backend.baseball.team.BallPark;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class KboTeam {

    @Id
    private String teamCode; // "HH", "LG", "LT", "HT", "SS", "SK", "KT", "NC", "OB", "WO"

    private String teamName; // SSG 랜더스

    private Double avgRank;
    private Integer currentRank;
    private Double pastAvgRank;

    private Integer foundedYear;

    private Integer starPlayerCount;

    private Double averageAge;

    @Enumerated(EnumType.STRING)
    private BallPark homeTown;

    private Double fanScale;

    @Transient
    private List<CrawledPlayer> crawledPlayers = new ArrayList<>();

    public KboTeam(String teamCode, String teamName) {
        this.teamCode = teamCode;
        this.teamName = teamName;
    }

    public void addPlayer(CrawledPlayer crawledPlayer) {
        crawledPlayers.add(crawledPlayer);
    }
}
