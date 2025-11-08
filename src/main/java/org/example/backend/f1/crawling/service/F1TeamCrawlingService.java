package org.example.backend.f1.crawling.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.example.backend.f1.crawling.dto.F1TeamBasicInfo;
import org.example.backend.f1.team.F1TeamRepository;
import org.example.backend.f1.crawling.dto.F1TeamCrawlingDto;
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
public class F1TeamCrawlingService {

    private final F1TeamRepository f1TeamRepository;

    @Autowired
    public F1TeamCrawlingService(F1TeamRepository f1TeamRepository) {
        this.f1TeamRepository = f1TeamRepository;
    }

    @Transactional
    public void crawlingTeamData() {
        WebDriver driver = null;
        try {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless"); // 1. Headless 모드 (필수)
            options.addArguments("--no-sandbox"); // 2. 샌드박스 비활성화 (서버 환경)
            options.addArguments("--disable-dev-shm-usage"); // 3. 공유 메모리 사용 비활성화 (서버 환경)
            options.addArguments("--disable-gpu"); // (선택 사항) GPU 비활성화
            // === [끝] 옵션 추가 ===

            driver = new ChromeDriver(options); // <--- 옵션을 적용하여 드라이버를 생성합니다.
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            driver.get("https://www.formula1.com/en/teams.html");

            String teamCardSelector = "a[class*='group/team-card relative z-0 rounded-m overflow-hidden']";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(teamCardSelector)));
            List<WebElement> teamCards = driver.findElements(By.cssSelector(teamCardSelector));
            System.out.println("총 " + teamCards.size() + "명의 팀 카드를 찾았습니다.");

            List<F1TeamBasicInfo> teamBasics = new ArrayList<>();
            for (WebElement card : teamCards) {
                String name = card.findElements(By.xpath(".//p")).get(0).getText();
                String url = card.getAttribute("href");
                teamBasics.add(new F1TeamBasicInfo(name, url));
            }

            List<F1TeamCrawlingDto> allTeamDetails = new ArrayList<>();
            System.out.println("\n--- 상세 정보 수집 시작 ---");

            for (F1TeamBasicInfo basicInfo : teamBasics) {
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
                String constructorChampionship = driver.findElement(By.xpath("//*[@id=\"statistics\"]/div/div/div/div[2]/div/div[3]/div/dl/div[7]/dd")).getText();
                String careerWins;
                if (highestFinish.startsWith("1 (x"))
                    careerWins = highestFinish.substring(highestFinish.indexOf("(x") + 2, highestFinish.indexOf(')'));
                else if (highestFinish.trim().equals("1")) {
                    careerWins = "1";
                }
                else careerWins = "0";

                int sumRank = Integer.parseInt(seasonRank.replaceAll("[^0-9]", ""));
                double count = 1.0;

                String fullName = basicInfo.getName();


                String defaultName = "";

                if (fullName.contains("Red Bull")) defaultName = "Red Bull";
                else if (fullName.contains("Mercedes")) defaultName = "Mercedes";
                else if (fullName.contains("Ferrari")) defaultName = "Ferrari";
                else if (fullName.contains("McLaren")) defaultName = "McLaren";
                else if (fullName.contains("Aston Martin")) defaultName = "Aston Martin";
                else if (fullName.contains("Alpine")) defaultName = "Alpine";
                else if (fullName.contains("Williams")) defaultName = "Williams";
                else if (fullName.contains("RB")) defaultName = "RB";
                else if (fullName.contains("Sauber")) defaultName = "Sauber";
                else if (fullName.contains("Haas")) defaultName = "Haas";
                else defaultName = fullName;

                driver.get("https://www.formula1.com/en/results/2024/team");

                String xpath2024 = "//div[@id='results-table']//table/tbody/tr[contains(normalize-space(td[2]), '" + defaultName
                        + "')]/td[1]";

                try {
                    WebElement rankElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath2024)));
                    sumRank += Integer.parseInt(rankElement.getText());
                    count += 1.0;

                } catch (TimeoutException e) {
                    System.out.println("2024년 기록에 " + defaultName + " (원본: "+ fullName +") 팀이 없거나 로드에 실패했습니다.");
                } catch (NumberFormatException e) {
                    System.out.println("2024년 " + defaultName + " 팀의 랭킹을 숫자로 변환할 수 없습니다.");
                }

                driver.get("https://www.formula1.com/en/results/2023/team");

                String xpath2023 = "//div[@id='results-table']//table/tbody/tr[contains(normalize-space(td[2]), '" + defaultName
                        + "')]/td[1]";

                try {
                    WebElement rankElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath2023)));
                    sumRank += Integer.parseInt(rankElement.getText());
                    count += 1.0;

                } catch (TimeoutException e) {
                    System.out.println("2023년 기록에 " + defaultName + " (원본: "+ fullName +") 팀이 없거나 로드에 실패했습니다.");
                } catch (NumberFormatException e) {
                    System.out.println("2023년 " + defaultName + " 팀의 랭킹을 숫자로 변환할 수 없습니다.");
                }
                double avgRank = sumRank / count;

                allTeamDetails.add(new F1TeamCrawlingDto(
                        basicInfo.getName(),
                        imageUrl,
                        seasonRank,
                        seasonPoints,
                        seasonWins,
                        seasonPodiums,
                        careerWins,
                        careerPodiums,
                        constructorChampionship,
                        avgRank
                ));
            }

            System.out.println("\n\n=== 최종 수집된 팀 목록 (" + allTeamDetails.size() + "팀) ===");
            allTeamDetails.forEach(System.out::println);

            updateF1TeamData(allTeamDetails);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private void updateF1TeamData(List<F1TeamCrawlingDto> allTeamDetails) {
        List<String> teamNames = allTeamDetails.stream()
                .map((detail) -> detail.name())
                .toList();

        List<F1Team> teams = f1TeamRepository.findAllByNameIn(teamNames);

        Map<String, F1Team> existingTeamMap = teams.stream()
                .collect(Collectors.toMap(F1Team::getName, existTeam -> existTeam));

        List<F1Team> teamToSave = new ArrayList<>();

        for (F1TeamCrawlingDto allTeamDetail : allTeamDetails) {
            String teamName = allTeamDetail.name();

            F1Team existTeam = existingTeamMap.get(teamName);

            if (existTeam != null) {
                existTeam.updateInfo(allTeamDetail);
                teamToSave.add(existTeam);
            } else {
                F1Team newDriver = new F1Team(allTeamDetail);
                teamToSave.add(newDriver);
            }
        }
        f1TeamRepository.saveAll(teamToSave);
    }

}
