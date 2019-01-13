package pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static driverFactory.DriverFactory.driver;

public class BetsPage {

    public BetsPage() {
        PageFactory.initElements(driver(), this);
    }

    @FindBy(css = ".table.midwide-content>div.row-bottomline-link")
    public List<WebElement> betInfoList;

    @FindBy(css = "[class='content-wrapper']")
    public WebElement betSlipContent;

    @FindBy(css = "[class='plustxt bold']")
    public WebElement betSlipTag;

    @FindBy(css = "#Main-Menu-Button")
    public WebElement mainMenuButton;

    @FindBy(css = "[id='statement-menu-top']")
    public WebElement statementInfo;

    @FindBy(css = "#single-bs-bonus")
    public WebElement multiBetBonus;
}
