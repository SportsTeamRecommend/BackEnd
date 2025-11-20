package org.example.backend.baseball.statistics;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import org.example.backend.baseball.table.KboTeam;
import org.example.backend.f1.team.F1Team;

@Getter
@Entity
public class KboStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Double likedPercentage = 0.0;
    private Long liked = 0L;
    private Long recommended = 0L;

    @OneToOne
    private KboTeam team;

    public KboStatistics(KboTeam team, double likedPercentage, long liked, long recommended) {
        this.team = team;
        this.likedPercentage = likedPercentage;
        this.liked = liked;
        this.recommended = recommended;
    }

    public KboStatistics() {

    }

    public KboStatistics(KboTeam team) {
    }

    public void incrementLiked() {
        this.liked++;
        this.updateLikedPercentage();
    }

    public void incrementRecommended() {
        this.recommended++;
        this.updateLikedPercentage();
    }

    private void updateLikedPercentage() {
        if(this.recommended != 0)
            this.likedPercentage = this.liked / (double)this.recommended * 100;
        else
            this.likedPercentage = 0.0;
    }
}
