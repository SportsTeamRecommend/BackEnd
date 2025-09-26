package org.example.backend.baseball.crawling.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.team.TeamRepository;
import org.example.backend.baseball.team.TeamService;
import org.example.backend.baseball.team.Team;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CrawlingScheduler {

    private final CrawlingService crawlingService;
    private final TeamRepository teamRepository;
    private final TeamService teamService;

    // 매일 새벽 n시에 실행 (cron = 초 분 시 일 월 요일)
    @Scheduled(cron = "0 57 14 * * *")
    public void runCrawling() {
        String[] teamCodes = {"LG", "HH", "SK", "SS", "KT", "LT", "NC", "HT", "OB", "WO"};
        List<Team> teams = crawlingService.crawlTeams(teamCodes);

        teamRepository.saveAll(teams);
    }

    @Scheduled(cron = "0 58 14 * * *")
    public void reCalculate(){
        teamService.updateTeamRank();
    }
}
