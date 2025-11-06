package org.example.backend.baseball.team;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.table.Team;
import org.example.backend.baseball.table.KboTeamWeight;
import org.example.backend.baseball.weight.KboTeamWeightRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final EntityCalculator calculator;
    private final KboTeamWeightRepository weightRepository;

    @Transactional // dirty checking 용
    public void updateTeamRank() {
        var teams = teamRepository.findAll();

        for (Team team : teams) {
            if (team.getPastAvgRank() != null && team.getCurrentRank() != null) {
                double avg = (team.getPastAvgRank() * 2 + team.getCurrentRank()) / 3.0;
                team.setAvgRank(avg);
            }
        }
    }

    public void calculateEntityWeight(){
        List<Team> teams = teamRepository.findAll();

        int minStar = 1, maxStar = 4;
        double minAge = 26.55, maxAge = 29.82;
        double minFollower = 9.9, maxFollower = 38.0;
        double maxDistance = 10.0;

        for (Team team : teams) {
            KboTeamWeight weight = new KboTeamWeight();
            weight.setTeam(team);

            weight.setRecord(calculator.calculateRecordWeight(team.getAvgRank()));
            weight.setLegacy(calculator.calculateLegacyWeight(team.getFoundedYear()));
            weight.setFranchiseStar(calculator.calculateFranchiseStarWeight(team.getStarPlayerCount(), minStar, maxStar));
            weight.setGrowth(calculator.calculateGrowthWeight(team.getAverageAge(), minAge, maxAge));
            weight.setRegion(calculator.calculateRegionWeight(team.getFanScale(), maxDistance));
            weight.setFandom(calculator.calculateFandomWeight(team.getFanScale(), minFollower, maxFollower));

            weightRepository.save(weight);
        }
    }
}
