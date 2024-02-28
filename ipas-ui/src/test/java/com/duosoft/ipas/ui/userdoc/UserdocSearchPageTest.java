package com.duosoft.ipas.ui.userdoc;

import com.duosoft.ipas.config.ConfigurationUiTest;
import org.hamcrest.CoreMatchers;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Ignore
public class UserdocSearchPageTest extends ConfigurationUiTest {

    @Test
    public void testUntitledTestCase() {

        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(baseUrl + "/userdoc/search");
        authorization();

        // Title
        assertEquals("Търсене на вторични действия", driver.getTitle());

        // breadcrumbs
        assertEquals("Начало", driver.findElement(By.xpath("//*[@id=\"breadcrumbs\"]/ol/li[1]/a")).getText());
        assertEquals(baseUrl + "/home", driver.findElement(By.xpath("//*[@id=\"breadcrumbs\"]/ol/li[1]/a")).getAttribute("href"));
        assertEquals("Търсене на вторични действия", driver.findElement(By.xpath("//*[@id=\"breadcrumbs\"]/ol/li[2]/span")).getText() );

        // left side panel
        if (driver.findElement(By.xpath("/html/body")).getAttribute("class").equals("dark")) {
            driver.findElement(By.xpath("//*[@id=\"btn-menu\"]")).click();
        }

        assertEquals("Вид документ", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[1]/span")).getText());
        if (!driver.findElement(By.xpath("//*[@id=\"doc-type\"]")).isEnabled()) {
            driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[1]/label")).click();
        }
        assertEquals("Вид обект", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[2]/span")).getText());
        assertEquals("Номер на документ", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[3]/span")).getText());
        assertEquals("Дата на заявяване", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[4]/span")).getText());
        assertEquals("Статус", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[5]/span")).getText());
        assertEquals("Действия", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[6]/span")).getText());
        assertEquals("Лице", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[7]/span")).getText());

        // main block
        assertTrue(driver.findElement(By.xpath("//*[@id=\"result\"]")).getText().equals(""));
        // searching
        driver.findElement(By.xpath("//*[@id=\"submit-form\"]")).click();
        // result panel
        assertThat( driver.findElement(By.xpath("//*[@id=\"result\"]/div/div[1]/div/span")).getText(), CoreMatchers.containsString("Общ брой: "));

    }
}
