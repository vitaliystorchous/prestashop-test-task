package my.prestashop.general;

import org.openqa.selenium.WebDriver;

public class SiteLauncher {
    public String homePageURL = "http://prestashop-automation.qatestlab.com.ua/ru/";
    //как пример, что бы я еще сюда поместил
    public String userName;
    public String password;

    public void openHomePage(WebDriver driver) {
        driver.get(homePageURL);
    }
}
