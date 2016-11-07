Feature: KeepAlive.feature. To know whether the api is live we send a keep alive message

  @stub @confidence @live
  Scenario: send a keep alive request
    Given an active session for customer_vet
    When GET /keepalive
    Then the HTTP status code should be 200
    Then response should be like:
    """
    KEEPALIVE_OK
    """