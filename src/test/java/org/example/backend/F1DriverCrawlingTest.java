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

public class F1DriverCrawlingTest {

    private static class DriverBasicInfo {
        private final String name;
        private final String team;
        private final String url;

        public DriverBasicInfo(String name, String team, String url) {
            this.name = name;
            this.team = team;
            this.url = url;
        }
        public String getName() { return name; }
        public String getTeam() { return team; }
        public String getUrl() { return url; }
    }

    private static class DriverCrawlingDto {
        private final String name;
        private final String team;
        private final String dateOfBirth;
        private final String imageUrl;
        private final String seasonRank;
        private final String seasonPoints;
        private final String seasonWins;
        private final String seasonPodiums;
        private final String careerWins;
        private final String careerPodiums;
        private final String driverChampionship;

        public DriverCrawlingDto(String name, String team, String dateOfBirth, String imageUrl, String seasonRank, String seasonPoints, String seasonWins, String seasonPodiums, String careerWins, String careerPodiums, String driverChampionship) {
            this.name = name;
            this.team = team;
            this.dateOfBirth = dateOfBirth;
            this.imageUrl = imageUrl;
            this.seasonRank = seasonRank;
            this.seasonPoints = seasonPoints;
            this.seasonWins = seasonWins;
            this.seasonPodiums = seasonPodiums;
            this.careerWins = careerWins;
            this.careerPodiums = careerPodiums;
            this.driverChampionship = driverChampionship;
        }

        @Override
        public String toString() {
            return "\n==========================================" +
                    "\n이름: " + name +
                    "\n팀: " + team +
                    "\n나이(생년월일): " + dateOfBirth +
                    "\n이미지: " + imageUrl +
                    "\n시즌 순위: " + seasonRank +
                    "\n시즌 점수: " + seasonPoints +
                    "\n시즌 우승: " + seasonWins +
                    "\n시즌 포디움: " + seasonPodiums +
                    "\n역대 우승: " + careerWins +
                    "\n역대 포디움: " + careerPodiums +
                    "\n드라이버 챔피언십: " + driverChampionship;
        }

        public String getDriverChampionship() {
            return driverChampionship;
        }
    }

    @Test
    void DriverCrawlingTest() {
        WebDriver driver = null;
        try {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            driver.get("https://www.formula1.com/en/drivers.html");

            String driverCardSelector = "div[style*='--f1-team-colour'] > a";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(driverCardSelector)));
            List<WebElement> driverCards = driver.findElements(By.cssSelector(driverCardSelector));
            System.out.println("총 " + driverCards.size() + "명의 드라이버 카드를 찾았습니다.");

            List<DriverBasicInfo> driverBasics = new ArrayList<>();
            for (WebElement card : driverCards) {
                String firstName = card.findElement(By.cssSelector("p.typography-module_display-l-regular__MOZq8")).getText();
                String lastName = card.findElement(By.cssSelector("p.typography-module_display-l-bold__m1yaJ")).getText();
                String name = firstName + " " + lastName;
                String team = card.findElement(By.cssSelector("p.typography-module_body-xs-semibold__Fyfwn")).getText();
                String url = card.getAttribute("href");
                driverBasics.add(new DriverBasicInfo(name, team, url));
            }

            List<DriverCrawlingDto> allDriverDetails = new ArrayList<>();
            System.out.println("\n--- 상세 정보 수집 시작 ---");

            for (DriverBasicInfo basicInfo : driverBasics) {
                driver.get(basicInfo.getUrl());
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"statistics\"]/div")));

                String imagePath = "//*[@id=\"maincontent\"]/div/div/div[2]/div[1]/div/div/div[4]/img";
                WebElement imageElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(imagePath)));
                String imageUrl = imageElement.getAttribute("src");
                String dateOfBirth = driver.findElement(By.xpath("//*[@id=\"biography\"]/div/div/div/div[1]/div[2]/dl/div[1]/dd")).getText();
                String seasonRank = driver.findElement(By.xpath("//*[@id=\"statistics\"]/div/div/div/div/div[1]/div/div[1]/dl[1]/div[1]/dd")).getText();
                String seasonPoints = driver.findElement(By.xpath("//*[@id=\"statistics\"]/div/div/div/div/div[1]/div/div[1]/dl[1]/div[2]/dd")).getText();
                String seasonWins = driver.findElement(By.xpath("//*[@id=\"statistics\"]/div/div/div/div/div[1]/div/div[1]/dl[2]/div[3]/dd")).getText();
                String seasonPodiums = driver.findElement(By.xpath("//*[@id=\"statistics\"]/div/div/div/div/div[1]/div/div[1]/dl[2]/div[4]/dd")).getText();
                String careerPodiums = driver.findElement(By.xpath("//*[@id=\"statistics\"]/div/div/div/div/div[3]/div/dl/div[4]/dd")).getText();
                String highestFinish = driver.findElement(By.xpath("//*[@id=\"statistics\"]/div/div/div/div/div[3]/div/dl/div[5]/dd")).getText();
                String driverChampionship = driver.findElement(By.xpath("//*[@id=\"statistics\"]/div/div/div/div/div[3]/div/dl/div[7]/dd")).getText();
                String careerWins;

                if (highestFinish.startsWith("1 (x"))
                    careerWins = highestFinish.substring(highestFinish.indexOf("(x") + 2, highestFinish.indexOf(')'));
                else if (highestFinish.trim().equals("1")) {
                    careerWins = "1";
                }
                else careerWins = "0";

                allDriverDetails.add(new DriverCrawlingDto(
                        basicInfo.getName(),
                        basicInfo.getTeam(),
                        dateOfBirth,
                        imageUrl,
                        seasonRank,
                        seasonPoints,
                        seasonWins,
                        seasonPodiums,
                        careerWins,
                        careerPodiums,
                        driverChampionship
                ));
            }

            System.out.println("\n\n=== 최종 수집된 드라이버 DTO 목록 (" + allDriverDetails.size() + "명) ===");
            allDriverDetails.forEach(System.out::println);

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
