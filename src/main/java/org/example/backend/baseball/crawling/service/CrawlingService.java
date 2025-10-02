package org.example.backend.baseball.crawling.service;

import org.example.backend.baseball.crawling.crawler.AverageAgeCrawler;
import org.example.backend.baseball.crawling.crawler.TeamRankCrawler;
import org.example.backend.baseball.team.Team;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrawlingService {
    public List<Team> crawlTeams(String[] teamCodes) {
        CrawlingManager manager = new CrawlingManager();
        try {
            manager.addCrawler(new AverageAgeCrawler());
            manager.addCrawler(new TeamRankCrawler());
            return manager.crawlAllTeams(teamCodes);
        } finally {
            manager.close();
        }
    }

}
