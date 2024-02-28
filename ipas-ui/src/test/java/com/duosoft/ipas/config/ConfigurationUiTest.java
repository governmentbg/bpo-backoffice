package com.duosoft.ipas.config;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import com.duosoft.ipas.IpasApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {IpasApplication.class})
@ContextConfiguration(classes = IpasDatabaseConfig.class)
@TestPropertySource(properties = {
        "cas.service.url=http://localhost:58569/j_spring_cas_security_check",
        "server.port=58569"
})
public abstract class ConfigurationUiTest {

    protected StringBuffer verificationErrors = new StringBuffer();

    @LocalServerPort
    protected String port;

    protected ChromeDriverService service;
    protected WebDriver driver;
    protected String baseUrl = "http://localhost:";

    @Before
    public void setUp() {
        baseUrl = baseUrl + port;
        driver = new SeleniumConfig(Browsers.CHROME).getDriver();
    }

    @After
    public void tearDown() {
        driver.close();
        //driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    protected void authorization() {
        if (driver.getTitle().equals("BPO Mock Authentication Server")) {
            driver.findElement(By.id("user_0")).click();
        }

        if (driver.getTitle().equals("Начало - portal.bpo.bg")) {
            driver.findElement(By.id("_58_login")).click();
            driver.findElement(By.id("_58_login")).clear();
            driver.findElement(By.id("_58_login")).sendKeys("test");
            driver.findElement(By.id("_58_password")).click();
            driver.findElement(By.id("_58_password")).clear();
            driver.findElement(By.id("_58_password")).sendKeys("p0r7aLbp0bg");
            driver.findElement(By.xpath("//*[@id=\"_58_fm\"]/div/button")).click();
        }
    }
}
