package com.duosoft.ipas.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.File;
import java.util.concurrent.TimeUnit;

class SeleniumConfig {

    private final WebDriver driver;

    public SeleniumConfig(final Browsers browser) {
        switch(browser){
            case FIREFOX:
                driver = new FirefoxDriver(new FirefoxOptions());
                break;
            case CHROME:
                final ChromeOptions options = new ChromeOptions();
               // options.addArguments("headless");
               // options.addArguments("window-size=1200x600");
                driver = new ChromeDriver();
//Applied wait time
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//maximize window
                driver.manage().window().maximize();
                break;
            default:
                throw new IllegalArgumentException(String.format("%s is not a supported browser", browser));
        }
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    static {
        System.setProperty("webdriver.gecko.driver", findFile("geckodriver.exe"));
        System.setProperty("webdriver.chrome.driver", findFile("chromedriver-v.83.exe"));
    }

    static private String findFile(String filename) {
        final String paths[] = {"", "bin/"};
        for (String path : paths) {
            if (new File(path + filename).exists())
                return path + filename;
        }
        return "";
    }

    public WebDriver getDriver() {
        return driver;
    }
}
