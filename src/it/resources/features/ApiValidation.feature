Feature: ApiValidation.feature. To test the Validations
  In order to initiate of the request for change-repayment
  I should call the api of /api/consumer-credit-cards/requests/change-repayment/off

@stub @live
Scenario: customer is selecting an invalid cardId
Given an active session for Partikulier
When POST to / with body:
"""
      {
          "cardId": "INVALID"
      }
   """
Then the HTTP status code should be 500
And JSON error should be 532.500.0.400

@stub @live
Scenario: customer is selecting a null card
Given an active session for Partikulier
When POST to / with body:
"""
      {
          ""
      }
   """
Then the HTTP status code should be 500
And JSON error should be 0.500.0.0

@stub @live
Scenario: customer is selecting an empty card
Given an active session for Partikulier
When POST to / with body:
"""
      {
          "cardId": ""
      }
   """
Then the HTTP status code should be 500
And JSON error should be 532.500.0.400
