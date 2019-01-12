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
import pageObjects.BetPawaPage;

import static org.junit.Assert.*;

public class BetPawaTest extends DriverFactory implements DriverActions {

    static double initialBalance;
    static double newBalance;
    static int stakeAmount;
    static String voucherData;
    static int voucherAmount;

    @Inject
    private BetPawaPage betPawaPage;

    @Before("@start")
    public void setUp() {
        betPawaPage = new BetPawaPage();
    }

    @Given("^(.*) user with (.*)$")
    public void user(String userName, String balanceName) throws InterruptedException {
        switch (userName) {
            case "Uganda":
                openUrl("http://ug.test.verekuu.com/");
                click(betPawaPage.loginLink);
                if (balanceName.equalsIgnoreCase("balance")) {
                    betPawaPage.phoneNumberInput.sendKeys("788899001");
                } else if (balanceName.equalsIgnoreCase("no balance")) {
                    betPawaPage.phoneNumberInput.sendKeys("788899002");
                } else {
                    throw new IllegalArgumentException();
                }
                betPawaPage.passwordInput.sendKeys("123456");
                break;
            case "Kenya":
                openUrl("http://ke.test.verekuu.com/");
                click(betPawaPage.loginLink);
                betPawaPage.phoneNumberInput.sendKeys("254728899021");
                betPawaPage.passwordInput.sendKeys("123456");
                break;
            default:
                displayAlert("Unknown user!");
        }
        click(betPawaPage.loginButton);
        Thread.sleep(3000);
    }

    @When("^place a bet of (\\d+) (\\d+) (?:time|times)$")
    public void placeABet(int amountToBet, int amountOfBets) {
        clickAny(betPawaPage.pawaBoostCategory, amountOfBets);
        if (betPawaPage.betsAmount.size() < amountOfBets) {
            clickAny(betPawaPage.footballCategory, amountOfBets);
        }
        stakeAmount = amountToBet;
        assertTrue(!betPawaPage.placeBetButton.isEnabled());
        initialBalance = Math.round(Double.parseDouble(betPawaPage.balanceInfo.getText()
                .replaceAll("[^0-9]", ""))) / 100.0;
        betPawaPage.stakeInput.sendKeys(String.valueOf(stakeAmount));
        if (betPawaPage.betsAmount.size() > 1) {
            assertTrue(betPawaPage.bonusInfo.isDisplayed());
        }
    }

    @Then("^bet successfully placed$")
    public void betSuccessfullyPlaced() {
        assertTrue(betPawaPage.placeBetButton.isEnabled());
        click(betPawaPage.placeBetButton);
        fluentWait().until(ExpectedConditions.visibilityOf(betPawaPage.successNotification));
    }

    @And("^balance is reduced by bet stake amount$")
    public void balanceIsReducedByBetStakeAmount() {
        newBalance = initialBalance - stakeAmount;
        fluentWait().until(ExpectedConditions.visibilityOf(betPawaPage.balanceInfo));
        assertEquals(newBalance, Math.round(Double.parseDouble(betPawaPage.balanceInfo.getText()
                .replaceAll("[^0-9]", ""))) / 100.0, 0.00);

    }

    @And("^info about (.*) is displayed on statement$")
    public void infoAboutPlacedBetIsDisplayedOnStatement(String actionDetails) throws InterruptedException {
        click(betPawaPage.mainMenuButton);
        switch (actionDetails) {
            case "placed bet":
                click(betPawaPage.betInfo);
                fluentWait().until(ExpectedConditions.visibilityOfAllElements(betPawaPage.betInfoList));
                click(betPawaPage.betInfoList.get(0));
                fluentWait().until(ExpectedConditions.visibilityOf(betPawaPage.betSlipContent));
                String slipInfo = betPawaPage.betSlipTag.getText();
                click(betPawaPage.mainMenuButton);
                click(betPawaPage.statementInfo);
                fluentWait().until(ExpectedConditions.visibilityOfAllElements(betPawaPage.statementBetInfo));
                assertTrue(betPawaPage.statementBetInfo.get(0)
                        .getText().equalsIgnoreCase("bet " + slipInfo + " placed"));
                break;
            case "created voucher":
                driver().navigate().refresh();
                click(betPawaPage.mainMenuButton);
                click(betPawaPage.statementInfo);
                fluentWait().until(ExpectedConditions.visibilityOfAllElements(betPawaPage.statementVoucherInfo));
                assertTrue(betPawaPage.statementVoucherInfo.get(0)
                        .getText().contains("voucher created"));
        }

    }

    @And("^bonus is calculated$")
    public void bonusIsCalculated() {
        scrollTo(betPawaPage.betInfoLink);
        click(betPawaPage.betInfoLink);
        fluentWait().until(ExpectedConditions.visibilityOf(betPawaPage.multiBetBonus));
        assertTrue(betPawaPage.multiBetBonus.isDisplayed());
    }

    @And("^user withdraws (\\d+) to (.*)$")
    public void userPerformsWithdrawToVoucher(int withdrawAmount, String withdrawAction) throws InterruptedException {
        switch (withdrawAction) {
            case "voucher":
                click(betPawaPage.mainMenuButton);
                click(betPawaPage.withdrawButton);
                click(betPawaPage.withdrawToVoucher);
                input(betPawaPage.withdrawAmount, withdrawAmount);
                initialBalance = Math.round(Double.parseDouble(betPawaPage.balanceInfo.getText()
                        .replaceAll("[^0-9]", ""))) / 100.0;
                voucherAmount = withdrawAmount;
                click(betPawaPage.createVoucher);
                fluentWait().until(ExpectedConditions.visibilityOf(betPawaPage.notifySuccess));
                click(betPawaPage.withdrawToVoucher);
                Thread.sleep(3000);
                voucherData = betPawaPage.vouchersList.get(0).getText();
                break;
        }
    }

    @Then("^(.*) is generated$")
    public void voucherIsGenerated(String entityName) throws InterruptedException {
        click(betPawaPage.mainMenuButton);
        click(betPawaPage.logoutButton);
        click(betPawaPage.loginLink);
        betPawaPage.phoneNumberInput.clear();
        betPawaPage.phoneNumberInput.sendKeys("788899002");
        betPawaPage.passwordInput.sendKeys("123456");
        click(betPawaPage.loginButton);
        Thread.sleep(3000);

        switch (entityName) {
            case "voucher":
                driver().navigate().to("http://ug.test.verekuu.com/voucher");
                input(betPawaPage.voucherActivationInput, voucherData);
                click(betPawaPage.activateVoucherButton);
                assertTrue(betPawaPage.notifySuccess.isDisplayed());
                assertTrue(betPawaPage.voucherAmountInfoCell.getText().contains(String.valueOf(voucherAmount)));
        }
    }

    @And("^voucher number is displayed$")
    public void voucherNumberIsDisplayed() throws InterruptedException {
        click(betPawaPage.mainMenuButton);
        click(betPawaPage.logoutButton);
        click(betPawaPage.loginLink);
        betPawaPage.phoneNumberInput.clear();
        betPawaPage.phoneNumberInput.sendKeys("788899001");
        betPawaPage.passwordInput.sendKeys("123456");
        click(betPawaPage.loginButton);
        Thread.sleep(3000);
        click(betPawaPage.mainMenuButton);
        click(betPawaPage.withdrawButton);
        click(betPawaPage.withdrawToVoucher);
        fluentWait().until(ExpectedConditions.visibilityOfAllElements(betPawaPage.vouchersList));
        assertTrue(betPawaPage.vouchersList.get(0).getText().contains(voucherData));
    }

    @And("^money are out of account$")
    public void moneyAreOutOfAccount() {
        newBalance = initialBalance - voucherAmount;
        assertEquals(newBalance, Math.round(Double.parseDouble(betPawaPage.balanceInfo.getText()
                .replaceAll("[^0-9]", ""))) / 100.0, 0.00);
    }


    @After("@end")
    public void tearDown() {
//        quit();
    }

}
