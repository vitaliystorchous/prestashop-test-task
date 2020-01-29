package my.prestashop.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchPage {
    public String productSortFieldCssLocator = ".products-sort-order";
    public String sortOptionPriceDescXpathLocator = "//a[contains(@href, 'product.price.desc')]";
    public String totalProductsMessageCssLocator = ".total-products";
    public String allProductsCssLocator = ".product-miniature";
    public String productPriceCssLocator = ".product-miniature:nth-child(Index) .price";
    public String usdSign = "$";
    public String eurSign = "€";
    public String uahSign = "₴";

    WebElement productSortField;
    WebElement sortOptionPriceDesc;
    WebElement totalProductsMessage;
    List<WebElement> products;


    public SearchPage(WebDriver driver) {
        productSortField = driver.findElement(By.cssSelector(productSortFieldCssLocator));
        sortOptionPriceDesc = driver.findElement(By.xpath(sortOptionPriceDescXpathLocator));
        totalProductsMessage = driver.findElement(By.cssSelector(totalProductsMessageCssLocator));
        products = driver.findElements(By.cssSelector(allProductsCssLocator));
    }


    public int getTotalProductsFromMessage(WebDriver driver) {
        String totalProducts = totalProductsMessage.getText().replaceAll("\\D+", "");
        return Integer.parseInt(totalProducts);
    }

    public int getActualNumberOfProducts(WebDriver driver) {
        return products.size();
    }

    public String getProductPriceCurrency(WebDriver driver, int productIndex) {
        WebElement productPrice = driver.findElement(By.cssSelector(productPriceCssLocator.replaceAll("Index", Integer.toString(productIndex))));
        if(productPrice.getText().contains(usdSign)) { return usdSign; }
        else if(productPrice.getText().contains(eurSign)) { return eurSign; }
        else if(productPrice.getText().contains(uahSign)) {return uahSign; }
        else return null;
    }

    double getPrice(String p) {
        p = p.replaceAll("[^,0-9]", "");
        p = p.replaceAll(",", ".");
        return Double.parseDouble(p);
    }

    public double getHighestProductPrice(WebDriver driver) {
        double highestPrice = 0;
        for(int c = 1; c <= products.size(); c++) {
            WebElement productPrice = driver.findElement(By.cssSelector(productPriceCssLocator.replaceAll("Index", Integer.toString(c))));
            double price = getPrice(productPrice.getText());
            if(price > highestPrice) { highestPrice = price; }
            else continue;
        }

        return highestPrice;
    }

    public double getProductPrice(WebDriver driver, int index) {
        WebElement productPrice = driver.findElement(By.cssSelector(productPriceCssLocator.replaceAll("Index", Integer.toString(index))));
        return getPrice(productPrice.getText());
    }

    public void waitProductsAreSorted_priceDesc(WebDriver driver) {
        for(int c = 0; c < 5; c++) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(getHighestProductPrice(driver) == getProductPrice(driver, 1)) { break; }
            else { continue; }
        }
    }
}
