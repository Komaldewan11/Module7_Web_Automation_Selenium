package org.example;

import Utils.CommonMethods;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class TestAutomation {

    private final String FILEPATH =
            "/Users/komaldewan/IdeaProjects/Framework-PracticeForm/src/main/resources/images/sample-bumblebee-400x300.png";
    private final String EXCEL_DATA =
            "/Users/komaldewan/IdeaProjects/Demoqa_trial/src/main/resources/TestData/practiceFormData.xlsx";

    WebElement maleGender;
    WebElement femaleGender;
    WebElement otherGender;
    WebElement txtSubjects;
    WebElement hobbiesSports;
    WebElement hobbiesReading;
    WebElement hobbiesMusic;

    @DataProvider(name = "LinkText")
    public Object[][] getTestDataFromExcel() throws IOException {
        CommonMethods excel = new CommonMethods();
        List<Object[]> testData = excel.getTestData(EXCEL_DATA);
        Object[][] data = new Object[testData.size()][12];
        for (int i=0; i<testData.size(); i++) {
            data[i] = testData.get(i);
        }
        return data;
    }

    WebDriver driver;
    @BeforeMethod
    public void initializeDriver() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://demoqa.com/automation-practice-form");
        driver.manage().window().maximize();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 300);");
    }

    @Test(dataProvider = "LinkText")
    public void fillTheForm (String firstName, String lastName, String emailId, String gender, String mobileNumber,
                              String monthDOB, String yearDOB,String dayDOB, List<String> subjects,
                              List<String> hobbies, String address, String state, String city) {

        driver.findElement(By.id("firstName")).sendKeys(firstName);
        driver.findElement(By.id("lastName")).sendKeys(lastName);
        driver.findElement(By.id("userEmail")).sendKeys(emailId);
        maleGender = driver.findElement(By.xpath("//label[@for='gender-radio-1']"));
        femaleGender = driver.findElement(By.xpath("//label[@for='gender-radio-2']"));
        otherGender = driver.findElement(By.xpath("//label[@for='gender-radio-3']"));
        selectGender(gender);
        driver.findElement(By.id("userNumber")).sendKeys(mobileNumber);
        //Date Picker
        WebElement datePickerInput = driver.findElement(By.id("dateOfBirthInput"));
        datePickerInput.click();
        WebElement monthDropDown = driver.findElement(By.className("react-datepicker__month-select"));
        selectDropDownOptionByVisibleText(monthDropDown, monthDOB);
        WebElement yearDropDown = driver.findElement(By.className("react-datepicker__year-select"));
        selectDropDownOptionByVisibleText(yearDropDown, yearDOB);
        selectDateForDateOFBirth(dayDOB);

        txtSubjects = driver.findElement(By.id("subjectsInput"));
        enterSubjects(subjects);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 300);");

        hobbiesSports = driver.findElement(By.xpath("//label[@for='hobbies-checkbox-1']"));
        hobbiesReading = driver.findElement(By.xpath("//label[@for='hobbies-checkbox-2']"));
        hobbiesMusic = driver.findElement(By.xpath("//label[@for='hobbies-checkbox-3']"));
        selectHobbies(hobbies);

        WebElement uploadPicture = driver.findElement(By.id("uploadPicture"));
        uploadPicture.sendKeys(FILEPATH);

        driver.findElement(By.id("currentAddress")).sendKeys(address);
        driver.findElement(By.id("react-select-3-input")).sendKeys(state, Keys.ENTER);
        driver.findElement(By.id("react-select-4-input")).sendKeys(city, Keys.ENTER);

        js.executeScript("window.scrollBy(0, 300);");

        WebElement submit = driver.findElement(By.xpath("//button[@id='submit']"));
        submit.click();

        //Assertions
        Assert.assertEquals(driver.findElement(By.xpath(
                        "//td[text()='Student Name']/following-sibling::td")).getText(),
                firstName + " " + lastName, "The Student Name not displayed as expected");
        Assert.assertEquals(driver.findElement(By.xpath(
                        "//td[text()='Student Email']/following-sibling::td")).getText(),
                emailId, "EmailId returned is incorrect");
        Assert.assertEquals(driver.findElement(By.xpath(
                        "//td[text()='Gender']/following-sibling::td")).getText().toLowerCase(),
                gender.toLowerCase(), "Gender returned is incorrect");
        Assert.assertEquals(driver.findElement(By.xpath(
                        "//td[text()='Mobile']/following-sibling::td")).getText(),
                mobileNumber, "Mobile Number returned is incorrect");
        Assert.assertEquals(driver.findElement(By.xpath(
                        "//td[text()='Date of Birth']/following-sibling::td")).getText(),
                dayDOB + " " + monthDOB + "," + yearDOB, "Returned date is incorrect");
        Assert.assertEquals(driver.findElement(By.xpath(
                "//td[text()='Subjects']/following-sibling::td")).getText(), String.join(
                        ", ", subjects), "Entered subjects returned are incorrect");
        Assert.assertEquals(driver.findElement(By.xpath(
                "//td[text()='Hobbies']/following-sibling::td")).getText(),
                String.join(", ", hobbies), "Selected hobby returned is incorrect");
        Assert.assertEquals(driver.findElement(
                        By.xpath("//td[text()='Picture']/following-sibling::td")).getText(),
                "sample-bumblebee-400x300.png", "Uploaded picture is incorrect");
        Assert.assertEquals(driver.findElement(By.xpath(
                        "//td[text()='Address']/following-sibling::td")).getText(),
                address,"Address returned is incorrect");
        Assert.assertEquals(driver.findElement(By.xpath(
                        "//td[text()='State and City']/following-sibling::td")).getText(),
                state + " " + city, "Returned State and City are incorrect");
        System.out.println("All form fields validated successfully");
    }

    public void selectDropDownOptionByVisibleText(WebElement ele, String ddValue) {
        Select dd = new Select(ele);
        dd.selectByVisibleText(ddValue);
    }
    public void selectDateForDateOFBirth(String dayDOB) {

        int day = Integer.parseInt(dayDOB);                 //remove leading zeros ("07" becomes "7")
        // Create an XPath string to find the correct day on the calendar
        // Exclude days from previous/next months using 'not(contains(@class,'outside-month'))'
        // Find the date element on the calendar using the XPath
        WebElement dateElement = driver.findElement(By.xpath(
                "//div[contains(@class,'react-datepicker__day') " +
                "and not(contains(@class,'outside-month')) " +
                "and text()='" + day + "']"));
        dateElement.click();
    }
    public void selectGender(final String gender) {
        switch(gender.toLowerCase()){
            case "male" -> maleGender.click();
            case "female" -> femaleGender.click();
            default -> otherGender.click();
        }
    }
    public void enterSubjects(final List<String> multiSubjects) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.elementToBeClickable(txtSubjects));
        for (String subjects : multiSubjects) {
            txtSubjects.sendKeys(subjects);
            txtSubjects.sendKeys(Keys.TAB);
        }
    }
    public void selectHobbies(final List<String> hobbies) {
        for (String hobby : hobbies) {
            switch (hobby.trim().toLowerCase()) {
                case "sports" -> hobbiesSports.click();
                case "reading" -> hobbiesReading.click();
                case "music" -> hobbiesMusic.click();
                default-> System.out.println("Invalid Hobby");
            }
        }
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}

