package my.prestashop.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HomePage {
    String homePageURL = "http://prestashop-automation.qatestlab.com.ua/ru/";
    String currencySelectorFieldCssLocator = ".currency-selector";
    String currencySelectorDropdownCssLocator = ".currency-selector .expand-more";
    String searchFieldNameLocator = "s";
    String searchSubmitButtonCssLocator = "[type=\"submit\"] .search";
    String allProductsCssLocator = ".product-miniature";
    String productActualPriceCssLocator = ".product-miniature:nth-child(Index) .price";
    public String usdSign = "$";
    public String eurSign = "€";
    public String uahSign = "₴";

    List<WebElement> products;


    public HomePage(WebDriver driver) {
        products = driver.findElements(By.cssSelector(allProductsCssLocator));
    }


    public void openPage(WebDriver driver) {
        driver.get(homePageURL);
    }

    public void checkCurrencySelector(WebDriver driver) {
        if(driver.findElement(By.cssSelector(".currency-selector")).isDisplayed() == false) {
            for(int c = 0; c < 3; c++) {
                driver.navigate().refresh();
                if (driver.findElement(By.cssSelector(".currency-selector")).isDisplayed() == true) break;
            }
        }
    }

    public void setCurrency(WebDriver driver, String currency) {
        checkCurrencySelector(driver);
        WebElement currencySelector = driver.findElement(By.cssSelector(currencySelectorDropdownCssLocator));
        currencySelector.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.partialLinkText(currency)).click();
    }

    public void searchByWord(WebDriver driver, String searchWord) {
        WebElement searchField = driver.findElement(By.name(searchFieldNameLocator));
        WebElement searchSubmitButton = driver.findElement(By.cssSelector(searchSubmitButtonCssLocator));

        searchField.sendKeys(searchWord);
        searchSubmitButton.click();
    }

    public String getSettedCurrency(WebDriver driver) {
        if(driver.findElement(By.cssSelector(currencySelectorFieldCssLocator)).getText().contains(usdSign)) return usdSign;
        else if(driver.findElement(By.cssSelector(currencySelectorFieldCssLocator)).getText().contains(eurSign)) return eurSign;
        else if(driver.findElement(By.cssSelector(currencySelectorFieldCssLocator)).getText().contains(uahSign)) return uahSign;
        else return null;
    }

    public String getProductPriceCurrency(WebDriver driver, int productIndex) {
        WebElement productPrice = driver.findElement(By.cssSelector(productActualPriceCssLocator.replaceAll("Index", Integer.toString(productIndex))));
        if(productPrice.getText().contains(usdSign)) { return usdSign; }
        else if(productPrice.getText().contains(eurSign)) { return eurSign; }
        else if(productPrice.getText().contains(uahSign)) {return uahSign; }
        else return null;
    }
}
