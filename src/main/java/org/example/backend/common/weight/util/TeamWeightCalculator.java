package org.example.backend.common.weight.util;

import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.table.Team;
import org.example.backend.baseball.team.TeamRepository;
import org.example.backend.baseball.table.KboTeamWeight;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamWeightCalculator {

    private final TeamRepository teamRepository;

    public KboTeamWeight calculateKboTeamWeight(Team team) {
        KboTeamWeight weight = new KboTeamWeight();

        // 팀 정보 연결
        weight.setTeam(team);

        double record = calculateRank(team.getAvgRank());
        weight.setRecord(record);

        double legacy = calculateLegacy(team.getFoundedYear());
        weight.setLegacy(legacy);


        // TODO: 프랜차이즈 스타, 성장 가능성, 지역 점수 등 추가 계산
        return weight;
    }

    private double calculateRank(double rank) {
        return 11 - rank;
    }

    private double calculateLegacy(int foundingYear) {
        int oldestYear = teamRepository.findOldestFoundedYear();
        int currentYear = java.time.Year.now().getValue();
        return ((double) (currentYear - foundingYear) / (currentYear - oldestYear)) * 9 + 1;
    }

}
