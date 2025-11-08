package org.example.backend.baseball.crawling.crawler;

import org.example.backend.baseball.crawling.dto.CrawledPlayer;
import org.example.backend.baseball.table.Team;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AverageAgeCrawler implements Crawler {
    @Override
    public Team crawlTeamData(WebDriver driver, String teamCode) {
        try {
            driver.get("https://www.koreabaseball.com/Player/Register.aspx");


            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            By headerSel = By.cssSelector("h4.bul_history");
            By tabSel = By.cssSelector("li[data-id='" + teamCode + "'] a");

            // 클릭 전 헤더 텍스트
            String before = wait.until(ExpectedConditions.visibilityOfElementLocated(headerSel)).getText();

            // 탭 요소
            WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(tabSel));

            // JS 클릭 + 중앙 스크롤 (오버레이/격자 스크롤 이슈 회피)
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", tab);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tab);

            Thread.sleep(500);

            // 파싱
            Document doc = Jsoup.parse(driver.getPageSource());
            Element header = doc.selectFirst("h4.bul_history");
            String teamName = (header != null) ? header.text().replace("선수등록명단", "").trim() : teamCode;

            Team team = new Team(teamCode, teamName);

            List<Integer> ages = new ArrayList<>();
            int tableIndex = 0;
            for (Element table : doc.select("table.tNData")) {
                if (tableIndex < 2) { tableIndex++; continue; } // 감독/코치 스킵
                if (isInsideHistorySection(table)) { tableIndex++; continue; } // 등/말소 스킵

                for (Element row : table.select("tbody > tr")) {
                    Elements td = row.select("td");
                    if (td.size() < 4) continue;
                    String name = td.get(1).text();
                    String birth = td.get(3).text();

                    Integer age = tryParseKoreanAge(birth);
                    if (age != null) {
                        team.getCrawledPlayers().add(new CrawledPlayer(name, age));
                        ages.add(age);
                    }
                }
                tableIndex++;
            }

            if (!ages.isEmpty()) {
                double avg = ages.stream().mapToInt(Integer::intValue).average().orElse(0);
                team.setAverageAge(avg);
            }
            return team;

        } catch (Exception e) {
            throw new RuntimeException("평균 나이 크롤링 실패: " + teamCode, e);
        }
    }

    private boolean isInsideHistorySection(Element table) {
        for (Element p : table.parents()) {
            var cls = p.classNames();
            if (cls.contains("row") && cls.contains("mt35")) return true;
        }
        return false;
    }

    private Integer tryParseKoreanAge(String birthText) {
        try {
            String yyyy = birthText.replaceAll("[^0-9]", "").substring(0, 4);
            int year = Integer.parseInt(yyyy);
            return LocalDate.now().getYear() - year + 1; // 필요시 만나이로 변경
        } catch (Exception e) {
            return null;
        }
    }
}
