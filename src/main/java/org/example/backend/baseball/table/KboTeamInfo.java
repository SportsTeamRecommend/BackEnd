package org.example.backend.baseball.table;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;

@Entity
public class KboTeamInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;
    private int leagueWins;
    private int koreaSeasonWins;
    private int postSeason;
    private double avgRank;
    private int seasonRank;
    private double winRate;
    private double battingAverage;
    private double earnedRunAverage;

    @OneToOne
    KboTeam kboTeam;
    @OneToMany
    List<KboPlayerInfo> kboPlayers;




}
