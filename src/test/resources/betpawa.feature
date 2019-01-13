@start @end
  Feature: Different bet placement attempts

    Scenario: Successful bet placement
      Given Uganda user with large balance
      When place a bet of 2 1 time
      Then bet successfully placed
      And balance is reduced by bet stake amount
      And info about placed bet is displayed on statement

    Scenario: Successful multi bet bonus placement
      Given Uganda user with large balance
      When place a bet of 1 7 times
      Then bet successfully placed
      And bonus is calculated
      And balance is reduced by bet stake amount
      And info about placed bet is displayed on statement

    Scenario: Successful withdrawal to voucher
      Given Uganda user with large balance
      And user withdraws 50 to voucher
      Then Uganda voucher is generated
      And voucher number is displayed
      And money are out of account
      And info about created voucher is displayed on statement
      
    Scenario: Failed withdrawal to voucher in case of insufficient balance
      Given Uganda user with small balance
      When place a bet of all 1 time
      Then bet successfully placed
      And user withdraws 10 to voucher
      Then ​You cannot withdraw more money than you have on your account. error appears
      
    Scenario: Failed withdrawal in case of invalid amount
      Given Uganda user with small balance
      And user withdraws nothing to voucher
      Then ​​Invalid amount: please select correct amount. error appears

    Scenario: Successful voucher redemption
      Given Uganda user with large balance
      Given user withdraws 20 to voucher
      Then Uganda voucher is generated
      Then money are into account
      And info about voucher deposit is displayed on statement

    Scenario: Failed voucher redemption in case of invalid currency
      Given Uganda user with large balance
      Given user withdraws 20 to voucher
      Then Kenya voucher is generated
      And ​The voucher is in UGX. You are only allowed to deposit vouchers in KES. error appears
