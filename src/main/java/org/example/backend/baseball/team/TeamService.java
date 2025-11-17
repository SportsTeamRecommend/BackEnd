package org.example.backend.baseball.team;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.table.KboTeam;
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

        for (KboTeam kboTeam : teams) {
            if (kboTeam.getPastAvgRank() != null && kboTeam.getCurrentRank() != null) {
                double avg = (kboTeam.getPastAvgRank() * 2 + kboTeam.getCurrentRank()) / 3.0;
                kboTeam.setAvgRank(avg);
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

        List<KboTeam> kboTeams = teamRepository.findAll();

        for (KboTeam kboTeam : kboTeams) {
            // 계산된 가중치 생성
            double record = calculator.calculateRecordWeight(kboTeam.getAvgRank());
            double legacy = calculator.calculateLegacyWeight(kboTeam.getFoundedYear(), currentYear, maxFoundedYear);
            double franchiseStar = calculator.calculateFranchiseStarWeight(kboTeam.getStarPlayerCount(), minStar, maxStar);
            double growth = calculator.calculateGrowthWeight(kboTeam.getAverageAge(), minAge, maxAge);
            double homeGround = 0.0;
            double fandom = calculator.calculateFandomWeight(kboTeam.getFanScale(), minFan, maxFan);

            // 이미 해당 팀코드의 KboWeight가 존재하는지 확인
            Optional<KboWeight> existingOpt = weightRepository.findByKboTeam_TeamCode(kboTeam.getTeamCode());

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
                newWeight.setKboTeam(kboTeam);
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
