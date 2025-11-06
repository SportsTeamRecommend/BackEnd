package org.example.backend.baseball.crawling.crawler;

import org.example.backend.baseball.table.Team;
import org.openqa.selenium.WebDriver;

public interface Crawler {
    Team crawlTeamData(WebDriver driver, String teamCode) throws Exception;
}
