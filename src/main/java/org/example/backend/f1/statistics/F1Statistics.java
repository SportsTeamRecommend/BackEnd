package org.example.backend.f1.statistics;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import org.example.backend.f1.team.F1Team;

@Entity
@Getter
public class F1Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Double likedPercentage = 0.0;
    private Long liked = 0L;
    private Long recommended = 0L;

    @OneToOne
    private F1Team team;


    public F1Statistics(F1Team team, double likedPercentage, long liked, long recommended) {
        this.team = team;
        this.likedPercentage = likedPercentage;
        this.liked = liked;
        this.recommended = recommended;
    }

    public F1Statistics() {

    }

    public F1Statistics(F1Team team) {
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
