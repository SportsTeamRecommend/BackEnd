package org.example.backend;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class F1TeamCrawlingTest {

    private static class TeamBasicInfo {
        private final String name;
        private final String url;

        public TeamBasicInfo(String name, String url) {
            this.name = name;
            this.url = url;
        }
        public String getName() { return name; }
        public String getUrl() { return url; }
    }

    private static class TeamCrawlingDto {
        private final String name;
        private final String imageUrl;
        private final String seasonRank;
        private final String seasonPoints;
        private final String seasonWins;
        private final String seasonPodiums;
        private final String careerWins;
        private final String careerPodiums;
        private final String url;

        public TeamCrawlingDto(String name, String imageUrl, String seasonRank, String seasonPoints, String seasonWins, String seasonPodiums, String careerWins, String careerPodiums, String url) {
            this.name = name;
            this.imageUrl = imageUrl;
            this.seasonRank = seasonRank;
            this.seasonPoints = seasonPoints;
            this.seasonWins = seasonWins;
            this.seasonPodiums = seasonPodiums;
            this.careerWins = careerWins;
            this.careerPodiums = careerPodiums;
            this.url = url;
        }

        @Override
        public String toString() {
            return "\n==========================================" +
                    "\n이름: " + name +
                    "\n이미지: " + imageUrl +
                    "\n시즌 순위: " + seasonRank +
                    "\n시즌 점수: " + seasonPoints +
                    "\n시즌 우승: " + seasonWins +
                    "\n시즌 포디움: " + seasonPodiums +
                    "\n역대 우승: " + careerWins +
                    "\n역대 포디움: " + careerPodiums;
        }
    }

    @Test
    void DriverCrawlingTest() {
        WebDriver driver = null;
        try {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            driver.get("https://www.formula1.com/en/teams.html");

            String teamCardSelector = "a[class*='group/team-card relative z-0 rounded-m overflow-hidden']";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(teamCardSelector)));
            List<WebElement> teamCards = driver.findElements(By.cssSelector(teamCardSelector));
            System.out.println("총 " + teamCards.size() + "명의 팀 카드를 찾았습니다.");

            List<TeamBasicInfo> teamBasics = new ArrayList<>();
            for (WebElement card : teamCards) {
                String name = card.findElements(By.xpath(".//p")).get(0).getText();
                String url = card.getAttribute("href");
                teamBasics.add(new TeamBasicInfo(name, url));
            }

            List<TeamCrawlingDto> allTeamDetails = new ArrayList<>();
            System.out.println("\n--- 상세 정보 수집 시작 ---");

            for (TeamBasicInfo basicInfo : teamBasics) {
                driver.get(basicInfo.getUrl());
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"statistics\"]/div")));

                String imagePath = "//*[@id=\"maincontent\"]/div/div/div[2]/div[1]/div/div/div/div[3]/div[1]/img";
                WebElement imageElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(imagePath)));
                String imageUrl = imageElement.getAttribute("src");
                String seasonRank = driver.findElement(By.xpath("//*[@id=\"statistics\"]/div/div/div/div[2]/div/div[1]/div/div[1]/dl[1]/div[1]/dd")).getText();
                String seasonPoints = driver.findElement(By.xpath("//*[@id=\"statistics\"]/div/div/div/div[2]/div/div[1]/div/div[1]/dl[1]/div[2]/dd")).getText();
                String seasonWins = driver.findElement(By.xpath("//*[@id=\"statistics\"]/div/div/div/div[2]/div/div[1]/div/div[1]/dl[2]/div[3]/dd")).getText();
                String seasonPodiums = driver.findElement(By.xpath("//*[@id=\"statistics\"]/div/div/div/div[2]/div/div[1]/div/div[1]/dl[2]/div[4]/dd")).getText();
                String careerPodiums = driver.findElement(By.xpath("//*[@id=\"statistics\"]/div/div/div/div[2]/div/div[3]/div/dl/div[4]/dd")).getText();
                String highestFinish = driver.findElement(By.xpath("//*[@id=\"statistics\"]/div/div/div/div[2]/div/div[3]/div/dl/div[5]/dd")).getText();
                String careerWins;
                if (highestFinish.startsWith("1 (x"))
                    careerWins = highestFinish.substring(highestFinish.indexOf("(x") + 2, highestFinish.indexOf(')'));
                else if (highestFinish.trim().equals("1")) {
                    careerWins = "1";
                }
                else careerWins = "0";

                allTeamDetails.add(new TeamCrawlingDto(
                        basicInfo.getName(),
                        imageUrl,
                        seasonRank,
                        seasonPoints,
                        seasonWins,
                        seasonPodiums,
                        careerWins,
                        careerPodiums,
                        basicInfo.getUrl()
                ));
            }

            System.out.println("\n\n=== 최종 수집된 팀 DTO 목록 (" + allTeamDetails.size() + "팀) ===");
            allTeamDetails.forEach(System.out::println);

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
