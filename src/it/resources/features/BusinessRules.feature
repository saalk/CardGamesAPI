Feature: BusinessRules.feature. To assert the correct behaviour of business rules in change repayment
  Customer will not pass the kan dat when the business rules fail

  @live
  Scenario: student account rule
    Given an active session for BusinessRulesFailStudent
    When POST to / with body:
    """
      {
          "cardId": "5240"
      }
    """
    Then we set the retrieved requestId to be used in other calls
    When POST to /(requestId)/submit with no body
    Then the HTTP status code should be 200
    Then the errorCode should be 509

  @live
  Scenario: closed account rule
    Given an active session for BusinessRulesFailClosed
    When POST to / with body:
    """
      {
          "cardId": "5240"
      }
    """
    Then we set the retrieved requestId to be used in other calls
    When POST to /(requestId)/submit with no body
    Then the HTTP status code should be 200
    Then the errorCode should be 507

  @live
  Scenario: cancelled account rule
    Given an active session for BusinessRulesFailCancelled
    When POST to / with body:
    """
      {
          "cardId": "5240"
      }
    """
    Then we set the retrieved requestId to be used in other calls
    When POST to /(requestId)/submit with no body
    Then the HTTP status code should be 200
    Then the errorCode should be 508

  @live
  Scenario: blocked account rule
    Given an active session for BusinessRulesFailBlocked
    When POST to / with body:
    """
      {
          "cardId": "5240"
      }
    """
    Then we set the retrieved requestId to be used in other calls
    When POST to /(requestId)/submit with no body
    Then the HTTP status code should be 200
    Then the errorCode should be 504

  @live
  Scenario: pending requests rule
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
    When POST to / with body:
    """
      {
          "cardId": "6789"
      }
    """
    Then we set the retrieved requestId to be used in other calls
    When POST to /(requestId)/submit with no body
    Then the HTTP status code should be 200
#    Commented because this BR is disabled in local
#    Then the errorCode should be 3
