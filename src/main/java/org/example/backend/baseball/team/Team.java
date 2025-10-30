package org.example.backend.baseball.team;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend.baseball.crawling.dto.CrawledPlayer;
import org.example.backend.baseball.weight.KboTeamWeight;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Team {

    @Id
    private String teamCode; // "HH", "LG", "LT", "HT", "SS", "SK", "KT", "NC", "OB", "WO"

    private String teamName; // SSG 랜더스
    private Double averageAge;

    private Double avgRank;
    private Integer currentRank;
    private Double pastAvgRank;

    @Transient
    private List<CrawledPlayer> crawledPlayers = new ArrayList<>();

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL)
    private KboTeamWeight weight;

    public Team(String teamCode, String teamName) {
        this.teamCode = teamCode;
        this.teamName = teamName;
    }

    public void addPlayer(CrawledPlayer crawledPlayer) {
        crawledPlayers.add(crawledPlayer);
    }
}
