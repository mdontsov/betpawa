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

    @And("^info about (.*) is displayed on statement$")
    public void infoAboutPlacedBetIsDisplayedOnStatement(String actionDetails) throws InterruptedException {
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
                        .getText().contains("Voucher created"));
        }

    }

    @And("^bonus is calculated$")
    public void bonusIsCalculated() {
        scrollTo(basePage.betInfoLink);
        click(basePage.betInfoLink);
        fluentWait().until(ExpectedConditions.visibilityOf(betsPage.multiBetBonus));
        assertTrue(betsPage.multiBetBonus.isDisplayed());
    }

    @And("^user withdraws (\\d+) to (.*)$")
    public void userPerformsWithdrawToVoucher(int withdrawAmount, String withdrawAction) throws InterruptedException {
        switch (withdrawAction) {
            case "voucher":
                click(basePage.mainMenuButton);
                click(basePage.withdrawButton);
                click(basePage.withdrawToVoucher);
                input(withdrawPage.withdrawAmount, withdrawAmount);
                initialBalance = Math.round(Double.parseDouble(basePage.balanceInfo.getText()
                        .replaceAll("[^0-9]", ""))) / 100.0;
                voucherAmount = withdrawAmount;
                click(withdrawPage.createVoucher);
                fluentWait().until(ExpectedConditions.visibilityOf(withdrawPage.notifySuccess));
//                click(basePage.withdrawToVoucher);
                Thread.sleep(3000);
                voucherData = withdrawPage.vouchersList.get(0).getText();
                break;
        }
    }

    @Then("^(.*) is generated$")
    public void voucherIsGenerated(String entityName) throws InterruptedException {
        click(basePage.mainMenuButton);
        click(basePage.logoutButton);
        click(basePage.loginLink);
        basePage.phoneNumberInput.clear();
        basePage.phoneNumberInput.sendKeys("788899002");
        basePage.passwordInput.sendKeys("123456");
        click(basePage.loginButton);
        Thread.sleep(3000);

        switch (entityName) {
            case "voucher":
                driver().navigate().to("http://ug.test.verekuu.com/voucher");
                input(voucherPage.voucherActivationInput, voucherData);
                click(voucherPage.activateVoucherButton);
                assertTrue(basePage.notifySuccess.isDisplayed());
                assertTrue(voucherPage.voucherAmountInfoCell.getText().contains(String.valueOf(voucherAmount)));
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
    }

    @And("^money are out of account$")
    public void moneyAreOutOfAccount() {
        newBalance = initialBalance - voucherAmount;
        assertEquals(newBalance, Math.round(Double.parseDouble(basePage.balanceInfo.getText()
                .replaceAll("[^0-9]", ""))) / 100.0, 0.00);
    }


    @After("@end")
    public void tearDown() {
//        quit();
    }

}
