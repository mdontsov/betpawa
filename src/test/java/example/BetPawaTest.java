package example;

import com.google.inject.Inject;
import cucumber.annotation.After;
import cucumber.annotation.Before;
import cucumber.annotation.en.And;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import driverFactory.DriverActions;
import driverFactory.DriverFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.BasePage;
import pageObjects.UgandaPage;

import static org.junit.Assert.*;

public class BetPawaTest extends DriverFactory implements DriverActions {

    static double initialBalance;
    static double newBalance;
    static int stakeAmount;

    @Inject
    private UgandaPage ugandaPage;

    @Inject
    private BasePage basePage;

    @Before("@start")
    public void setUp() {
        basePage = new BasePage();
    }

    @Given("^(.*) page is opened$")
    public void pageIsOpened(String locationName) {
        switch (locationName) {
            case "Uganda":
                openUrl("http://ug.test.verekuu.com/");
                break;
            case "Kenya":
                openUrl("http://ke.test.verekuu.com/");
                break;
        }
    }

    @Given("^(.*) user with (.*)$")
    public void user(String userName, String balanceName) {
        click(basePage.loginLink);
        switch (userName) {
            case "Uganda":
                if (balanceName.equalsIgnoreCase("balance")) {
                    basePage.phoneNumberInput.sendKeys("788899001");
                } else if (balanceName.equalsIgnoreCase("no balance")) {
                    basePage.phoneNumberInput.sendKeys("788899002");
                } else {
                    throw new IllegalArgumentException();
                }
                basePage.passwordInput.sendKeys("123456");
                break;
            case "Kenya":
                basePage.phoneNumberInput.sendKeys("254728899021");
                basePage.passwordInput.sendKeys("123456");
                break;
            default:
                displayAlert("Unknown user!");
        }
        click(basePage.loginButton);
    }

    @When("^place a bet of (\\d+)$")
    public void placeABet(int amountToBet) {
        clickAny(basePage.betOptions);
        assertTrue(!basePage.placeBetButton.isEnabled());
        initialBalance = Math.round(Double.parseDouble(basePage.balanceInfo.getText()
                .replaceAll("[^0-9]", ""))) / 100.0;
        stakeAmount = amountToBet;
        basePage.stakeInput.sendKeys(String.valueOf(stakeAmount));
    }

    @Then("^bet successfully placed$")
    public void betSuccessfullyPlaced() {
        assertTrue(basePage.placeBetButton.isEnabled());
        click(basePage.placeBetButton);
        fluentWait().until(ExpectedConditions.visibilityOf(basePage.successNotification));
    }

    @And("^balance is reduced by bet stake amount$")
    public void balanceIsReducedByBetStakeAmount() {
        newBalance = initialBalance - stakeAmount;
        assertEquals(newBalance, Math.round(Double.parseDouble(basePage.balanceInfo.getText()
                .replaceAll("[^0-9]", ""))) / 100.0, 0.00);

    }

    @And("^info about placed bet is displayed on statement$")
    public void infoAboutPlacedBetIsDisplayedOnStatement() {
        click(basePage.betInfoLink);
        fluentWait().until(ExpectedConditions.visibilityOf(basePage.betSlipContent));
        String slipInfo = basePage.betSlipTag.getText();
        click(basePage.mainMenuButton);
        click(basePage.statementInfo);
        fluentWait().until(ExpectedConditions.visibilityOfAllElements(basePage.statementActionInfo));
    }

    @After("@end")
    public void tearDown() {
//        quit();
    }
}
