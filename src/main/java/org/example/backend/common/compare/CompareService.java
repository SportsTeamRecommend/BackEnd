package org.example.backend.common.compare;

import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.table.KboWeight;
import org.example.backend.baseball.weight.KboWeightRepository;
import org.example.backend.f1.table.F1Weight;
import org.example.backend.f1.weight.F1WeightRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompareService {

    private final KboWeightRepository kboWeightRepository;
    private final F1WeightRepository f1WeightRepository;

    public KboWeightResponse getKboWeightByTeamName(String teamName) {

        KboWeight weight = kboWeightRepository.findByKboTeam_TeamName(teamName)
                .orElseThrow(() -> new RuntimeException("해당 팀 KBO 가중치가 없습니다."));

        return new KboWeightResponse(
                weight.getKboTeam().getTeamName(),
                weight.getRecord(),
                weight.getLegacy(),
                weight.getFranchiseStar(),
                weight.getGrowth(),
                weight.getFandom()
        );
    }

    public F1WeightResponse getF1WeightByTeamName(String teamName) {
        F1Weight weight = f1WeightRepository.findByMetrics_Name(teamName)
                .orElseThrow(() -> new RuntimeException("해당 팀 F1 가중치가 없습니다."));

        return new F1WeightResponse(
                weight.getMetrics().getName(),
                weight.getTeamRecord(),
                weight.getDriverRecord(),
                weight.getLegacy(),
                weight.getFranchiseStar(),
                weight.getUnderdog(),
                weight.getFandom()
        );
    }
}
