package pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static driverFactory.DriverFactory.driver;

public class VoucherPage {

    public VoucherPage() {
        PageFactory.initElements(driver(), this);
    }

    @FindBy(css = "#input_voucher")
    public WebElement voucherActivationInput;

    @FindBy(css = "#activateVoucher")
    public WebElement activateVoucherButton;

    @FindBy(css = ".item-yellow")
    public WebElement voucherAmountInfoCell;

    @FindBy(css = ".notify.error")
    public WebElement notifyError;
}
