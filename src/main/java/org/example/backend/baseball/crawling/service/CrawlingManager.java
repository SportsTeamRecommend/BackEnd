package org.example.backend.baseball.crawling.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.backend.baseball.crawling.crawler.Crawler;
import org.example.backend.baseball.table.Team;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.chrome.ChromeOptions;

class CrawlingManager {
    private final WebDriver driver;
    private final List<Crawler> crawlers = new ArrayList<>();

    public CrawlingManager() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");

        this.driver = new ChromeDriver(options);
    }

    public void addCrawler(Crawler crawler) {
        crawlers.add(crawler);
    }

    public List<Team> crawlAllTeams(String[] teamCodes) {
        List<Team> results = new ArrayList<>();
        for (String teamCode : teamCodes) {
            Team combined = new Team(teamCode, "");
            for (Crawler c : crawlers) {
                try {
                    Team part = c.crawlTeamData(driver, teamCode);
                    mergeTeamData(combined, part);
                } catch (Exception e) {
                    System.err.println("크롤링 실패: " + c.getClass().getSimpleName() + " - " + teamCode);
                }
            }
            results.add(combined);
        }
        return results;
    }

    private void mergeTeamData(Team target, Team source) {
        if (source == null) return;
        if (target.getTeamName() == null || target.getTeamName().isEmpty()) {
            target.setTeamName(source.getTeamName());
        }
        if (source.getAverageAge() != null && source.getAverageAge() > 0) {
            target.setAverageAge(source.getAverageAge());
        }
        if (source.getCurrentRank() != null) {
            target.setCurrentRank(source.getCurrentRank());
        }
        if (source.getCrawledPlayers() != null && !source.getCrawledPlayers().isEmpty()) {
            target.getCrawledPlayers().addAll(source.getCrawledPlayers());
        }
    }

    public void close() { if (driver != null) driver.quit(); }
}
