package org.example.backend.baseball.team;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.table.Team;
import org.example.backend.baseball.table.KboWeight;
import org.example.backend.baseball.weight.KboWeightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final EntityCalculator calculator;
    private final KboWeightRepository weightRepository;

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
    @Transactional
    public void calculateEntityWeight() {
        int currentYear = LocalDate.now().getYear();

        int minStar = teamRepository.findMinStarPlayerCount();
        int maxStar = teamRepository.findMaxStarPlayerCount();

        double minAge = teamRepository.findMinAverageAge();
        double maxAge = teamRepository.findMaxAverageAge();

        double minFan = teamRepository.findMinFanScale();
        double maxFan = teamRepository.findMaxFanScale();

        int maxFoundedYear = teamRepository.findOldestFoundedYear();

        List<Team> teams = teamRepository.findAll();

        for (Team team : teams) {
            // 계산된 가중치 생성
            double record = calculator.calculateRecordWeight(team.getAvgRank());
            double legacy = calculator.calculateLegacyWeight(team.getFoundedYear(), currentYear, maxFoundedYear);
            double franchiseStar = calculator.calculateFranchiseStarWeight(team.getStarPlayerCount(), minStar, maxStar);
            double growth = calculator.calculateGrowthWeight(team.getAverageAge(), minAge, maxAge);
            double homeGround = 0.0;
            double fandom = calculator.calculateFandomWeight(team.getFanScale(), minFan, maxFan);

            // 이미 해당 팀코드의 KboWeight가 존재하는지 확인
            Optional<KboWeight> existingOpt = weightRepository.findByTeam_TeamCode(team.getTeamCode());

            if (existingOpt.isPresent()) { // 이후로는 update
                KboWeight existing = existingOpt.get();
                existing.setRecord(record);
                existing.setLegacy(legacy);
                existing.setFranchiseStar(franchiseStar);
                existing.setGrowth(growth);
                existing.setHomeGround(homeGround);
                existing.setFandom(fandom);
            } else { // 최초에 insert
                KboWeight newWeight = new KboWeight();
                newWeight.setTeam(team);
                newWeight.setRecord(record);
                newWeight.setLegacy(legacy);
                newWeight.setFranchiseStar(franchiseStar);
                newWeight.setGrowth(growth);
                newWeight.setHomeGround(homeGround);
                newWeight.setFandom(fandom);
                weightRepository.save(newWeight);
            }
        }
    }

}
