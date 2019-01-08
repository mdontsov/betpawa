package pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static driverFactory.DriverFactory.driver;

public class UgandaPage {

    public UgandaPage() {
        PageFactory.initElements(driver(), this);
    }

    @FindBy(css = "a[id='secondarySlot']")
    public WebElement loginLink;

    @FindBy(css = "[id='input_phone']")
    public WebElement phoneNumberInput;

    @FindBy(css = "[id='input_password']")
    public WebElement passwordInput;
}
