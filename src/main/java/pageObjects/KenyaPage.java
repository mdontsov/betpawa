package pageObjects;

import org.openqa.selenium.support.PageFactory;

import static driverFactory.DriverFactory.driver;


public class KenyaPage {

    public KenyaPage() {
        PageFactory.initElements(driver(), this);
    }
}
