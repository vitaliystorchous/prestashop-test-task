package my.prestashop.tests;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import my.prestashop.pages.*;
import my.prestashop.general.*;

public class TestSuit {

    EventFiringWebDriver driver;

    @BeforeTest
    public void openBrowser() {
        driver = new EventFiringWebDriver(new ChromeDriver());
        driver.register(new Listener());
    }

    @Test
    public void runTests() {
        //1.Открыть главную страницу сайта
        SiteLauncher launcher = new SiteLauncher();
        launcher.openHomePage(driver);
        HomePage homePage = new HomePage(driver);


        //2.Выполнить проверку, что цена продуктов в секции "Популярные товары" указана
        // в соответствии с установленной валютой в шапке сайта (USD, EUR, UAH).
        String settedCurrency = homePage.header.getSettedCurrency(driver);
        for (int c = 1; c <= homePage.prodSection.products.size(); c++) {
            String productPriceCurrency = homePage.prodSection.getProductPriceCurrency(driver, c);
            //Вариант с Assert:
            Assert.assertEquals(productPriceCurrency, settedCurrency);

            //Вариант без Assert для вывода результата проверки в консоль:
            if(productPriceCurrency == settedCurrency && c < homePage.prodSection.products.size()) continue;
            else if (productPriceCurrency == settedCurrency && c == homePage.prodSection.products.size()) System.out.println("Verification №2.1 - passed");
            else {
                System.out.println("Verification №2.1 - failed" +
                        "\nProduct currency " + productPriceCurrency + " doesn't match setted currency " + settedCurrency);
                break;
            }
        }

        homePage.header.setCurrency(driver, "$");
        for (int c = 1; c <= homePage.prodSection.products.size(); c++) {
            String productPriceCurrency = homePage.prodSection.getProductPriceCurrency(driver, c);
            //Вариант с Assert:
            Assert.assertEquals(productPriceCurrency, "$");

            //Вариант без Assert для вывода результата проверки в консоль:
            if(productPriceCurrency == "$" && c < homePage.prodSection.products.size()) continue;
            else if (productPriceCurrency == "$" && c == homePage.prodSection.products.size()) System.out.println("Verification №2.2 - passed");
            else {
                System.out.println("Verification №2.2 - failed" +
                        "\nProduct currency " + productPriceCurrency + " doesn't match setted currency $");
                break;
            }
        }

        homePage.header.setCurrency(driver, "€");
        for (int c = 1; c <= homePage.prodSection.products.size(); c++) {
            String productPriceCurrency = homePage.prodSection.getProductPriceCurrency(driver, c);
            //Вариант с Assert:
            Assert.assertEquals(productPriceCurrency, "€");

            //Вариант без Assert для вывода результата проверки в консоль:
            if(productPriceCurrency == "€" && c < homePage.prodSection.products.size()) continue;
            else if (productPriceCurrency == "€" && c == homePage.prodSection.products.size()) System.out.println("Verification №2.3 - passed");
            else {
                System.out.println("Verification №2.3 - failed" +
                        "\nProduct currency " + productPriceCurrency + " doesn't match setted currency €");
                break;
            }
        }

        homePage.header.setCurrency(driver, "₴");
        for (int c = 1; c <= homePage.prodSection.products.size(); c++) {
            String productPriceCurrency = homePage.prodSection.getProductPriceCurrency(driver, c);
            //Вариант с Assert:
            Assert.assertEquals(productPriceCurrency, "₴");

            //Вариант без Assert для вывода результата проверки в консоль:
            if(productPriceCurrency == "₴" && c < homePage.prodSection.products.size()) continue;
            else if (productPriceCurrency == "₴" && c == homePage.prodSection.products.size()) System.out.println("Verification №2.4 - passed");
            else {
                System.out.println("Verification №2.4 - failed" +
                        "\nProduct currency " + productPriceCurrency + " doesn't match setted currency ₴");
                break;
            }
        }


        //3.Установить показ цены в USD используя выпадающий список в шапке сайта.
        homePage.header.setCurrency(driver, "$");


        //4.Выполнить поиск в каталоге по слову “dress.”
        homePage.searchByWord(driver, "dress");
        SearchPage searchPage = new SearchPage(driver);


        //5.Выполнить проверку, что страница "Результаты поиска" содержит надпись "Товаров: x",
        // где x - количество действительно найденных товаров.
        int totalProductsInMessage = searchPage.getTotalProductsFromMessage(driver);
        int actualNumberOfProducts = searchPage.prodSection.getActualNumberOfProducts(driver);
        //Вариант с Assert:
        Assert.assertEquals(totalProductsInMessage, actualNumberOfProducts);

        //Вариант без Assert для вывода результата проверки в консоль:
        if(totalProductsInMessage == actualNumberOfProducts) System.out.println("Verification №5 - passed");
        else System.out.println("Verification №5 - failed" +
                "\nTotal products in message (" + totalProductsInMessage + ") isn't equal to actual number of products (" + actualNumberOfProducts + ")");


        //6.Проверить, что цена всех показанных результатов отображается в долларах.
        for(int c = 1; c <= searchPage.prodSection.getActualNumberOfProducts(driver); c++) {
            String productPriceCurrency = homePage.prodSection.getProductPriceCurrency(driver, c);
            //Вариант с Assert:
            Assert.assertEquals(productPriceCurrency, "$");

            //Вариант без Assert для вывода результата проверки в консоль:
            if(productPriceCurrency == "$" && c < homePage.prodSection.products.size()) continue;
            else if (productPriceCurrency == "$" && c == homePage.prodSection.products.size()) System.out.println("Verification №6 - passed");
            else {
                System.out.println("Verification №6 - failed" +
                        "\nProduct currency " + productPriceCurrency + " doesn't match setted currency $");
                break;
            }
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

            //Вариант с Assert:
            Assert.assertTrue(currentProductPrice >= nextProductPrice);

            //Вариант без Assert для вывода результата проверки в консоль:
            if(currentProductPrice >= nextProductPrice && c < searchPage.prodSection.products.size() - 1) continue;
            else if (currentProductPrice >= nextProductPrice && c == searchPage.prodSection.products.size() - 1) System.out.println("Verification №8 - passed");
            else System.out.println("Verification №8 - failed" +
                    "\nCurrent product price (" + currentProductPrice + ") is not bigger or equal to the next product price (" + nextProductPrice + ")");
        }


        //9.Для товаров со скидкой указана скидка в процентах вместе с ценой до и после скидки.
        //10.Необходимо проверить, что цена до и после скидки совпадает с указанным размером скидки.
        for(int c = 1; c <= searchPage.prodSection.products.size(); c++) {
            if(searchPage.prodSection.isProductWithDiscount(driver, c)) {
                double regularPrice = searchPage.prodSection.getProductRegularPrice(driver, c);
                double actualPrice = searchPage.prodSection.getProductActualPrice(driver, c);
                double expectedDiscountPercent = searchPage.prodSection.getProductDiscountPercent(driver, c);
                double actualDiscountPercent = Math.round(100 - (actualPrice / regularPrice) * 100);

                //Вариант с Assert:
                Assert.assertEquals(expectedDiscountPercent, actualDiscountPercent);

                //Вариант без Assert для вывода результата проверки в консоль:
                if(expectedDiscountPercent != actualDiscountPercent) {
                    System.out.println("Verification №10 - failed" +
                            "\nExpected discount percent (" + expectedDiscountPercent + ") isn't equal to the actual discount percent (" + actualDiscountPercent + ")");
                    break;
                }
            }
            if(c == searchPage.prodSection.products.size()) System.out.println("Verification №10 - passed");
        }
    }

    @AfterTest
    public void closeBrowser() {
        driver.quit();
    }
}
