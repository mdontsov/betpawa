package pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static driverFactory.DriverFactory.driver;

public class StatementPage {

    public StatementPage() {
        PageFactory.initElements(driver(), this);
    }

    @FindBy(css = "[class='statement']>tbody>tr>td>a")
    public List<WebElement> statementBetInfo;

    @FindBy(css = "[class='statement']>tbody>tr>td.st-action")
    public List<WebElement> statementVoucherInfo;
}
