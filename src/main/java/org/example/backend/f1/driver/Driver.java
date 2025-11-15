package org.example.backend.f1.driver;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.backend.f1.crawling.dto.DriverCrawlingDto;
import org.example.backend.f1.team.F1Team;
import org.example.backend.f1.crawling.Parser;

@Entity
@Getter
@NoArgsConstructor
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String dateOfBirth;
    private String imageUrl;
    private String nationality;
    private Integer debutYear;
    private Integer seasonPolls;
    private Integer seasonPosition;
    private Integer seasonPoint;
    private Integer seasonWins;
    private Integer seasonPodiums;
    private Integer careerWins;
    private Integer careerPodiums;
    private Integer worldChampionship;
    private Double avgPoints;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private F1Team team;

    public Driver(DriverCrawlingDto driverCrawlingDto) {
        this.name = driverCrawlingDto.name();
        this.team = driverCrawlingDto.team();
        this.dateOfBirth = driverCrawlingDto.dateOfBirth();
        this.imageUrl = driverCrawlingDto.imageUrl();
        this.nationality = driverCrawlingDto.nationality();
        this.debutYear = null;
        this.seasonPolls = Parser.parseAsInteger(driverCrawlingDto.seasonPolls());
        this.seasonPosition = Parser.parseAsInteger(driverCrawlingDto.seasonPosition());
        this.seasonPoint = Parser.parseAsInteger(driverCrawlingDto.seasonPoint());
        this.seasonWins = Parser.parseAsInteger(driverCrawlingDto.seasonWins());
        this.seasonPodiums = Parser.parseAsInteger(driverCrawlingDto.seasonPodiums());
        this.careerWins = Parser.parseAsInteger(driverCrawlingDto.careerWins());
        this.careerPodiums = Parser.parseAsInteger(driverCrawlingDto.careerPodiums());
        this.worldChampionship = Parser.parseAsInteger(driverCrawlingDto.worldChampionship());
        this.avgPoints = driverCrawlingDto.avgPoints();
    }

    public void updateInfo(DriverCrawlingDto driverCrawlingDto) {
        this.name = driverCrawlingDto.name();
        this.team = driverCrawlingDto.team();
        this.dateOfBirth = driverCrawlingDto.dateOfBirth();
        this.imageUrl = driverCrawlingDto.imageUrl();
        this.nationality = driverCrawlingDto.nationality();
        this.debutYear = null;
        this.seasonPolls = Parser.parseAsInteger(driverCrawlingDto.seasonPolls());
        this.seasonPosition = Parser.parseAsInteger(driverCrawlingDto.seasonPosition());
        this.seasonPoint = Parser.parseAsInteger(driverCrawlingDto.seasonPoint());
        this.seasonWins = Parser.parseAsInteger(driverCrawlingDto.seasonWins());
        this.seasonPodiums = Parser.parseAsInteger(driverCrawlingDto.seasonPodiums());
        this.careerWins = Parser.parseAsInteger(driverCrawlingDto.careerWins());
        this.careerPodiums = Parser.parseAsInteger(driverCrawlingDto.careerPodiums());
        this.worldChampionship = Parser.parseAsInteger(driverCrawlingDto.worldChampionship());
        this.avgPoints = driverCrawlingDto.avgPoints();
    }
}
