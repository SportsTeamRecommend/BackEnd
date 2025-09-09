package org.example.backend.baseball.team.crawling;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.backend.baseball.team.crawling.crawler.Crawler;
import org.example.backend.baseball.team.entity.Team;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
class CrawlingManager {
    private final WebDriver driver;
    private final List<Crawler> crawlers = new ArrayList<>();

    public CrawlingManager() {
        WebDriverManager.chromedriver().setup();
        this.driver = new ChromeDriver();
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
        if (source.getPlayers() != null && !source.getPlayers().isEmpty()) {
            target.getPlayers().addAll(source.getPlayers());
        }
    }

    public void close() { if (driver != null) driver.quit(); }
}
