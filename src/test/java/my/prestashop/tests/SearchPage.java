package my.prestashop.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class SearchPage {
    public String productSortFieldCssLocator = ".products-sort-order";
    public String sortPriceDescOptionXpathLocator = "//a[contains(@href, 'product.price.desc')]";
    public String totalProductsMessageCssLocator = ".total-products";
    public String allProductsCssSelector = ".product-miniature";

    public boolean checkNumOfProducts(WebDriver driver) {
        WebElement totalProductsMessage = driver.findElement(By.cssSelector(totalProductsMessageCssLocator));
        String totalProductsNumber = totalProductsMessage.getText().replaceAll("\\D+", "");
        int numOfProducts = Integer.parseInt(totalProductsNumber);
        List<WebElement> products = driver.findElements(By.cssSelector(allProductsCssSelector));
        return (numOfProducts == products.size());
    }

    public boolean checkProductsCurrency(WebDriver driver, String currency) {
        int numOfProducts = driver.findElements(By.cssSelector(allProductsCssSelector)).size();
        for(int c = 1; c <= numOfProducts; c++) {
            String productCssLocator = ".product-miniature:nth-child(" + Integer.toString(c) + ") .price";
            WebElement productPrice = driver.findElement(By.cssSelector(productCssLocator));
            if(productPrice.getText().contains(currency)) continue;
            else return false;
        }
        return true;
    }
}
