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
import pageObjects.UgandaPage;

public class BetPawaTest extends DriverFactory implements DriverActions {

    @Inject
    private UgandaPage ugandaPage;

    @Before("@start")
    public void setUp() {
        ugandaPage = new UgandaPage();
    }

    @Given("^page is opened$")
    public void pageIsOpened() {
        openUrl("http://ug.test.verekuu.com/");
    }

    @Given("^(.*) user$")
    public void user(String userName) {
        userName = "788899001";
    }

    @And("^enough money on balance$")
    public void enoughMoneyOnBalance() {
    }

    @When("^place a bet$")
    public void placeABet() {
    }

    @Then("^bet successfully placed$")
    public void betSuccessfullyPlaced() {
    }

    @And("^balance is reduced by bet stake amount$")
    public void balanceIsReducedByBetStakeAmount() {
    }

    @And("^info about placed bet is displayed on statement$")
    public void infoAboutPlacedBetIsDisplayedOnStatement() {
    }

    @After("@end")
    public void tearDown() {
        quit();
    }
}
