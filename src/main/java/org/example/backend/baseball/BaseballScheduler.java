package org.example.backend.baseball;

import lombok.RequiredArgsConstructor;

import org.example.backend.baseball.crawling.service.CrawlingService;
import org.example.backend.baseball.table.Team;
import org.example.backend.baseball.team.TeamRepository;
import org.example.backend.baseball.team.TeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO : 스케쥴러 대체용, 배포시 삭제할 것

@RestController
@RequestMapping("/api/kbo")
@RequiredArgsConstructor
public class BaseballScheduler {

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
        List<Team> teams = crawlingService.crawlTeams(teamCodes);

        teamRepository.saveAll(teams);
    }

    @PostMapping("calculate/entity-weight")
    public void calculateEntityWeight() {
        teamService.calculateEntityWeight();
    }


}
