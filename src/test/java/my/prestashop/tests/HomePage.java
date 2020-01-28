package my.prestashop.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage {
    String homePageURL = "http://prestashop-automation.qatestlab.com.ua/ru/";
    String currencySelectorCssLocator = ".currency-selector .expand-more";
    String searchFieldNameLocator = "s";
    String searchSubmitButtonCssLocator = "[type=\"submit\"] .search";


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
        WebElement currencySelector = driver.findElement(By.cssSelector(currencySelectorCssLocator));
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
}
