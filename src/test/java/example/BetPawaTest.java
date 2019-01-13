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
import pageObjects.*;

import static org.junit.Assert.*;

public class BetPawaTest extends DriverFactory implements DriverActions {

    static double initialBalance;
    static double newBalance;
    static int stakeAmount;
    static String voucherData;
    static int voucherAmount;
    static final String WITHDRAW_ERROR = "You cannot withdraw more money than you have on your account.";
    static final String INVALID_AMOUNT_ERROR = "Invalid amount: please select correct amount.";
    static final String INVALID_CURRENCY_ERROR = "The voucher is in UGX. You are only allowed to deposit vouchers in KES.";

    @Inject
    private BasePage basePage;

    @Inject
    private BetsPage betsPage;

    @Inject
    private StatementPage statementPage;

    @Inject
    private VoucherPage voucherPage;

    @Inject
    private WithdrawPage withdrawPage;

    @Before("@start")
    public void setUp() {
        basePage = new BasePage();
        betsPage = new BetsPage();
        statementPage = new StatementPage();
        voucherPage = new VoucherPage();
        withdrawPage = new WithdrawPage();
    }

    @Given("^(.*) user with (.*)$")
    public void user(String userName, String balanceName) throws InterruptedException {
        switch (userName) {
            case "Uganda":
                openUrl("http://ug.test.verekuu.com/");
                click(basePage.loginLink);
                if (balanceName.equalsIgnoreCase("large balance")) {
                    basePage.phoneNumberInput.sendKeys("788899001");
                } else if (balanceName.equalsIgnoreCase("small balance")) {
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
        Thread.sleep(3000);
    }

    @When("^place a bet of (.*) (.*) (?:time|times)$")
    public void placeABet(String amountToBet, String amountOfBets) {
        clickAny(basePage.pawaBoostCategory, Integer.parseInt(amountOfBets));
        if (basePage.betsAmount.size() < Integer.parseInt(amountOfBets)) {
            clickAny(basePage.footballCategory, Integer.parseInt(amountOfBets));
        }
        initialBalance = Math.round(Double.parseDouble(basePage.balanceInfo.getText()
                .replaceAll("[^0-9]", ""))) / 100.0;
        if (amountToBet.equalsIgnoreCase("all")) {
            stakeAmount = (int) Math.round(initialBalance - 1);
            basePage.stakeInput.sendKeys(String.valueOf(stakeAmount));
        } else {
            stakeAmount = Integer.parseInt(amountToBet);
            assertTrue(!basePage.placeBetButton.isEnabled());
            basePage.stakeInput.sendKeys(String.valueOf(stakeAmount));
        }
        if (basePage.betsAmount.size() > 1) {
            assertTrue(basePage.bonusInfo.isDisplayed());
        }
    }

    @Then("^bet successfully placed$")
    public void betSuccessfullyPlaced() throws InterruptedException {
        assertTrue(basePage.placeBetButton.isEnabled());
        click(basePage.placeBetButton);
        Thread.sleep(3000);
    }

    @And("^balance is reduced by bet stake amount$")
    public void balanceIsReducedByBetStakeAmount() {
        newBalance = initialBalance - stakeAmount;
        fluentWait().until(ExpectedConditions.visibilityOf(basePage.balanceInfo));
        assertEquals(newBalance, Math.round(Double.parseDouble(basePage.balanceInfo.getText()
                .replaceAll("[^0-9]", ""))) / 100.0, 0.00);

    }

    @And("^info about (.*) (.*) displayed on statement$")
    public void infoAboutActionDetailsDisplayedOnStatement(String actionDetails, String statementStatus) throws InterruptedException {
        click(basePage.mainMenuButton);
        switch (actionDetails) {
            case "placed bet":
                click(basePage.betInfo);
                fluentWait().until(ExpectedConditions.visibilityOfAllElements(betsPage.betInfoList));
                click(betsPage.betInfoList.get(0));
                fluentWait().until(ExpectedConditions.visibilityOf(betsPage.betSlipContent));
                String slipInfo = betsPage.betSlipTag.getText();
                click(betsPage.mainMenuButton);
                click(betsPage.statementInfo);
                fluentWait().until(ExpectedConditions.visibilityOfAllElements(statementPage.statementBetInfo));
                assertTrue(statementPage.statementBetInfo.get(0)
                        .getText().equalsIgnoreCase("bet " + slipInfo + " placed"));
                break;
            case "created voucher":
                driver().navigate().refresh();
                click(basePage.mainMenuButton);
                click(basePage.statementInfo);
                fluentWait().until(ExpectedConditions.visibilityOfAllElements(statementPage.statementVoucherInfo));
                assertTrue(statementPage.statementVoucherInfo.get(0)
                        .getText().contains("created"));
                break;
            case "voucher deposit":
                driver().navigate().refresh();
                click(basePage.mainMenuButton);
                click(basePage.statementInfo);
                fluentWait().until(ExpectedConditions.visibilityOfAllElements(statementPage.statementVoucherInfo));
                assertTrue(statementPage.statementVoucherInfo.get(0)
                        .getText().contains("redeemed"));
                if (statementStatus.equalsIgnoreCase("is not")) {
                    assertFalse(statementPage.statementVoucherInfo.get(0)
                            .getText().contains("redeemed"));
                }
                break;
        }
    }

    @And("^bonus is calculated$")
    public void bonusIsCalculated() {
        scrollTo(basePage.betInfoLink);
        click(basePage.betInfoLink);
        fluentWait().until(ExpectedConditions.visibilityOf(betsPage.multiBetBonus));
        assertTrue(betsPage.multiBetBonus.isDisplayed());
    }

    @And("^user withdraws (?:^$|(.*)) to (.*)$")
    public void userPerformsActionToSomething(String withdrawAmount, String withdrawAction) throws InterruptedException {
        switch (withdrawAction) {
            case "voucher":
                click(basePage.mainMenuButton);
                click(basePage.withdrawButton);
                click(basePage.withdrawToVoucher);
                if (withdrawAmount.equalsIgnoreCase("nothing")) {
                    voucherAmount = 0;
                    click(withdrawPage.createVoucher);
                } else {
                    voucherAmount = Integer.parseInt(withdrawAmount);
                    input(withdrawPage.withdrawAmount, voucherAmount);
                    initialBalance = Math.round(Double.parseDouble(basePage.balanceInfo.getText()
                            .replaceAll("[^0-9]", ""))) / 100.0;
                    click(withdrawPage.createVoucher);
                }
                Thread.sleep(3000);
                break;
        }
    }

    @Then("^(.*) is generated$")
    public void somethingIsGenerated(String entityName) throws InterruptedException {
        Thread.sleep(3000);
        voucherData = withdrawPage.vouchersList.get(0).getText();

        switch (entityName) {
            case "Uganda voucher":
                click(basePage.mainMenuButton);
                click(basePage.logoutButton);
                click(basePage.loginLink);
                basePage.phoneNumberInput.clear();
                basePage.phoneNumberInput.sendKeys("788899002");
                basePage.passwordInput.sendKeys("123456");
                click(basePage.loginButton);
                Thread.sleep(3000);
                driver().navigate().to("http://ug.test.verekuu.com/voucher");
                initialBalance = Math.round(Double.parseDouble(basePage.balanceInfo.getText()
                        .replaceAll("[^0-9]", ""))) / 100.0;
                input(voucherPage.voucherActivationInput, voucherData);
                click(voucherPage.activateVoucherButton);
                Thread.sleep(3000);
                assertTrue(basePage.notifySuccess.isDisplayed());
                assertEquals(voucherPage.voucherAmountInfoCell.getText(), String.valueOf(voucherAmount));
                break;
            case "Kenya voucher":
                click(basePage.mainMenuButton);
                click(basePage.logoutButton);
                driver().navigate().to("http://ke.test.verekuu.com/");
                click(basePage.loginLink);
                basePage.phoneNumberInput.sendKeys("728899021");
                basePage.passwordInput.sendKeys("123456");
                click(basePage.loginButton);
                Thread.sleep(3000);
                driver().navigate().to("http://ke.test.verekuu.com/voucher");
                initialBalance = Math.round(Double.parseDouble(basePage.balanceInfo.getText()
                        .replaceAll("[^0-9]", ""))) / 100.0;
                input(voucherPage.voucherActivationInput, voucherData);
                click(voucherPage.activateVoucherButton);
                Thread.sleep(3000);
        }
    }

    @And("^voucher number is displayed$")
    public void voucherNumberIsDisplayed() throws InterruptedException {
        click(basePage.mainMenuButton);
        click(basePage.logoutButton);
        click(basePage.loginLink);
        basePage.phoneNumberInput.clear();
        basePage.phoneNumberInput.sendKeys("788899001");
        basePage.passwordInput.sendKeys("123456");
        click(basePage.loginButton);
        Thread.sleep(3000);
        click(basePage.mainMenuButton);
        click(basePage.withdrawButton);
        click(basePage.withdrawToVoucher);
        fluentWait().until(ExpectedConditions.visibilityOfAllElements(withdrawPage.vouchersList));
        assertTrue(withdrawPage.vouchersList.get(0).getText().contains(voucherData));
        initialBalance = Math.round(Double.parseDouble(basePage.balanceInfo.getText()
                .replaceAll("[^0-9]", ""))) / 100.0;
    }

    @And("^money are (.*) account$")
    public void moneyAreOutOfAccount(String moneyTransfer) {
        switch (moneyTransfer) {
            case "out of":
                newBalance = initialBalance;
                assertEquals(newBalance, Math.round(Double.parseDouble(basePage.balanceInfo.getText()
                        .replaceAll("[^0-9]", ""))) / 100.0, 0.00);
                break;

            case "into":
                newBalance = initialBalance + voucherAmount;
                assertEquals(newBalance, Math.round(Double.parseDouble(basePage.balanceInfo.getText()
                        .replaceAll("[^0-9]", ""))) / 100.0, 0.00);
                break;
        }
    }

    @Then("^​(.*) error appears$")
    public void someErrorAppears(String errorMessage) {
        if (basePage.balanceInfo.getText().contains("UGX")
                && initialBalance < voucherAmount) {
            assertTrue(withdrawPage.notifyError.isDisplayed());
            errorMessage = WITHDRAW_ERROR;
            assertEquals(errorMessage, withdrawPage.notifyError.getText());
        } else if (basePage.balanceInfo.getText().contains("UGX")
                && initialBalance > voucherAmount) {
            assertTrue(withdrawPage.notifyError.isDisplayed());
            errorMessage = INVALID_AMOUNT_ERROR;
            assertEquals(errorMessage, withdrawPage.notifyError.getText());
        } else if (basePage.balanceInfo.getText().contains("Ksh")) {
            assertTrue(voucherPage.notifyError.isDisplayed());
            errorMessage = INVALID_CURRENCY_ERROR;
            assertEquals(errorMessage, voucherPage.notifyError.getText());
        }
    }

    @After("@end")
    public void tearDown() {
        quit();
    }
}
