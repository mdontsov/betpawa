package driverFactory;

import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static driverFactory.DriverFactory.driver;

public interface DriverActions {

    default WebDriverWait webDriverWait() {
        return new WebDriverWait(driver(), 10);
    }

    default FluentWait<WebDriver> fluentWait() {
        return new FluentWait<>(driver())
                .withTimeout(Duration.ofSeconds(5))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);
    }

    default void clickLink(Object object) throws NoSuchElementException, InvalidArgumentException {
        if (object instanceof WebElement) {
            ((WebElement) object).click();
        } else {
            List<WebElement> elements = (List<WebElement>) object;
            for (WebElement element : elements) {
                element.click();
            }
        }
    }

    default void clickAnylink(List<WebElement> elements) {
        webDriverWait().until(ExpectedConditions.visibilityOfAllElements(elements));
        Random r = new Random();
        int i = r.nextInt(elements.size());
        elements.get(i).click();
    }

    default boolean isClickable(Object object) throws NoSuchElementException {
        if (object instanceof WebElement) {
            webDriverWait().until(ExpectedConditions.elementToBeClickable((WebElement) object));
            return true;
        } else if (object instanceof List<?>) {
            List<WebElement> elements = (List<WebElement>) object;
            elements.forEach(element -> {
                webDriverWait().until(ExpectedConditions.elementToBeClickable(element));
            });
            return true;
        } else {
            return false;
        }
    }

    default void quit() {
        driver().quit();
    }

    default void openUrl(String url) {
        driver().get(url);
    }
}
