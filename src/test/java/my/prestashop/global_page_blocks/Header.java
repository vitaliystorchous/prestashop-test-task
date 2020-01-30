package my.prestashop.global_page_blocks;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Header {
    String currencySelectorFieldCssLocator = ".currency-selector";
    String currencySelectorDropdownCssLocator = ".currency-selector .expand-more";
    public String usdSign = "$";
    public String eurSign = "€";
    public String uahSign = "₴";



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

    public String getSettedCurrency(WebDriver driver) {
        checkCurrencySelector(driver);
        if(driver.findElement(By.cssSelector(currencySelectorFieldCssLocator)).getText().contains(usdSign)) return usdSign;
        else if(driver.findElement(By.cssSelector(currencySelectorFieldCssLocator)).getText().contains(eurSign)) return eurSign;
        else if(driver.findElement(By.cssSelector(currencySelectorFieldCssLocator)).getText().contains(uahSign)) return uahSign;
        else return null;
    }
}
