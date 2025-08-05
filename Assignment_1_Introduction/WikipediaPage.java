package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class WikipediaPage {

    public static void main (String[] args) throws IOException {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.get("https://www.wikipedia.org");
        driver.manage().window().maximize();

        String actualTitle = driver.getTitle();

        // Typecast the driver to TakesScreenshot
        TakesScreenshot ts = (TakesScreenshot) driver;

        // Capture the screenshot as a file
        File srcFile = ts.getScreenshotAs(OutputType.FILE);

        // Define the destination file path
        File destFile = new File("./src/main/java/screenshots/wikipedia_screenshot.png");

        // Copy the screenshot to the destination
        FileUtils.copyFile(srcFile, destFile);
        System.out.println("Screenshot saved successfully at: " + destFile.getAbsolutePath());
        Assert.assertEquals(actualTitle,"Wikipedia", "Title does not matched");
        System.out.println("Title validation: " + actualTitle);

        driver.quit();
    }
}
