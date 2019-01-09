@start @end
  Feature: Different bet placement attempts

    Scenario: Successful bet placement
      Given Uganda user with balance
      When place a bet of 2 1 time
      Then bet successfully placed
      And balance is reduced by bet stake amount
      And info about placed bet is displayed on statement

    Scenario: Successful multi bet bonus placement
      Given Uganda user with balance
      When place a bet of 1 7 times
      Then bet successfully placed
      And bonus is calculated
      And balance is reduced by bet stake amount
      And info about placed bet is displayed on statement