Feature: Select.feature. To execute the select for change repayment
  In order to initiate of the request for change repayment
  I should call the api of /api/consumer-credit-cards/requests/change-repayment/off/

  @live
  Scenario: customer is selecting with his own card
    Given an active session for customer_vet
    When POST to / with body:
    """
      {
          "cardId": "6789"
      }
   """
    Then the HTTP status code should be 200
    Then json should be like:
    """
      {
        "requestId": "String value",
        "creditCard": {
            "name": "String value",
            "maskedCreditcardNumber": "String value",
            "accountNumber": "String value",
            "type": "String value",
            "productType": "String value",
            "ibanNumber": "String value",
            "startDate": "String value",
            "status": "String value",
            "currentAccount": "String value",
            "assignedCreditLimit": "String value",
		    "balanceAmount": "String value",
            "cardId": "String value"
        },
        "agreement": {
            "correspondenceAddress": {
              "addressLine1": "String value",
              "addressLine2": "String value"
            },
            "role": "String value"
        }
    }
    """

  @live
  Scenario: customer is selecting a card of which he is not the main holder
    Given an active session for 6_klantzes
    When POST to / with body:
    """
      {
          "cardId": "1186"
      }
   """
    Then the HTTP status code should be 200
    Then json should be like:
    """

      {
          "requestId":  "String value",
          "creditCard": {
            "name":"String value",
            "maskedCreditcardNumber":"String value",
            "accountNumber":"String value",
            "type":"String value",
            "productType":"String value",
            "ibanNumber":"String value",
            "startDate":"String value",
            "status":"String value",
            "currentAccount":"String value",
            "assignedCreditLimit": "String value",
		    "balanceAmount": "String value",
            "cardId":"String value"
       },
       "agreement": {
            "correspondenceAddress": {
              "addressLine1": "String value",
              "addressLine2": "String value"
            },
        "role": "String value"
       }
      }
    """

  @live
  Scenario: customer is selecting with his own card
    Given an active session for xenwmtd4
    When POST to / with body:
    """
      {
          "cardId": "9521"
      }
   """
    Then the HTTP status code should be 200
    Then json should be like:
    """
      {
        "requestId": "String value",
        "creditCard": {
            "name": "String value",
            "maskedCreditcardNumber": "String value",
            "accountNumber": "String value",
            "type": "String value",
            "productType": "String value",
            "ibanNumber": "String value",
            "startDate": "String value",
            "status": "String value",
            "currentAccount": "String value",
            "assignedCreditLimit": "String value",
		    "balanceAmount": "String value",
            "cardId": "String value"
        },
        "agreement": {
            "correspondenceAddress": {
                "addressLine1": "String value",
                "addressLine2": "String value"
            },
            "role": "String value"
        }
      }
    """

  @live
  Scenario: customer is NOT selecting with his own card
    Given an active session for Partikulier
    When POST to / with body:
      """
        {
            "cardId": "3111"
        }
     """
    Then the HTTP status code should be 500
    And JSON error should be 532.500.0.400
