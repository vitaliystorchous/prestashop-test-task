package my.prestashop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import my.prestashop.global_page_blocks.*;

public class SearchPage {
    //Locators of page elements
    public String productSortFieldCssLocator = ".products-sort-order";
    public String sortOptionPriceDescXpathLocator = "//a[contains(@href, 'product.price.desc')]";
    public String totalProductsMessageCssLocator = ".total-products";

    //Page elements
    public WebElement productSortField;
    public WebElement sortOptionPriceDesc;
    public WebElement totalProductsMessage;

    //Page sections
    public Header header;
    public ProductsSection prodSection;

    //Constructor
    public SearchPage(WebDriver driver) {
        productSortField = driver.findElement(By.cssSelector(productSortFieldCssLocator));
        sortOptionPriceDesc = driver.findElement(By.xpath(sortOptionPriceDescXpathLocator));
        totalProductsMessage = driver.findElement(By.cssSelector(totalProductsMessageCssLocator));
        header = new Header();
        prodSection = new ProductsSection(driver);
    }


    public int getTotalProductsFromMessage(WebDriver driver) {
        String totalProducts = totalProductsMessage.getText().replaceAll("\\D+", "");
        return Integer.parseInt(totalProducts);
    }
}
