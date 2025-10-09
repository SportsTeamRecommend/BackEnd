package org.example.backend.f1.crawling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CrawlingRunner implements ApplicationRunner {

    private final F1TeamCrawlingService f1TeamCrawlingService;
    private final DriverCrawlingService driverCrawlingService;

    @Autowired
    CrawlingRunner(DriverCrawlingService driverCrawlingService, F1TeamCrawlingService f1TeamCrawlingService) {
        this.driverCrawlingService = driverCrawlingService;
        this.f1TeamCrawlingService = f1TeamCrawlingService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        f1TeamCrawlingService.crawlingTeamData();
        driverCrawlingService.crawlingDriverData();
    }

}
