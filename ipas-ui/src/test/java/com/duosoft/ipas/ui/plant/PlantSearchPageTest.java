package com.duosoft.ipas.ui.plant;

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
public class PlantSearchPageTest extends ConfigurationUiTest {

    @Test
    public void testUntitledTestCase() {

        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(baseUrl + "/plants_and_breeds/search");
        authorization();

        // Title
        assertEquals("Търсене на сортове растения и породи животни", driver.getTitle());

        // breadcrumbs
        assertEquals("Начало", driver.findElement(By.xpath("//*[@id=\"breadcrumbs\"]/ol/li[1]/a")).getText());
        assertEquals(baseUrl + "/home", driver.findElement(By.xpath("//*[@id=\"breadcrumbs\"]/ol/li[1]/a")).getAttribute("href"));
        assertEquals("Търсене на сортове растения и породи животни", driver.findElement(By.xpath("//*[@id=\"breadcrumbs\"]/ol/li[2]/span")).getText() );

        // left side panel
        if (driver.findElement(By.xpath("/html/body")).getAttribute("class").equals("dark")) {
            driver.findElement(By.xpath("//*[@id=\"btn-menu\"]")).click();
        }

        assertEquals("Наименование на български", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[1]/span")).getText());
        if (!driver.findElement(By.xpath("//*[@id=\"title\"]")).isEnabled()) {
            driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[1]/label")).click();
        }
        assertEquals("Наименование на английски", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[2]/span")).getText());
        assertEquals("Произход, поддържане и размножаване", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[3]/span")).getText());
        assertEquals("Таксон", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[4]/span")).getText());
        assertEquals("Заявителски номер", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[5]/span")).getText());
        assertEquals("Дата на заявяване", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[6]/span")).getText());
        assertEquals("Регистров номер", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[7]/span")).getText());
        assertEquals("Дата на регистрация", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[8]/span")).getText());
        assertEquals("Начало на закрила", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[9]/span")).getText());
        assertEquals("Срок на закрила", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[10]/span")).getText());
        assertEquals("Заявител/притежател", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[11]/span")).getText());
        assertEquals("Изобретател", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[12]/span")).getText());
        assertEquals("ПИС", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[13]/span")).getText());
        assertEquals("Статус", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[14]/span")).getText());
        assertEquals("Действия", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[15]/span")).getText());

        if (driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[16]/span")).getText().equals("")) {
            scrolPage(js);
        }
        assertEquals("Данни за публикации", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[16]/span")).getText());

        if (driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[17]/span")).getText().equals("")) {
            scrolPage(js);
        }
        assertEquals("Клас по МПК", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[17]/span")).getText());

        if (driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[18]/span")).getText().equals("")) {
            scrolPage(js);
        }
        assertEquals("Предложено наименование на български", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[18]/span")).getText());

        if (driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[19]/span")).getText().equals("")) {
            scrolPage(js);
        }
        assertEquals("Предложено наименование на английски", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[19]/span")).getText());

        if (driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[20]/span")).getText().equals("")) {
            scrolPage(js);
        }
        assertEquals("Публикувано наименование на български", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[20]/span")).getText());

        if (driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[21]/span")).getText().equals("")) {
            scrolPage(js);
        }
        assertEquals("Публикувано наименование на английски", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[21]/span")).getText());

        if (driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[22]/span")).getText().equals("")) {
            scrolPage(js);
        }
        assertEquals("Oдобрено наименование на български", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[22]/span")).getText());

        if (driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[23]/span")).getText().equals("")) {
            scrolPage(js);
        }
        assertEquals("Oдобрено наименование на английски", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[23]/span")).getText());

        if (driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[24]/span")).getText().equals("")) {
            scrolPage(js);
        }
        assertEquals("Oтказано наименование на български", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[24]/span")).getText());

        if (driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[25]/span")).getText().equals("")) {
            scrolPage(js);
        }
        assertEquals("Oтказано наименование на английски", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[25]/span")).getText());

        if (driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[26]/span")).getText().equals("")) {
            scrolPage(js);
        }
        assertEquals("Признак (характеристика)", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[26]/span")).getText());

        if (driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[27]/span")).getText().equals("")) {
            scrolPage(js);
        }
        assertEquals("Устойчивост", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[27]/span")).getText());

        if (driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[28]/span")).getText().equals("")) {
            scrolPage(js);
        }
        assertEquals("Условия за изпитване", driver.findElement(By.xpath("//*[@id=\"detail-list\"]/li[28]/span")).getText());

        // main block
        assertTrue(driver.findElement(By.xpath("//*[@id=\"result\"]")).getText().equals(""));
        // searching
        driver.findElement(By.xpath("//*[@id=\"submit-form\"]")).click();
        // result panel
        assertThat( driver.findElement(By.xpath("//*[@id=\"result\"]/div/div[1]/div/span")).getText(), CoreMatchers.containsString("Общ брой: "));

    }

    private void scrolPage(JavascriptExecutor js) {
        js.executeScript("document.getElementById(\"psb\").scrollTop += 300;");
    }
}
