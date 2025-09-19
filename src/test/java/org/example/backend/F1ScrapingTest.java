package org.example.backend;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class F1ScrapingTest {

    @Test
    void F1_팀순위_크롤링_테스트() {
        WebDriver driver = null;

        try {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();

            driver.get("https://www.formula1.com/en/results.html/2024/team.html");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            try {
                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                String cookieAcceptButtonId = "#onetrust-accept-btn-handler";
                WebElement acceptButton = shortWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cookieAcceptButtonId)));

                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("arguments[0].click();", acceptButton);
                System.out.println("성공: JavaScript를 사용하여 쿠키 동의 팝업을 닫았습니다.");
            } catch (Exception e) {
                System.out.println("정보: 쿠키 동의 팝업이 없거나 이미 처리되어 다음 단계로 진행합니다.");
            }


            String tableRowsSelector = "table.f1-table tbody tr";
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(tableRowsSelector)));
            System.out.println("성공: 순위표 데이터 행들의 로딩을 확인했습니다.");

            List<WebElement> teamRows = driver.findElements(By.cssSelector(tableRowsSelector));
            System.out.println("총 " + teamRows.size() + "개의 팀 정보를 성공적으로 가져왔습니다.");

            for (WebElement row : teamRows) {
                String teamName = row.findElement(By.cssSelector("td:nth-child(2) a")).getText();
                String points = row.findElement(By.cssSelector("td:nth-child(3)")).getText();

                System.out.printf("팀: %-20s | 점수: %s\n", teamName, points);
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

}
