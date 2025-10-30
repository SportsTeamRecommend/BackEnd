package org.example.backend.f1.crawling;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.example.backend.f1.team.F1TeamRepository;
import org.example.backend.f1.team.dto.F1TeamCrawlingDto;
import org.example.backend.f1.team.entity.F1Team;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class F1TeamCrawlingService {

    F1TeamRepository teamRepository;

    @Autowired
    public F1TeamCrawlingService(F1TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    void crawlingTeamData() {
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
                double count = 1;

                driver.get("https://www.formula1.com/en/results/2024/team");
                try {
                    sumRank += Integer.parseInt(driver.findElement(By.xpath("//table[contains(@class, 'f1-table')]/tbody/tr[contains(td[2], '" + basicInfo.getName() +"')]/td[1]")).getText());
                    count += 1.0;

                } catch (NoSuchElementException e) {
                    System.out.println("목록에 해당 선수가 없어 건너뜁니다.");
                }

                driver.get("https://www.formula1.com/en/results/2023/team");
                try {
                    sumRank += Integer.parseInt(driver.findElement(By.xpath("//table[contains(@class, 'f1-table')]/tbody/tr[contains(td[2], '" + basicInfo.getName() +"')]/td[1]")).getText());
                    count += 1.0;

                } catch (NoSuchElementException e) {
                    System.out.println("목록에 해당 팀이 없어 건너뜁니다.");
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

            System.out.println("\n\n=== 최종 수집된 팀 DTO 목록 (" + allTeamDetails.size() + "팀) ===");
            allTeamDetails.forEach(System.out::println);

            for(F1TeamCrawlingDto teamCrawlingDto : allTeamDetails) {
                teamRepository.save(new F1Team(teamCrawlingDto));
            }

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

}
