package org.example.backend.f1.team.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.backend.f1.driver.entity.Driver;
import org.example.backend.f1.team.dto.F1TeamCrawlingDto;

@Entity
@Getter
@NoArgsConstructor
public class F1Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String imageUrl;
    private String seasonPosition;
    private String seasonPoint;
    private String seasonWins;
    private String seasonPodiums;
    private String totalWins;
    private String totalPodiums;
    private String constructorChampionship;
    private Double avgRank;

    @OneToMany(mappedBy = "team")
    private List<Driver> drivers = new ArrayList<>();

    public F1Team(F1TeamCrawlingDto teamCrawlingDto) {
        name = teamCrawlingDto.name();
        imageUrl = teamCrawlingDto.logoUrl();
        seasonPosition = teamCrawlingDto.seasonPosition();
        seasonPoint = teamCrawlingDto.seasonPoint();
        seasonWins = teamCrawlingDto.seasonWins();
        seasonPodiums = teamCrawlingDto.seasonPodiums();
        totalWins = teamCrawlingDto.careerWins();
        totalPodiums = teamCrawlingDto.careerPodiums();
        constructorChampionship = teamCrawlingDto.worldChampionship();
        this.avgRank = teamCrawlingDto.avgRank();
    }
}
