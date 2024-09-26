package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.logging.Logger;

public class UserAuth {

    private WebDriver driver;
    private static final Logger logger = Logger.getLogger(UserAuth.class.getName());

    @BeforeClass
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            driver = new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            driver = new FirefoxDriver(options);
        }
        driver.get("http://localhost/kanboard");
    }

    @Test
    public void testIncorrectUsername() {
        Assert.assertFalse(performLogin("hacker", "232323"), "The authentication should fail with incorrect username.");
        logger.info("Completed test for incorrect username.");
    }

    @Test
    public void testIncorrectPassword() {
        Assert.assertFalse(performLogin("admin", "qqq11q"), "The authentication should fail with incorrect password.");
        logger.info("Completed test for incorrect password.");
    }


    @Test
    public void testSuccessfulLogin() {
        Assert.assertTrue(performLogin("admin", "admin"), "Authentication failed with valid credentials.");
        logger.info("Completed positive test for successful login.");
    }

    @AfterClass
    public void tearDown() {
        try {
            Thread.sleep(3000); // Затримка 3 секунди перед закриттям браузера
        } catch (InterruptedException e) {
            logger.severe("Interrupted during sleep: " + e.getMessage());
        }
        if (driver != null) {
            driver.quit();
            logger.info("Browser closed successfully.");
        }
    }


    public boolean performLogin(String username, String password) {
        try {
            Thread.sleep(500);

            WebElement usernameField = driver.findElement(By.name("username"));
            usernameField.clear();
            usernameField.sendKeys(username);

            Thread.sleep(500);

            WebElement passwordField = driver.findElement(By.name("password"));
            passwordField.clear();
            passwordField.sendKeys(password);

            Thread.sleep(500);

            WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            loginButton.click();

            return driver.getTitle().contains("Dashboard");

        } catch (Exception e) {
            logger.severe("Login failed: " + e.getMessage());
            return false;
        }
    }
}
