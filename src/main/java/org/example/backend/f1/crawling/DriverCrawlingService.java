package org.example.backend.f1.crawling;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.example.backend.f1.driver.DriverRepository;
import org.example.backend.f1.driver.dto.DriverCrawlingDto;
import org.example.backend.f1.driver.entity.Driver;
import org.example.backend.f1.team.F1TeamRepository;
import org.example.backend.f1.team.entity.F1Team;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverCrawlingService {

    DriverRepository driverRepository;
    F1TeamRepository teamRepository;

    @Autowired
    public DriverCrawlingService(DriverRepository driverRepository, F1TeamRepository teamRepository) {
        this.driverRepository = driverRepository;
        this.teamRepository = teamRepository;
    }

    void crawlingDriverData() {
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

                F1Team f1Team = teamRepository.findByName(basicInfo.getTeam());
                allDriverDetails.add(new DriverCrawlingDto(
                        basicInfo.getName(),
                        f1Team,
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

            for(DriverCrawlingDto driverCrawlingDto : allDriverDetails) {
                driverRepository.save(new Driver(driverCrawlingDto));
            }

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
