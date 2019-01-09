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

import static org.junit.Assert.*;

public class BetPawaTest extends DriverFactory implements DriverActions {

    static double initialBalance;
    static double newBalance;
    static int stakeAmount;

    @Inject
    private BasePage basePage;

    @Before("@start")
    public void setUp() {
        basePage = new BasePage();
    }

    @Given("^(.*) user with (.*)$")
    public void user(String userName, String balanceName) {
        switch (userName) {
            case "Uganda":
                openUrl("http://ug.test.verekuu.com/");
                click(basePage.loginLink);
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
                openUrl("http://ke.test.verekuu.com/");
                click(basePage.loginLink);
                basePage.phoneNumberInput.sendKeys("254728899021");
                basePage.passwordInput.sendKeys("123456");
                break;
            default:
                displayAlert("Unknown user!");
        }
        click(basePage.loginButton);
    }

    @When("^place a bet of (\\d+) (\\d+) (?:time|times)$")
    public void placeABet(int amountToBet, int amountOfBets) {
        clickAny(basePage.pawaBoostCategory, amountOfBets);
        if (basePage.betsAmount.size() < amountOfBets) {
            clickAny(basePage.footballCategory, amountOfBets);
        }
        stakeAmount = amountToBet;
        assertTrue(!basePage.placeBetButton.isEnabled());
        initialBalance = Math.round(Double.parseDouble(basePage.balanceInfo.getText()
                .replaceAll("[^0-9]", ""))) / 100.0;
        basePage.stakeInput.sendKeys(String.valueOf(stakeAmount));
        if (basePage.betsAmount.size() > 1) {
            assertTrue(basePage.bonusInfo.isDisplayed());
        }
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
        fluentWait().until(ExpectedConditions.visibilityOf(basePage.balanceInfo));
        assertEquals(newBalance, Math.round(Double.parseDouble(basePage.balanceInfo.getText()
                .replaceAll("[^0-9]", ""))) / 100.0, 0.00);

    }

    @And("^info about placed bet is displayed on statement$")
    public void infoAboutPlacedBetIsDisplayedOnStatement() {
        click(basePage.mainMenuButton);
        click(basePage.betInfo);
        fluentWait().until(ExpectedConditions.visibilityOfAllElements(basePage.betInfoList));
        click(basePage.betInfoList.get(0));
        fluentWait().until(ExpectedConditions.visibilityOf(basePage.betSlipContent));
        String slipInfo = basePage.betSlipTag.getText();
        click(basePage.mainMenuButton);
        click(basePage.statementInfo);
        fluentWait().until(ExpectedConditions.visibilityOfAllElements(basePage.statementActionInfo));
        assertTrue(basePage.statementActionInfo.get(0).getText().contains(slipInfo));
    }

    @After("@end")
    public void tearDown() {
//        quit();
    }

    @And("^bonus is calculated$")
    public void bonusIsCalculated() {
        scrollTo(basePage.betInfoLink);
        click(basePage.betInfoLink);
        fluentWait().until(ExpectedConditions.visibilityOf(basePage.multiBetBonus));
        assertTrue(basePage.multiBetBonus.isDisplayed());
    }
}
