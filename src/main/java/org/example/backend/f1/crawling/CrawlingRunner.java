package org.example.backend.f1.crawling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CrawlingRunner implements ApplicationRunner {

    private final TeamCrawlingService teamCrawlingService;
    private final DriverCrawlingService driverCrawlingService;

    @Autowired
    CrawlingRunner(DriverCrawlingService driverCrawlingService, TeamCrawlingService teamCrawlingService) {
        this.driverCrawlingService = driverCrawlingService;
        this.teamCrawlingService = teamCrawlingService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        teamCrawlingService.crawlingTeamData();
        driverCrawlingService.crawlingDriverData();
    }

}
