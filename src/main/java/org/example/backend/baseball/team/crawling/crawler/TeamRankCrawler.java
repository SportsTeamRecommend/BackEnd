package org.example.backend.baseball.team.crawling.crawler;

import org.example.backend.baseball.team.entity.Team;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

public class TeamRankCrawler implements Crawler {

    // 팀코드 -> 페이지 표시명 매핑 (KBO 표기 기준)
    private static final Map<String, String> CODE_TO_NAME = new HashMap<>();
    static {
        CODE_TO_NAME.put("LG", "LG");
        CODE_TO_NAME.put("HH", "한화");
        CODE_TO_NAME.put("SK", "SSG");
        CODE_TO_NAME.put("SS", "삼성");
        CODE_TO_NAME.put("KT", "KT");
        CODE_TO_NAME.put("LT", "롯데");
        CODE_TO_NAME.put("NC", "NC");
        CODE_TO_NAME.put("HT", "KIA");
        CODE_TO_NAME.put("OB", "두산");
        CODE_TO_NAME.put("WO", "키움");
    }

    /**
     * 팀 순위 페이지에서 teamCode에 해당하는 팀의 순위를 찾아 Team으로 리턴
     * @param driver  Selenium WebDriver (밖에서 생성/관리)
     * @param teamCode "LG","HH","OB" 등
     */
    @Override
    public Team crawlTeamData(WebDriver driver, String teamCode) {
        try {
            // 1) 페이지 이동
            driver.get("https://www.koreabaseball.com/Record/TeamRank/TeamRankDaily.aspx");

            // 2) 테이블이 렌더된 뒤의 HTML을 Jsoup으로 파싱
            Document doc = Jsoup.parse(driver.getPageSource());

            // 3) 순위 테이블의 모든 행
            Elements rows = doc.select("#cphContents_cphContents_cphContents_udpRecord table.tData tbody tr");

            // 4) 코드 -> 팀명 변환
            String wanted = CODE_TO_NAME.getOrDefault(teamCode, teamCode).trim();

            // 5) 각 행에서 순위/팀명 추출 후 매칭
            for (Element tr : rows) {
                Elements td = tr.select("td");
                if (td.size() < 2) continue;

                String rankTxt = td.get(0).text().trim();
                String teamName = td.get(1).text().trim();

                // 숫자행만 (합계/공백 방지)
                if (!rankTxt.matches("\\d+")) continue;

                if (teamName.equals(wanted)) {
                    int rank = Integer.parseInt(rankTxt);

                    // ---- Team 객체 생성 & 반환 (필드명에 맞게 수정) ----
                    Team team = new Team();
                    team.setTeamName(teamName);
                    team.setCurrentRank(rank);
                    return team;
                }
            }

            // 못 찾으면 기본값으로 반환 (원하면 예외로 바꿔도 됨)
            Team fallback = new Team();
            fallback.setTeamName(wanted);
            fallback.setCurrentRank(-1);
            return fallback;

        } catch (Exception e) {
            throw new RuntimeException("팀 순위 크롤링 실패: " + teamCode, e);
        }
    }
}
