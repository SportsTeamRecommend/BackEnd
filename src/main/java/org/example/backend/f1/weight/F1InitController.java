package org.example.backend.f1.weight;

import lombok.RequiredArgsConstructor;
import org.example.backend.f1.crawling.service.DriverCrawlingService;
import org.example.backend.f1.crawling.service.F1TeamCrawlingService;
import org.example.backend.f1.statistics.F1StatisticsService;
import org.example.backend.f1.team.F1InitService;
import org.example.backend.f1.team.F1TeamService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/f1")
@RequiredArgsConstructor
public class F1InitController {

    private final F1TeamCrawlingService f1TeamCrawlingService;
    private final DriverCrawlingService driverCrawlingService;
    private final F1TeamService f1TeamService;
    private final F1StatisticsService f1StatisticsService;
    private final F1InitService f1InitService;

    @PostMapping("/crawling")
    public void crawling() throws Exception {
        f1TeamCrawlingService.crawlingTeamData();
        driverCrawlingService.crawlingDriverData();
        f1StatisticsService.InitStatistics();
    }

    @PostMapping("/calculate/entity-weight")
    public void calculateEntityWeight() {
        f1TeamService.calculateTeamWeights();
    }

    @PostMapping("/init/data")
    public void init() {
        f1InitService.buildMetrics();
    }

}
