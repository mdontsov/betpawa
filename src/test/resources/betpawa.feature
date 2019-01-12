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

    Scenario: Successful withdrawal to voucher
      Given Uganda user with balance
      And user withdraws 50 to voucher
      Then voucher is generated
      And voucher number is displayed
      And money are out of account
      And info about created voucher is displayed on statement
