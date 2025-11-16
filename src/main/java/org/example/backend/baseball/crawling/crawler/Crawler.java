package org.example.backend.baseball.crawling.crawler;

import org.example.backend.baseball.table.KboTeam;
import org.openqa.selenium.WebDriver;

public interface Crawler {
    KboTeam crawlTeamData(WebDriver driver, String teamCode) throws Exception;
}
