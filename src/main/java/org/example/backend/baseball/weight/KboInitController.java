package org.example.backend.baseball.weight;

import lombok.RequiredArgsConstructor;


import org.example.backend.baseball.crawling.service.CrawlingService;
import org.example.backend.baseball.table.KboTeam;
import org.example.backend.baseball.team.KboTeamRepository;
import org.example.backend.baseball.team.KboTeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kbo")
@RequiredArgsConstructor
public class KboInitController {

    private final KboTeamService kboTeamService;
    private final CrawlingService crawlingService;
    private final KboTeamRepository kboTeamRepository;

    @PostMapping("/crawling")
    public void runCrawling() {
        String[] teamCodes = {"LG", "HH", "SK", "SS", "KT", "LT", "NC", "HT", "OB", "WO"};
        List<KboTeam> kboTeams = crawlingService.crawlTeams(teamCodes);

        kboTeamRepository.saveAll(kboTeams);
    }

    @PostMapping("/calculate/entity-weight")
    public void calculateEntityWeight() {
        kboTeamService.updateTeamRank();
        kboTeamService.calculateTeamWeight();
    }
}
