package org.example.backend.util;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.crawling.service.CrawlingService;
import org.example.backend.baseball.table.KboTeam;
import org.example.backend.baseball.team.KboTeamRepository;
import org.example.backend.baseball.team.KboTeamService;
import org.example.backend.f1.crawling.service.DriverCrawlingService;
import org.example.backend.f1.crawling.service.F1TeamCrawlingService;
import org.example.backend.f1.statistics.F1StatisticsService;
import org.example.backend.f1.team.F1TeamService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataUpdateController {
    private final F1TeamCrawlingService f1TeamCrawlingService;
    private final DriverCrawlingService driverCrawlingService;
    private final CrawlingService crawlingService;
    private final KboTeamService kboTeamService;
    private final KboTeamRepository kboTeamRepository;
    private final F1StatisticsService f1StatisticsService;
    private final F1TeamService f1TeamService;

    @PostMapping("/f1/update")
    public void updateF1Data() throws Exception {
        f1TeamCrawlingService.crawlingTeamData();
        driverCrawlingService.crawlingDriverData();
        f1StatisticsService.InitStatistics();
        //f1TeamService.calculateTeamWeights();
    }

    @PostMapping("/baseball/update")
    public void updateBaseballData() {
        String[] teamCodes = {"LG", "HH", "SK", "SS", "KT", "LT", "NC", "HT", "OB", "WO"};
        List<KboTeam> kboTeams = crawlingService.crawlTeams(teamCodes);
        kboTeamRepository.saveAll(kboTeams);
        kboTeamService.updateTeamRank();
        kboTeamService.calculateTeamWeight();
    }
}
