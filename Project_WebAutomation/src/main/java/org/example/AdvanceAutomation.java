package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class AdvanceAutomation {

    @Test
    public static void ValidateCheckBox() {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://demoqa.com/checkbox");
        driver.manage().window().maximize();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 300);");
        driver.findElement(By.xpath("//button[@class='rct-option rct-option-expand-all']")).click();

        WebElement homeCheckbox = driver.findElement(By.xpath("//span[text()='Home']/preceding-sibling::span"));
        js.executeScript("arguments[0].scrollIntoView()", homeCheckbox);
        homeCheckbox.click();

        List<WebElement> checkBoxCount = driver.findElements(By.xpath("//span[@class='rct-checkbox']"));
        Assert.assertEquals(checkBoxCount.size(), 17, "it is not expanded");
        WebElement notesCheckbox = driver.findElement(By.xpath(
                "//span[text()='Notes']/preceding-sibling::span"));
        notesCheckbox.click();
        Assert.assertFalse(notesCheckbox.isSelected(), "Checkbox is not selected");
        WebElement publicCheckbox = driver.findElement(By.xpath(
                "//span[text()='Public']/preceding-sibling::span"));
        publicCheckbox.click();
        Assert.assertFalse(notesCheckbox.isSelected(), "Checkbox is not selected");

        driver.close();
        driver.quit();
    }

    @Test
    public static void ValidateRadioButton() {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://demoqa.com/radio-button");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 300);");

        driver.findElement(By.xpath("//label[@for='yesRadio']")).click();
        Assert.assertEquals(driver.findElement(By.xpath("//p[@class='mt-3']")).getText(),
                "You have selected Yes");
        driver.findElement(By.xpath("//label[@for='impressiveRadio']")).click();
        Assert.assertEquals(driver.findElement(By.xpath("//p[@class='mt-3']")).getText(),
                "You have selected Impressive");

        driver.close();
        driver.quit();
    }

    @Test
    public void ValidateClickMeAlerts () {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://demoqa.com/alerts");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 300);");

        //Alert Button
        WebElement alertButton = driver.findElement(By.id("alertButton"));
        alertButton.click();
        Alert alert1 = driver.switchTo().alert();
        System.out.println("Alert1 : " + alert1.getText());
        Assert.assertEquals(alert1.getText(), "You clicked a button",
                "Alert1 text did not match");
        alert1.accept();

        //Timer Alert Button
        WebElement timerAlertButton = driver.findElement(By.id("timerAlertButton"));
        timerAlertButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.alertIsPresent());

        Alert alert2 = driver.switchTo().alert();
        System.out.println("Alert2 : " + alert2.getText());
        Assert.assertEquals(alert2.getText(), "This alert appeared after 5 seconds",
                "Alert2 text did not match");
        alert2.accept();

        driver.close();
        driver.quit();
    }

    @Test
    public static void windowBrowsers() throws InterruptedException {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://demoqa.com/browser-windows");
        driver.manage().window().maximize();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 300);");

        String originalWindowHandle = driver.getWindowHandle();                 // Saving original window
        //New Tab
        driver.findElement(By.id("tabButton")).click();
        Set<String> tabHandles = driver.getWindowHandles();                  // Give list of all the open windows

        for (String windowHandle : tabHandles) {                         // Loops through each window handle in the set.
            if (!originalWindowHandle.equals(windowHandle)) {            // Checks if the current handle is not the original one
                driver.switchTo().window(windowHandle);                  // Switches Selenium's focus to the new window/tab.

                WebElement sampleHeading = driver.findElement(By.id("sampleHeading"));
                Assert.assertEquals(sampleHeading.getText(), "This is a sample page");
                driver.close();
                driver.switchTo().window(originalWindowHandle);
            }
        }
        Thread.sleep(5000);                          //Without this sleep unable to remove the ad-popup

        //New Window
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement windowButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("windowButton")));
        windowButton.click();

        Set<String> windowHandles = driver.getWindowHandles();

        for (String windowHandle : windowHandles) {
            if (!originalWindowHandle.equals(windowHandle)) {
                driver.switchTo().window(windowHandle);

                WebElement sampleHeading = driver.findElement(By.id("sampleHeading"));
                Assert.assertEquals(sampleHeading.getText(), "This is a sample page");
                driver.close();
                driver.switchTo().window(originalWindowHandle);
            }
        }
        driver.close();
        driver.quit();
    }
}
