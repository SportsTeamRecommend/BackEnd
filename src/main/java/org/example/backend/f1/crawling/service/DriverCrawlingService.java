package org.example.backend.f1.crawling.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.example.backend.f1.crawling.dto.DriverBasicInfo;
import org.example.backend.f1.driver.DriverRepository;
import org.example.backend.f1.crawling.dto.DriverCrawlingDto;
import org.example.backend.f1.driver.Driver;
import org.example.backend.f1.team.F1TeamRepository;
import org.example.backend.f1.team.F1Team;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DriverCrawlingService {

    DriverRepository driverRepository;
    F1TeamRepository teamRepository;

    @Autowired
    public DriverCrawlingService(DriverRepository driverRepository, F1TeamRepository teamRepository) {
        this.driverRepository = driverRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional
    public void crawlingDriverData() {
        WebDriver driver = null;
        try {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");

            driver = new ChromeDriver(options);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

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

            List<String> teamNames = driverBasics.stream()
                    .map(DriverBasicInfo::getTeam)
                    .distinct() // 중복된 팀 이름 제거
                    .toList();

            List<F1Team> f1Teams = teamRepository.findAllByNameIn(teamNames);
            Map<String, F1Team> teamMap = f1Teams.stream()
                    .collect(Collectors.toMap(F1Team::getName, team -> team));

            List<DriverCrawlingDto> allDriverDetails = new ArrayList<>();
            System.out.println("\n--- 상세 정보 수집 시작 ---");

            for (DriverBasicInfo basicInfo : driverBasics) {
                driver.get(basicInfo.getUrl());
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"statistics\"]/div")));

                String imagePath = "//*[@id=\"maincontent\"]/div/div/div[2]/div[1]/div/div/div[4]/img";
                WebElement imageElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(imagePath)));
                String imageUrl = imageElement.getAttribute("src");
                String nationality = driver.findElement(By.xpath("//*[@id=\"maincontent\"]/div/div/div[2]/div[1]/div/div/div[7]/div[2]/div[1]/div/div/p")).getText();
                String dateOfBirth = driver.findElement(By.xpath("//*[@id=\"biography\"]/div/div/div/div[1]/div[2]/dl/div[1]/dd")).getText();
                String seasonPolls = driver.findElement(By.xpath("//*[@id=\"statistics\"]/div/div/div/div/div[1]/div/div[1]/dl[2]/div[5]/dd")).getText();
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

                int sumPoints = Integer.parseInt(seasonPoints);
                double count = 1;

                String[] nameParts = basicInfo.getName().split(" ");
                String lastName = nameParts[nameParts.length - 1];

                driver.get("https://www.formula1.com/en/results/2024/drivers");

                String xpath2024 = "//div[@id='results-table']//table/tbody/tr[td[2]//span[text() = '" + lastName + "']]/td[5]";

                try {
                    WebElement pointsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath2024)));
                    sumPoints += Integer.parseInt(pointsElement.getText());
                    count += 1.0;

                } catch (TimeoutException e) {
                    System.out.println("2024년 기록에 " + lastName + " 선수가 없거나 로드에 실패했습니다.");
                } catch (NumberFormatException e) {
                    System.out.println("2024년 " + lastName + " 선수의 포인트를 숫자로 변환할 수 없습니다.");
                }

                driver.get("https://www.formula1.com/en/results/2023/drivers");

                String xpath2023 = "//div[@id='results-table']//table/tbody/tr[td[2]//span[text() = '" + lastName + "']]/td[5]";

                try {
                    WebElement pointsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath2023)));
                    sumPoints += Integer.parseInt(pointsElement.getText());
                    count += 1.0;

                } catch (TimeoutException e) {
                    System.out.println("2023년 기록에 " + lastName + " 선수가 없거나 로드에 실패했습니다.");
                } catch (NumberFormatException e) {
                    System.out.println("2023년 " + lastName + " 선수의 포인트를 숫자로 변환할 수 없습니다.");
                }

                double avgPoints = sumPoints / count;
                F1Team f1Team = teamMap.get(basicInfo.getTeam());
                if (f1Team == null) {
                    System.err.println("DB 에서 해당 선수의 팀을 찾을 수 없습니다.");
                    continue;
                }

                allDriverDetails.add(new DriverCrawlingDto(
                        basicInfo.getName(),
                        f1Team,
                        dateOfBirth,
                        imageUrl,
                        nationality,
                        seasonPolls,
                        seasonRank,
                        seasonPoints,
                        seasonWins,
                        seasonPodiums,
                        careerWins,
                        careerPodiums,
                        driverChampionship,
                        avgPoints
                ));
            }

            System.out.println("\n\n=== 최종 수집된 드라이버 목록 (" + allDriverDetails.size() + "명) ===");
            allDriverDetails.forEach(System.out::println);

            updateDriverData(allDriverDetails);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private void updateDriverData(List<DriverCrawlingDto> allDriverDetails) {
        List<String> driverNames = allDriverDetails.stream()
                .map((detail) -> detail.name())
                .toList();

        List<Driver> drivers = driverRepository.findAllByNameIn(driverNames);

        Map<String, Driver> existingDriverMap = drivers.stream()
                .collect(Collectors.toMap(Driver::getName, existDriver -> existDriver));

        List<Driver> driversToSave = new ArrayList<>();

        for (DriverCrawlingDto dto : allDriverDetails) {
            String driverName = dto.name();

            Driver existingDriver = existingDriverMap.get(driverName);

            if (existingDriver != null) {
                existingDriver.updateInfo(dto);
                driversToSave.add(existingDriver);
            } else {
                Driver newDriver = new Driver(dto);
                driversToSave.add(newDriver);
            }
        }
        driverRepository.saveAll(driversToSave);
    }
}
