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
        Assert.assertEquals(numOfProducts, driver.findElements(By.cssSelector(".product-miniature")).size());

        //6.Проверить, что цена всех показанных результатов отображается в долларах.
        for(int c = 1; c <= numOfProducts; c++) {
            String productsCssLocator = ".product-miniature:nth-child(" + Integer.toString(c) + ") .price";
            Assert.assertEquals(true, driver.findElement(By.cssSelector(productsCssLocator)).getText().contains("$"));
        }

        //7.Установить сортировку "от высокой к низкой."
        driver.findElement(By.cssSelector(".products-sort-order")).click();
        driver.findElement(By.xpath("//a[contains(@href, 'product.price.desc')]")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //8.Проверить, что товары отсортированы по цене, при этом некоторые товары могут быть со скидкой,
        // и при сортировке используется цена без скидки.
        for(int c = 1; c < numOfProducts; c++) {
            String currentProductsCssLocator = ".product-miniature:nth-child(" + Integer.toString(c) + ")";
            String currentProductPrice;
            if (driver.findElements(By.cssSelector(currentProductsCssLocator + " .regular-price")).size() > 0) {
                currentProductPrice = driver.findElement(By.cssSelector(currentProductsCssLocator + " .regular-price")).getText();
            } else {
                currentProductPrice = driver.findElement(By.cssSelector(currentProductsCssLocator + " .price")).getText();
            }
            double currentPrice = getPrice(currentProductPrice);

            String nextProductsCssLocator = ".product-miniature:nth-child(" + Integer.toString(c + 1) + ")";
            String nextProductsPrice;
            if (driver.findElements(By.cssSelector(nextProductsCssLocator + " .regular-price")).size() > 0) {
                nextProductsPrice = driver.findElement(By.cssSelector(nextProductsCssLocator + " .regular-price")).getText();
            } else {
                nextProductsPrice = driver.findElement(By.cssSelector(nextProductsCssLocator + " .price")).getText();
            }
            double nextPrice = getPrice(nextProductsPrice);

            Assert.assertEquals(true, (currentPrice >= nextPrice));
        }

        //9.Для товаров со скидкой указана скидка в процентах вместе с ценой до и после скидки.
        //10.Необходимо проверить, что цена до и после скидки совпадает с указанным размером скидки.
        for(int c = 1; c <= numOfProducts; c++) {
            String currentProductsCssLocator = ".product-miniature:nth-child(" + Integer.toString(c) + ")";
            if (driver.findElements(By.cssSelector(currentProductsCssLocator + " .regular-price")).size() > 0) {
                double discountPercent = getPrice(driver.findElement(By.cssSelector(currentProductsCssLocator + " .discount-percentage")).getText());
                double regularPrice = getPrice(driver.findElement(By.cssSelector(currentProductsCssLocator + " .regular-price")).getText());
                double actualPrice = getPrice(driver.findElement(By.cssSelector(currentProductsCssLocator + " .price")).getText());
                System.out.println("discountPercent = " + discountPercent + "\nregularPrice = " + regularPrice + "\nactualPrice = " + actualPrice);
                //цены и размер скидки получил, осталось только посчитать что все расчитали правильно
            }
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

    double getPrice(String p) {
        p = p.replaceAll("[^,0-9]", "");
        p = p.replaceAll(",", ".");
        return Double.parseDouble(p);
    }
}
