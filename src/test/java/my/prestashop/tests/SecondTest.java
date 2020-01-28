package my.prestashop.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SecondTest {

    WebDriver driver = new ChromeDriver();

    @Test
    public void checkSearch() {
        //1.Открыть главную страницу сайта
        HomePage homePage = new HomePage();
        homePage.openPage(driver);

        //3.Установить показ цены в USD используя выпадающий список в шапке сайта.
        homePage.setCurrency(driver, "$");

        //4.Выполнить поиск в каталоге по слову “dress.”
        homePage.searchByWord(driver, "dress");


        //5.Выполнить проверку, что страница "Результаты поиска" содержит надпись "Товаров: x",
        // где x - количество действительно найденных товаров.
        SearchPage searchPage = new SearchPage();
        Assert.assertEquals(searchPage.checkNumOfProducts(driver), true);


        //6.Проверить, что цена всех показанных результатов отображается в долларах.
        Assert.assertEquals(searchPage.checkProductsCurrency(driver, "$"), true);

        /*
        //7.Установить сортировку "от высокой к низкой."
        driver.findElement(By.cssSelector(".products-sort-order")).click();
        driver.findElement(By.xpath("//a[contains(@href, 'product.price.desc')]")).click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.cssSelector(".products-sort-order")), driver.findElement(By.xpath("//a[contains(@href, 'product.price.desc')]")).getText()));
        double highestPrice = getHighestPrice(numOfProducts);
        double firstProductPrice = getPrice(driver.findElement(By.cssSelector(" .price")).getText());
        int counter = 0;
        while (highestPrice == firstProductPrice) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter++;
            if(counter == 10) return;
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

            Assert.assertEquals((currentPrice >= nextPrice), true);
        }

        //9.Для товаров со скидкой указана скидка в процентах вместе с ценой до и после скидки.
        //10.Необходимо проверить, что цена до и после скидки совпадает с указанным размером скидки.
        for(int c = 1; c <= numOfProducts; c++) {
            String currentProductsCssLocator = ".product-miniature:nth-child(" + Integer.toString(c) + ")";
            if (driver.findElements(By.cssSelector(currentProductsCssLocator + " .regular-price")).size() > 0) {
                double displayedDiscountPercent = getPercent(driver.findElement(By.cssSelector(currentProductsCssLocator + " .discount-percentage")).getText());
                double regularPrice = getPrice(driver.findElement(By.cssSelector(currentProductsCssLocator + " .regular-price")).getText());
                double actualPrice = getPrice(driver.findElement(By.cssSelector(currentProductsCssLocator + " .price")).getText());
                double actualDiscountPercent = Math.round(100 - (actualPrice / regularPrice) * 100);
                Assert.assertEquals(displayedDiscountPercent, actualDiscountPercent);
            }
        }

         */
    }

    double getPrice(String p) {
        p = p.replaceAll("[^,0-9]", "");
        p = p.replaceAll(",", ".");
        return Double.parseDouble(p);
    }

    double getPercent(String p) {
        p = p.replaceAll("[^,0-9]", "");
        p = p.replaceAll(",", ".");
        return Double.parseDouble(p);
    }

    double getHighestPrice(int numberOfProducts) {
        double highestPrice = 0.0;
        for (int c = 1; c <= numberOfProducts; c++) {
            String productsCssLocator = ".product-miniature:nth-child(" + Integer.toString(c) + ")";
            String currentProductPrice;
            if (driver.findElements(By.cssSelector(productsCssLocator + " .regular-price")).size() > 0) {
                currentProductPrice = driver.findElement(By.cssSelector(productsCssLocator + " .regular-price")).getText();
            } else {
                currentProductPrice = driver.findElement(By.cssSelector(productsCssLocator + " .price")).getText();
            }
            double price = getPrice(currentProductPrice);
            if(price > highestPrice) {highestPrice = price;}
            else continue;
        }
        return highestPrice;
    }
}
