package pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

import static driverFactory.DriverFactory.driver;

public class BetPawaPage {

    public BetPawaPage() {
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

    @FindBy(css = ".event-bet")
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

    @FindBy(css = "#Main-Menu-Button")
    public WebElement mainMenuButton;

    @FindBy(css = "[id='my-bets-menu-top']")
    public WebElement betInfo;

    @FindBy(css = "[id='statement-menu-top']")
    public WebElement statementInfo;

    @FindBy(css = "[class='plustxt bold']")
    public WebElement betSlipTag;

    @FindBy(css = "[class='statement']>tbody>tr>td>a")
    public List<WebElement> statementActionInfo;

    @FindBy(css = "[class='content-wrapper']")
    public WebElement betSlipContent;

    @FindBy(css = "[id='Bp-Leg-Bonus-Info']")
    public WebElement bonusInfo;

    @FindBy(css = ".component-boosted>div>.events-container>.events-sub-container>div>div>.event-bet")
    public List<WebElement> pawaBoostCategory;

    @FindBy(css = "[category='2']>.block>.events-container>div>div>div>.event-bet")
    public List<WebElement> footballCategory;

    @FindBy(css = "#single-bs-bonus")
    public WebElement multiBetBonus;

    @FindBy(css = ".bp-bet-chosen")
    public List<WebElement> betsAmount;

    @FindBy(css = ".table.midwide-content>div.row-bottomline-link")
    public List<WebElement> betInfoList;

    @FindBy(css = "a[id='withdraw-menu-top']")
    public WebElement withdrawButton;

    @FindBy(css = "a[id=withdraw-to-voucher]")
    public WebElement withdrawToVoucher;

    @FindBy(css = "[id=Input-Amount]")
    public WebElement withdrawAmount;

    @FindBy(css = "[value='Create voucher']")
    public WebElement createVoucher;

    @FindBy(css = ".notify.success")
    public WebElement notifySuccess;

    @FindBy(css = ".row-bottomline>div:nth-child(2)>div>span")
    public List<WebElement> vouchersList;

    @FindBy(css = "#input_voucher")
    public WebElement voucherActivationInput;

    @FindBy(css = "#activateVoucher")
    public WebElement activateVoucherButton;

    @FindBy(css = ".item-yellow")
    public WebElement voucherAmountInfoCell;

    @FindBy(css = ".bp-dropdown.bp-dropdown-active>div>ul>li:nth-child(8)")
    public WebElement logoutButton;
}
