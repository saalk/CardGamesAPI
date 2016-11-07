Feature: CreditcardsList.feature. To execute the listing filtered cards
  In order to execute the listing of filtered cards
  I should call the api of /api/consumer-credit-cards/

  @stub
  Scenario: list filtered cards for a customer
    Given an active session for multiple_repayments
    When GET /?repayment=charge
    Then the HTTP status code should be 200
    Then json should be like:
    """
      [
        {
          "name": "A C POL",
          "maskedCreditcardNumber": "5248.****.****.3009",
          "accountNumber": "0747780978",
          "type": "Primary",
          "productType": "Platinumcard",
          "ibanNumber":"NL86 INGB 0747 7809 78",
          "startDate": "2008-11-13",
          "status": "Closed",
          "assignedCreditLimit":"2500",
          "balanceAmount":"0",
          "cardId": "3009"
        }
      ]
    """
    Then json string should contain:
    """
      J A M Charge
    """

  @stub
  Scenario: list filtered cards for a customer
    Given an active session for multiple_repayments
    When GET /?repayment=revolving
    Then the HTTP status code should be 200
    Then json should be like:
    """
      [
        {
          "name": "A C POL",
          "maskedCreditcardNumber": "5248.****.****.3009",
          "accountNumber": "0747780978",
          "type": "Primary",
          "productType": "Platinumcard",
          "ibanNumber":"NL86 INGB 0747 7809 78",
          "startDate": "2008-11-13",
          "status": "Closed",
          "assignedCreditLimit":"2500",
          "balanceAmount":"0",
          "cardId": "3009"
        }
      ]
    """
    Then json string should contain:
    """
      J A M Revolving
    """