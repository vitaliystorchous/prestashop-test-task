package my.prestashop.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SecondTest {

    WebDriver driver = new ChromeDriver();

    @Test
    public void checkSearch() {
        //1.Открыть главную страницу сайта
        driver.get("http://prestashop-automation.qatestlab.com.ua/ru/");

        //3.Установить показ цены в USD используя выпадающий список в шапке сайта.
        checkCurrencySelector();
        driver.findElement(By.cssSelector(".currency-selector .expand-more")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.partialLinkText("$")).click();

        //4.Выполнить поиск в каталоге по слову “dress.”
        driver.findElement(By.name("s")).sendKeys("dress");
        driver.findElement(By.cssSelector("[type=\"submit\"] .search")).click();

        //5.Выполнить проверку, что страница "Результаты поиска" содержит надпись "Товаров: x",
        // где x - количество действительно найденных товаров.
        String productsQuantity = driver.findElement(By.cssSelector(".total-products")).getText();
        int numOfProducts = Integer.parseInt(productsQuantity.replaceAll("\\D+", ""));
        System.out.println(numOfProducts);
        Assert.assertEquals(numOfProducts, driver.findElements(By.cssSelector(".product-miniature")).size());

        //6.Проверить, что цена всех показанных результатов отображается в долларах.
        for(int c = 1; c <= numOfProducts; c++) {
            String productsCssLocator = ".product-miniature:nth-child(" + Integer.toString(c) + ") .price";
            Assert.assertEquals(true, driver.findElement(By.cssSelector(productsCssLocator)).getText().contains("$"));
        }
    }

    void checkCurrencySelector() {
        if(driver.findElement(By.cssSelector(".currency-selector")).isDisplayed() == false) {
            for(int c = 0; c < 3; c++) {
                driver.navigate().refresh();
                if (driver.findElement(By.cssSelector(".currency-selector")).isDisplayed() == true) break;
            }
        }
    }
}
