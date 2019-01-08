@start @end
  Feature: Different bet placement attempts

    Scenario: Successful bet placement
      Given Uganda page is opened
      Given Uganda user with balance
      When place a bet of 10
      Then bet successfully placed
      And balance is reduced by bet stake amount
      And info about placed bet is displayed on statement