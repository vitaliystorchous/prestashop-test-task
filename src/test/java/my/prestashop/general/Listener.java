package my.prestashop.general;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Listener extends AbstractWebDriverEventListener {
    File logFile = new File("logs\\logs.txt");
    FileWriter writer;
    {
        try {
            writer = new FileWriter(logFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        try { writer.write("\n" + by.toString() + " - searching"); }
        catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        try { writer.write("\n" + by.toString() + " - found"); }
        catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        try { writer.write("\n" + element.getText() + " - clicking"); }
        catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        System.out.println(throwable);
    }
}
