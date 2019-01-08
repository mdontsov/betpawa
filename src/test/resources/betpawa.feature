@start @end
  Feature: Different bet placement attempts

    Background:
      Given page is opened

    Scenario: Successful bet placement
      Given Uganda user
      And enough money on balance
      When place a bet
      Then bet successfully placed
      And balance is reduced by bet stake amount
      And info about placed bet is displayed on statement