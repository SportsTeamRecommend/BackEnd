package org.example.backend.baseball.weight;

import lombok.RequiredArgsConstructor;


import org.example.backend.baseball.crawling.service.CrawlingService;
import org.example.backend.baseball.table.KboTeam;
import org.example.backend.baseball.team.TeamRepository;
import org.example.backend.baseball.team.TeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kbo")
@RequiredArgsConstructor
public class KboScheduler {

    private final TeamService teamService;
    private final CrawlingService crawlingService;
    private final TeamRepository teamRepository;

    @PostMapping("/update/rank")
    public void updateTeamRank() {
        teamService.updateTeamRank();
    }

    @PostMapping("/crawling")
    public void runCrawling() {
        String[] teamCodes = {"LG", "HH", "SK", "SS", "KT", "LT", "NC", "HT", "OB", "WO"};
        List<KboTeam> kboTeams = crawlingService.crawlTeams(teamCodes);

        teamRepository.saveAll(kboTeams);
    }

    @PostMapping("/calculate/entity-weight")
    public void calculateEntityWeight() {
        teamService.calculateEntityWeight();
    }
}
