package pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static driverFactory.DriverFactory.driver;

public class WithdrawPage {

    public WithdrawPage() {
        PageFactory.initElements(driver(), this);
    }

    @FindBy(css = "[id=Input-Amount]")
    public WebElement withdrawAmount;

    @FindBy(css = "[value='Create voucher']")
    public WebElement createVoucher;

    @FindBy(css = ".notify.success")
    public WebElement notifySuccess;

    @FindBy(css = ".row-bottomline>div:nth-child(2)>div>span")
    public List<WebElement> vouchersList;
}
