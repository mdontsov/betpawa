package driverFactory;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
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
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(2))
                .ignoring(NoSuchElementException.class)
                .ignoring(ElementNotVisibleException.class)
                .ignoring(ElementNotInteractableException.class)
                .ignoring(ElementNotSelectableException.class);
    }

    default void click(Object object) throws NoSuchElementException, InvalidArgumentException {
        if (object instanceof WebElement) {
            ((WebElement) object).click();
        } else {
            List<WebElement> elements = (List<WebElement>) object;
            for (WebElement element : elements) {
                element.click();
            }
        }
    }

    default void clickAny(List<WebElement> elements, int times) {
        fluentWait().until(ExpectedConditions.visibilityOfAllElements(elements));
        Random r = new Random();
        while (times > 0) {
            --times;
            WebElement element = elements.get(r.nextInt(elements.size()));
            scrollTo(element);
            webDriverWait().until(ExpectedConditions.elementToBeClickable(element));
            click(element);
        }
    }

    default boolean isClickable(Object object) throws NoSuchElementException {
        if (object instanceof WebElement) {
            webDriverWait().until(ExpectedConditions.elementToBeClickable((WebElement) object));
            return true;
        } else if (object instanceof List<?>) {
            List<WebElement> elements = (List<WebElement>) object;
            elements.forEach(element -> webDriverWait().until(ExpectedConditions.elementToBeClickable(element)));
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

    default void displayAlert(String alertMessage) {
        ((JavascriptExecutor) driver()).executeScript("(alert( ' " + alertMessage + " '));");
    }

    default void scrollTo(WebElement element) {
        Actions actions = new Actions(driver());
        fluentWait().until(ExpectedConditions.visibilityOf(element));
        actions.moveToElement(element);
        actions.perform();
    }
}
