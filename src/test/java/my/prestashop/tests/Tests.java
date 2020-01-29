package my.prestashop.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Tests {

    WebDriver driver = new ChromeDriver();

    @Test
    public void checkSearch() {
        //1.Открыть главную страницу сайта
        HomePage homePage = new HomePage(driver);
        homePage.openPage(driver);


        //2.Выполнить проверку, что цена продуктов в секции "Популярные товары" указана
        // в соответствии с установленной валютой в шапке сайта (USD, EUR, UAH).
        String settedCurrency = homePage.getSettedCurrency(driver);
        for(int c = 1; c <= homePage.products.size(); c++) {
            Assert.assertEquals(homePage.getProductPriceCurrency(driver, c), settedCurrency);
        }

        homePage.setCurrency(driver, "$");
        for(int c = 1; c <= homePage.products.size(); c++) {
            Assert.assertEquals(homePage.getProductPriceCurrency(driver, c), "$");
        }

        homePage.setCurrency(driver, "€");
        for(int c = 1; c <= homePage.products.size(); c++) {
            Assert.assertEquals(homePage.getProductPriceCurrency(driver, c), "$");
        }

        homePage.setCurrency(driver, "₴");
        for(int c = 1; c <= homePage.products.size(); c++) {
            Assert.assertEquals(homePage.getProductPriceCurrency(driver, c), "$");
        }


        //3.Установить показ цены в USD используя выпадающий список в шапке сайта.
        homePage.setCurrency(driver, "$");


        //4.Выполнить поиск в каталоге по слову “dress.”
        homePage.searchByWord(driver, "dress");


        //5.Выполнить проверку, что страница "Результаты поиска" содержит надпись "Товаров: x",
        // где x - количество действительно найденных товаров.
        SearchPage searchPage = new SearchPage(driver);
        int totalProductsInMessage = searchPage.getTotalProductsFromMessage(driver);
        int actualNumberOfProducts = searchPage.getActualNumberOfProducts(driver);
        Assert.assertEquals(totalProductsInMessage, actualNumberOfProducts);


        //6.Проверить, что цена всех показанных результатов отображается в долларах.
        for(int c = 1; c <= searchPage.getActualNumberOfProducts(driver); c++) {
            Assert.assertEquals(searchPage.getProductPriceCurrency(driver, c), "$");
        }


        //7.Установить сортировку "от высокой к низкой."
        searchPage.productSortField.click();
        searchPage.sortOptionPriceDesc.click();
        searchPage.waitProductsAreSorted_priceDesc(driver);


        //8.Проверить, что товары отсортированы по цене, при этом некоторые товары могут быть со скидкой,
        // и при сортировке используется цена без скидки.
        for(int c = 1; c < searchPage.products.size(); c++) {
            double currentProductPrice = searchPage.isProductWithDiscount(driver, c) ?
                    searchPage.getProductRegularPrice(driver, c) : searchPage.getProductActualPrice(driver, c);
            double nextProductPrice = searchPage.isProductWithDiscount(driver, c + 1) ?
                    searchPage.getProductRegularPrice(driver, c + 1) : searchPage.getProductActualPrice(driver, c + 1);

            Assert.assertTrue(currentProductPrice >= nextProductPrice);
        }


        //9.Для товаров со скидкой указана скидка в процентах вместе с ценой до и после скидки.
        //10.Необходимо проверить, что цена до и после скидки совпадает с указанным размером скидки.
        for(int c = 1; c <= searchPage.products.size(); c++) {
            if(searchPage.isProductWithDiscount(driver, c)) {
                double regularPrice = searchPage.getProductRegularPrice(driver, c);
                double actualPrice = searchPage.getProductActualPrice(driver, c);
                double expectedDiscountPercent = searchPage.getProductDiscountPercent(driver, c);
                double actualDiscountPercent = Math.round(100 - (actualPrice / regularPrice) * 100);

                Assert.assertEquals(expectedDiscountPercent, actualDiscountPercent);
            }
        }
    }
}
