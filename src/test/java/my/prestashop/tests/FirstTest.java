package my.prestashop.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class FirstTest {

    WebDriver driver = new ChromeDriver();

    @Test
    public void checkPriceCurrency() {
        driver.get("http://prestashop-automation.qatestlab.com.ua/ru/");
        checkCurrencySelector();
        driver.findElement(By.cssSelector(".currency-selector .expand-more")).click();

        for (Integer c = 1; c <= driver.findElements(By.cssSelector(".currency-selector .dropdown-menu li")).size(); c++) {
            String currencyLocator = ".currency-selector .dropdown-menu li:nth-child(" + c.toString() + ")";
            WebElement currency = driver.findElement(By.cssSelector(currencyLocator));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String currencySign = currency.getText().substring(currency.getText().length() - 1);

            currency.click();

            for(Integer i = 1; i < driver.findElements(By.cssSelector(".products .product-miniature")).size(); i++) {
                String productPriceLocator = ".products .product-miniature:nth-child(" + i.toString() + ") .price";
                String prodPrice = driver.findElement(By.cssSelector(productPriceLocator)).getText();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String prodPriceCurrencySign = prodPrice.substring(prodPrice.length() - 1);
                Assert.assertEquals(currencySign, prodPriceCurrencySign);
            }

            checkCurrencySelector();
            driver.findElement(By.cssSelector(".currency-selector .expand-more")).click();
        }

        driver.quit();
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
