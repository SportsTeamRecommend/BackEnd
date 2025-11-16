package org.example.backend.baseball.crawling.service;

import org.example.backend.baseball.crawling.crawler.AverageAgeCrawler;
import org.example.backend.baseball.crawling.crawler.TeamRankCrawler;
import org.example.backend.baseball.table.KboTeam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrawlingService {
    public List<KboTeam> crawlTeams(String[] teamCodes) {
        CrawlingManager manager = new CrawlingManager();
        try {
            manager.addCrawler(new TeamRankCrawler());
            manager.addCrawler(new AverageAgeCrawler());
            return manager.crawlAllTeams(teamCodes);
        } finally {
            manager.close();
        }
    }

}
