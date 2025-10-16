package org.example.backend.common.weight.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import org.example.backend.baseball.team.Team;

@Entity
@Getter
public class F1TeamWeight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "team_id", nullable = false, unique = true)
    private Team team;

    private double teamRecord;
    private double driverRecord;
    private double heritage;
    private double franchiseStar;
    private double underdogSpirit;
    private double fandom;

    public double[] toVector() {
        return new double[]{teamRecord, driverRecord, heritage, franchiseStar, underdogSpirit, fandom};
    }

}
