package org.example.backend.f1.crawling;

import org.example.backend.baseball.crawling.service.CrawlingScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class CrawlingRunner implements CommandLineRunner {

    private final F1TeamCrawlingService f1TeamCrawlingService;
    private final DriverCrawlingService driverCrawlingService;
    private final CrawlingScheduler crawlingScheduler;

    @Autowired
    CrawlingRunner(DriverCrawlingService driverCrawlingService, F1TeamCrawlingService f1TeamCrawlingService,
                   CrawlingScheduler crawlingScheduler) {
        this.driverCrawlingService = driverCrawlingService;
        this.f1TeamCrawlingService = f1TeamCrawlingService;
        this.crawlingScheduler = crawlingScheduler;
    }

    @Override
    public void run(String... args) throws Exception {
        f1TeamCrawlingService.crawlingTeamData();
        driverCrawlingService.crawlingDriverData();
        /*
        crawlingScheduler.runCrawling();
        crawlingScheduler.reCalculate();
        crawlingScheduler.runCrawling();
        crawlingScheduler.reCalculate();
        */
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
