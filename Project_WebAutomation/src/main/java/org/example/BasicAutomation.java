package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class BasicAutomation {

    WebDriver driver;
    @BeforeMethod
    public void initializeDriver() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.wikipedia.org");
        driver.manage().window().maximize();
    }
    @Test
    public void wikipediaTest() {
        String title = driver.getTitle();
        Assert.assertEquals(title,"Wikipedia", "Title does not matched");
        System.out.println("Title validation: " + title);

        driver.findElement(By.className("frb-header-minimize-icon")).click();
        driver.findElement(By.className("frb-conversation-close")).click();

        WebElement searchInput = driver.findElement(By.xpath("//input[@id='searchInput']"));
        searchInput.click();
        searchInput.sendKeys("Historical Books");

        //DropDown Selection and Explicit Wait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement selectSuggestion = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                "//div[contains(@class,'suggestion-text')]/h3[contains(@class,'suggestion-title')]")));
        selectSuggestion.click();
        WebElement firstHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading")));
        Assert.assertEquals(firstHeading.getText(), "Historical books",
                "First Heading does not matched");
    }
    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
