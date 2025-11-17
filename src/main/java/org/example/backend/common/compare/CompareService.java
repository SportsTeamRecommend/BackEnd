package org.example.backend.common.compare;

import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.table.KboWeight;
import org.example.backend.baseball.weight.KboWeightRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompareService {

    private final KboWeightRepository kboWeightRepository;

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
}
