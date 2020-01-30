package my.prestashop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import my.prestashop.global_page_blocks.*;

public class HomePage {
    //Locators of page elements
    String searchFieldNameLocator = "s";
    String searchSubmitButtonCssLocator = "[type=\"submit\"] .search";

    //Page sections
    public Header header;
    public ProductsSection prodSection;

    //Constructor
    public HomePage(WebDriver driver) {
        header = new Header();
        prodSection = new ProductsSection(driver);
    }


    //Page methods
    public void searchByWord(WebDriver driver, String searchWord) {
        WebElement searchField = driver.findElement(By.name(searchFieldNameLocator));
        WebElement searchSubmitButton = driver.findElement(By.cssSelector(searchSubmitButtonCssLocator));

        searchField.sendKeys(searchWord);
        searchSubmitButton.click();
    }
}
