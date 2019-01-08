package pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

import static driverFactory.DriverFactory.driver;

public class BasePage {

    public BasePage() {
        PageFactory.initElements(driver(), this);
    }

    @FindBy(css = "a[id='secondarySlot']")
    public WebElement loginLink;

    @FindBy(css = "[id='input_phone']")
    public WebElement phoneNumberInput;

    @FindBy(css = "[id='input_password']")
    public WebElement passwordInput;

    @FindBy(css = "[id='bet_initial']")
    public WebElement infoSection;

    @FindBy(css = "[value='Log in']")
    public WebElement loginButton;

    @FindBy(css = "[class='event-odds']")
    public List<WebElement> betOptions;

    @FindBy(css = "[name='stake-input']")
    public WebElement stakeInput;

    @FindBy(css = "[value='Place bet']")
    public WebElement placeBetButton;

    @FindBy(css = "[id='Coupon-Status-Message'].notify.success")
    public WebElement successNotification;

    @FindBy(css = "[class='count bold']")
    public WebElement balanceInfo;

    @FindBy(css = "[id='Coupon-Status-Message']>a")
    public WebElement betInfoLink;

    @FindBy(css = "[id='Main-Menu-Button']")
    public WebElement mainMenuButton;

    @FindBy(css = "[id='statement-menu-top']")
    public WebElement statementInfo;

    @FindBy(css = "[class='plustxt bold']")
    public WebElement betSlipTag;

    @FindBy(css = "[class='statement']>tbody>tr>td>a")
    public List<WebElement> statementActionInfo;

    @FindBy(css = "[class='content-wrapper']")
    public WebElement betSlipContent;
}
