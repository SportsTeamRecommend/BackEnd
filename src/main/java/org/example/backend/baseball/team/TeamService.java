package org.example.backend.baseball.team;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.table.Team;
import org.example.backend.baseball.table.KboWeight;
import org.example.backend.baseball.weight.KboWeightRepository;
import org.example.backend.baseball.weight.KboWeightService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final EntityCalculator calculator;
    private final KboWeightRepository weightRepository;
    private final RegionDistanceRepository regionDistanceRepository;
    private final KboWeightService kboWeightService;

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

    /**
    * kbo_weight의 값을 계산하는 메서드
    */
    public void calculateEntityWeight() {
        int currentYear = LocalDate.now().getYear();

        int minStar = teamRepository.findMinStarPlayerCount();
        int maxStar = teamRepository.findMaxStarPlayerCount();

        double minAge = teamRepository.findMinAverageAge();
        double maxAge = teamRepository.findMaxAverageAge();

        double minFan = teamRepository.findMinFanScale();
        double maxFan = teamRepository.findMaxFanScale();

        int maxFoundedYear = teamRepository.findNewestFoundedYear();

        List<Team> teams = teamRepository.findAll();
        List<KboWeight> weights = new ArrayList<>();

        for (Team team : teams) {
            KboWeight weight = new KboWeight();
            weight.setTeam(team);

            weight.setRecord(calculator.calculateRecordWeight(team.getAvgRank()));
            weight.setLegacy(calculator.calculateLegacyWeight(team.getFoundedYear(), currentYear, maxFoundedYear));
            weight.setFranchiseStar(calculator.calculateFranchiseStarWeight(team.getStarPlayerCount(), minStar, maxStar));
            weight.setGrowth(calculator.calculateGrowthWeight(team.getAverageAge(), minAge, maxAge));

            // TODO : 이게 맞나?
            weight.setHomeGround(0.0);
            weight.setFandom(calculator.calculateFandomWeight(team.getFanScale(), minFan, maxFan));

            weights.add(weight);
        }

        weightRepository.saveAll(weights);
    }
}
