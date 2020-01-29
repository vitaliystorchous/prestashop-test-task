package my.prestashop.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import my.prestashop.pages.*;

public class Tests {

    WebDriver driver;

    @BeforeTest
    public void openBrowser() {
        driver = new ChromeDriver();
    }

    @Test
    public void runTests() {
        //1.Открыть главную страницу сайта
        HomePage homePage = new HomePage(driver);
        homePage.openPage(driver);


        //2.Выполнить проверку, что цена продуктов в секции "Популярные товары" указана
        // в соответствии с установленной валютой в шапке сайта (USD, EUR, UAH).
        String settedCurrency = homePage.header.getSettedCurrency(driver);
        for(int c = 1; c <= homePage.prodSection.products.size(); c++) {
            Assert.assertEquals(homePage.prodSection.getProductPriceCurrency(driver, c), settedCurrency);
        }

        homePage.header.setCurrency(driver, "$");
        for(int c = 1; c <= homePage.prodSection.products.size(); c++) {
            Assert.assertEquals(homePage.prodSection.getProductPriceCurrency(driver, c), "$");
        }

        homePage.header.setCurrency(driver, "€");
        for(int c = 1; c <= homePage.prodSection.products.size(); c++) {
            Assert.assertEquals(homePage.prodSection.getProductPriceCurrency(driver, c), "$");
        }

        homePage.header.setCurrency(driver, "₴");
        for(int c = 1; c <= homePage.prodSection.products.size(); c++) {
            Assert.assertEquals(homePage.prodSection.getProductPriceCurrency(driver, c), "$");
        }


        //3.Установить показ цены в USD используя выпадающий список в шапке сайта.
        homePage.header.setCurrency(driver, "$");


        //4.Выполнить поиск в каталоге по слову “dress.”
        homePage.searchByWord(driver, "dress");
        homePage = null;


        //5.Выполнить проверку, что страница "Результаты поиска" содержит надпись "Товаров: x",
        // где x - количество действительно найденных товаров.
        SearchPage searchPage = new SearchPage(driver);
        int totalProductsInMessage = searchPage.getTotalProductsFromMessage(driver);
        int actualNumberOfProducts = searchPage.prodSection.getActualNumberOfProducts(driver);
        Assert.assertEquals(totalProductsInMessage, actualNumberOfProducts);


        //6.Проверить, что цена всех показанных результатов отображается в долларах.
        for(int c = 1; c <= searchPage.prodSection.getActualNumberOfProducts(driver); c++) {
            Assert.assertEquals(searchPage.prodSection.getProductPriceCurrency(driver, c), "$");
        }


        //7.Установить сортировку "от высокой к низкой."
        searchPage.productSortField.click();
        searchPage.sortOptionPriceDesc.click();
        searchPage.prodSection.waitProductsAreSorted_priceDesc(driver);


        //8.Проверить, что товары отсортированы по цене, при этом некоторые товары могут быть со скидкой,
        // и при сортировке используется цена без скидки.
        for(int c = 1; c < searchPage.prodSection.products.size(); c++) {
            double currentProductPrice = searchPage.prodSection.isProductWithDiscount(driver, c) ?
                    searchPage.prodSection.getProductRegularPrice(driver, c) : searchPage.prodSection.getProductActualPrice(driver, c);
            double nextProductPrice = searchPage.prodSection.isProductWithDiscount(driver, c + 1) ?
                    searchPage.prodSection.getProductRegularPrice(driver, c + 1) : searchPage.prodSection.getProductActualPrice(driver, c + 1);

            Assert.assertTrue(currentProductPrice >= nextProductPrice);
        }


        //9.Для товаров со скидкой указана скидка в процентах вместе с ценой до и после скидки.
        //10.Необходимо проверить, что цена до и после скидки совпадает с указанным размером скидки.
        for(int c = 1; c <= searchPage.prodSection.products.size(); c++) {
            if(searchPage.prodSection.isProductWithDiscount(driver, c)) {
                double regularPrice = searchPage.prodSection.getProductRegularPrice(driver, c);
                double actualPrice = searchPage.prodSection.getProductActualPrice(driver, c);
                double expectedDiscountPercent = searchPage.prodSection.getProductDiscountPercent(driver, c);
                double actualDiscountPercent = Math.round(100 - (actualPrice / regularPrice) * 100);

                Assert.assertEquals(expectedDiscountPercent, actualDiscountPercent);
            }
        }
    }

    @AfterTest
    public void closeBrowser() {
        driver.quit();
    }
}
