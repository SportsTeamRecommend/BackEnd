package org.example.backend.baseball.table;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class KboPlayerInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String nationality;
    private String debutYear;
    private String dateOfBirth;

    @ManyToOne
    KboTeamInfo kboTeamInfo;
}
