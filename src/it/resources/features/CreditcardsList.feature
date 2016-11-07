Feature: CreditcardsList.feature. To execute the listing all cards
  In order to execute the listing of all cards
  I should call the api of /api/consumer-credit-cards/

  @stub @live
  Scenario: list cards for a customer
    Given an active session for 14_boerrigter
    When GET /
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
        },
        {
          "name": "J A M VET",
          "maskedCreditcardNumber": "5248.****.****.1185",
          "accountNumber": "0747780978",
          "type": "Secondary",
          "productType": "Platinumcard",
          "ibanNumber":"NL86 INGB 0747 7809 78",
          "startDate": "2008-11-13",
          "status": "Active",
          "assignedCreditLimit":"2500",
          "balanceAmount":"0",
          "linkedMainCardId": "3009",
          "cardId": "1185"
        }
      ]
    """

  @confidence
  Scenario: list cards for a Kruijt
    Given an active session for xenwmtd4
    When GET /
    Then the HTTP status code should be 200
    Then json should be like:
    """
      [
        {
            "name": "J JANSEN",
            "maskedCreditcardNumber": "5248.****.****.9521",
            "accountNumber": "0003551205",
            "type": "Primary",
            "productType": "Creditcard",
            "ibanNumber": "NL80 INGB 0003 5512 05",
            "startDate": "2015-12-05",
            "status": "Active",
            "assignedCreditLimit": "2500",
            "balanceAmount": "0",
            "cardId": "9521"
        },
        {
            "name": "J JANSEN",
            "maskedCreditcardNumber": "5248.****.****.2076",
            "accountNumber": "0003551205",
            "type": "Secondary",
            "productType": "Creditcard",
            "ibanNumber": "NL80 INGB 0003 5512 05",
            "startDate": "2015-12-05",
            "status": "Active",
            "assignedCreditLimit": "2500",
            "balanceAmount": "0",
            "linkedMainCardId": "9521",
            "cardId": "2076"
        }
      ]
    """


  @stub @live
  Scenario: no cards for a customer
    Given an active session for 11_klantelf
    When GET /
    Then the HTTP status code should be 200
    Then json should be like:
    """
      []
    """

  @stub @live
  Scenario: lecca returns a service error
    Given an active session for XX_errorClient
    When GET /
    Then the HTTP status code should be 500
    And JSON error should be 274.500.0169.0
