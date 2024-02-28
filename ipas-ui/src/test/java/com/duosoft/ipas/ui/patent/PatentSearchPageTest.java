package com.duosoft.ipas.ui.patent;

import com.duosoft.ipas.config.ConfigurationUiTest;
import org.hamcrest.CoreMatchers;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Ignore
public class PatentSearchPageTest extends ConfigurationUiTest {

    @Test
    public void testUntitledTestCase() {

        driver.get(baseUrl + "/patent/search");
        authorization();

        // Title
        assertEquals("Търсене на национален патент", driver.getTitle());

        // breadcrumbs
        assertEquals("Начало", driver.findElement(By.xpath("//*[@id=\"breadcrumbs\"]/ol/li[1]/a")).getText());
        assertEquals(baseUrl + "/home", driver.findElement(By.xpath("//*[@id=\"breadcrumbs\"]/ol/li[1]/a")).getAttribute("href"));
        assertEquals("Търсене на национален патент", driver.findElement(By.xpath("//*[@id=\"breadcrumbs\"]/ol/li[2]/span")).getText() );

        // left side panel
        if (driver.findElement(By.xpath("/html/body")).getAttribute("class").equals("dark")) {
            driver.findElement(By.xpath("//*[@id=\"btn-menu\"]")).click();
        }

        assertEquals("Наименование", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[1]/span")).getText());
        if (!driver.findElement(By.xpath("//*[@id=\"title\"]")).isEnabled()) {
            driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[1]/label")).click();
        }
        assertEquals("Ключови думи от реферата", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[2]/span")).getText());
        assertEquals("Заявителски номер", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[3]/span")).getText());
        assertEquals("Дата на заявяване", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[4]/span")).getText());
        assertEquals("Регистров номер", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[5]/span")).getText());
        assertEquals("Дата на регистрация", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[6]/span")).getText());
        assertEquals("Начало на закрила", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[7]/span")).getText());
        assertEquals("Срок на закрила", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[8]/span")).getText());
        assertEquals("Заявител/притежател", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[9]/span")).getText());
        assertEquals("Изобретател", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[10]/span")).getText());
        assertEquals("ПИС", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[11]/span")).getText());
        assertEquals("Статус", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[12]/span")).getText());
        assertEquals("Действия", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[13]/span")).getText());
        assertEquals("Данни за публикации", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[14]/span")).getText());
        assertEquals("Клас по МПК", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[15]/span")).getText());

        // main block
        assertTrue(driver.findElement(By.xpath("//*[@id=\"result\"]")).getText().equals(""));
        // searching
        driver.findElement(By.xpath("//*[@id=\"submit-form\"]")).click();
        // result panel
        assertThat( driver.findElement(By.xpath("//*[@id=\"result\"]/div/div[1]/div/span")).getText(), CoreMatchers.containsString("Общ брой: "));

    }
}
