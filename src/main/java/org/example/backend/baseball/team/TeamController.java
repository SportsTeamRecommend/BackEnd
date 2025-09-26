package org.example.backend.baseball.team;

import lombok.RequiredArgsConstructor;

import org.example.backend.baseball.crawling.service.CrawlingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO : 스케쥴러 대체용, 배포시 삭제할 것

@RestController
@RequestMapping("/KBO/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final CrawlingService crawlingService;
    private final TeamRepository teamRepository;

    @GetMapping("/calculate")
    public void updateTeamRank(){
        teamService.updateTeamRank();
    }

    @GetMapping("/crawling")
    public void runCrawling() {
        String[] teamCodes = {"LG", "HH", "SK", "SS", "KT", "LT", "NC", "HT", "OB", "WO"};
        List<Team> teams = crawlingService.crawlTeams(teamCodes);

        teamRepository.saveAll(teams);
    }

}
