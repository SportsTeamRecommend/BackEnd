package org.example.backend.f1.driver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;
import org.example.backend.f1.driver.dto.DriverCrawlingDto;
import org.example.backend.f1.team.entity.F1Team;

@Entity
@NoArgsConstructor
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String dateOfBirth;
    private String imageUrl;
    private String seasonPosition;
    private String seasonPoint;
    private String seasonWins;
    private String seasonPodiums;
    private String careerWins;
    private String careerPodiums;
    private String worldChampionship;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private F1Team f1Team;

    public Driver(DriverCrawlingDto driverCrawlingDto) {
        this.name = driverCrawlingDto.name();
        this.f1Team = driverCrawlingDto.f1Team();
        this.dateOfBirth = driverCrawlingDto.dateOfBirth();
        this.imageUrl = driverCrawlingDto.imageUrl();
        this.seasonPosition = driverCrawlingDto.seasonPosition();
        this.seasonPoint = driverCrawlingDto.seasonPoint();
        this.seasonWins = driverCrawlingDto.seasonWins();
        this.seasonPodiums = driverCrawlingDto.seasonPodiums();
        this.careerWins = driverCrawlingDto.careerWins();
        this.careerPodiums = driverCrawlingDto.careerPodiums();
        this.worldChampionship = driverCrawlingDto.worldChampionship();
    }
}
