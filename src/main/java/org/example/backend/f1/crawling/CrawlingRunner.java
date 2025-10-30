package org.example.backend.f1.crawling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class CrawlingRunner implements CommandLineRunner {

    private final F1TeamCrawlingService f1TeamCrawlingService;
    private final DriverCrawlingService driverCrawlingService;

    @Autowired
    CrawlingRunner(DriverCrawlingService driverCrawlingService, F1TeamCrawlingService f1TeamCrawlingService) {
        this.driverCrawlingService = driverCrawlingService;
        this.f1TeamCrawlingService = f1TeamCrawlingService;
    }

    @Override
    public void run(String... args) throws Exception {
        f1TeamCrawlingService.crawlingTeamData();
        driverCrawlingService.crawlingDriverData();
    }

    /*
    @Override
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void run(ApplicationArguments args) throws Exception {
        f1TeamCrawlingService.crawlingTeamData();
        driverCrawlingService.crawlingDriverData();
    }
    */

}
