package org.example.backend.f1.team;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.backend.f1.driver.Driver;
import org.example.backend.f1.crawling.dto.F1TeamCrawlingDto;
import org.example.backend.f1.crawling.Parser;

@Entity
@Getter
@NoArgsConstructor
public class F1Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String imageUrl;
    private String videoUrl = null;
    private Integer seasonPosition;
    private Double seasonPoint;
    private Integer seasonWins;
    private Integer seasonPodiums;
    private Integer totalWins;
    private Integer totalPodiums;
    private Integer constructorChampionship;
    private Double avgRank;

    @OneToMany(mappedBy = "team")
    private List<Driver> drivers = new ArrayList<>();

    public F1Team(F1TeamCrawlingDto teamCrawlingDto) {
        name = teamCrawlingDto.name();
        imageUrl = teamCrawlingDto.logoUrl();
        seasonPosition = Parser.parseAsInteger(teamCrawlingDto.seasonPosition());
        seasonPoint = Parser.parseAsDouble(teamCrawlingDto.seasonPoint());
        seasonWins = Parser.parseAsInteger(teamCrawlingDto.seasonWins());
        seasonPodiums = Parser.parseAsInteger(teamCrawlingDto.seasonPodiums());
        totalWins = Parser.parseAsInteger(teamCrawlingDto.careerWins());
        totalPodiums = Parser.parseAsInteger(teamCrawlingDto.careerPodiums());
        constructorChampionship = Parser.parseAsInteger(teamCrawlingDto.worldChampionship());
        this.avgRank = teamCrawlingDto.avgRank();
    }

    public void updateInfo(F1TeamCrawlingDto teamCrawlingDto) {
        name = teamCrawlingDto.name();
        imageUrl = teamCrawlingDto.logoUrl();
        seasonPosition = Parser.parseAsInteger(teamCrawlingDto.seasonPosition());
        seasonPoint = Parser.parseAsDouble(teamCrawlingDto.seasonPoint());
        seasonWins = Parser.parseAsInteger(teamCrawlingDto.seasonWins());
        seasonPodiums = Parser.parseAsInteger(teamCrawlingDto.seasonPodiums());
        totalWins = Parser.parseAsInteger(teamCrawlingDto.careerWins());
        totalPodiums = Parser.parseAsInteger(teamCrawlingDto.careerPodiums());
        constructorChampionship = Parser.parseAsInteger(teamCrawlingDto.worldChampionship());
        this.avgRank = teamCrawlingDto.avgRank();
    }
}
