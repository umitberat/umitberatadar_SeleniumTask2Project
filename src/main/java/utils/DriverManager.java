package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverManager {
    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();

            //Bildirimleri engelleme
            options.addArguments("--disable-notifications");

            //Çerez banner engelleme
            options.setExperimentalOption("prefs", new java.util.HashMap<String, Object>() {{
                put("profile.default_content_setting_values.notifications", 2);
                put("profile.default_content_setting_values.cookies", 2);
                put("profile.default_content_setting_values.popups", 0);
            }});

            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
