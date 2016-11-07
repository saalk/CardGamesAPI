Feature: SubmitAndReset.feature. Executes the reset of change repayment

  @stub @live
  Scenario: customer submits but then resets the request, and submits again
    Given an active session for Partikulier
    When POST to / with body:
    """
      {
          "cardId": "6789"
      }
   """
    Then we set the retrieved requestId to be used in other calls
    When POST to /(requestId)/submit with no body
    Then the HTTP status code should be 200
    When POST to /(requestId)/reset with no body
    Then the HTTP status code should be 200
    When POST to /(requestId)/submit with no body
    Then the HTTP status code should be 200

    Then json should be like:
    """
      {
        "requestId": "String value",
       	"cramId": "String value"
      }
    """

  @confidence
  Scenario: customer submits but then resets the request, and submits again
    Given an active session for xenwmtd4
    When POST to / with body:
    """
      {
          "cardId": "9521"
      }
   """
    Then we set the retrieved requestId to be used in other calls
    When POST to /(requestId)/submit with no body
    Then the HTTP status code should be 200
    When POST to /(requestId)/reset with no body
    Then the HTTP status code should be 200
    When POST to /(requestId)/submit with no body
    Then the HTTP status code should be 200
    Then json should be like:
    """
      {
        "requestId": "String value",
       	"cramId": "String value"
    }
    """

  @stub @live
  Scenario: customer selects a card but then resets the request, and finally submits
    Given an active session for Partikulier
    When POST to / with body:
    """
      {
          "cardId": "6789"
      }
   """
    Then we set the retrieved requestId to be used in other calls
    When POST to /(requestId)/reset with no body
    Then the HTTP status code should be 200
    When POST to / with body:
    """
      {
          "cardId": "6789"
      }
   """
    Then we set the retrieved requestId to be used in other calls
    When POST to /(requestId)/submit with no body
    Then the HTTP status code should be 200
    Then json should be like:
    """
      {
        "requestId": "String value",
       	"cramId": "String value"
      }
    """

  @stub @live
  Scenario: customer selects a card, submits and resets twice
    Given an active session for Partikulier
    When POST to / with body:
    """
      {
          "cardId": "6789"
      }
   """
    Then we set the retrieved requestId to be used in other calls
    When POST to /(requestId)/submit with no body
    Then the HTTP status code should be 200
    When POST to /(requestId)/reset with no body
    Then the HTTP status code should be 200
    When POST to /(requestId)/reset with no body
    Then the HTTP status code should be 200
