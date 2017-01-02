Feature: Execute a lifecycle of a player in the card game
  In order to execute the lifecycle of a player
  I should call the api of /api/players/ to post, put, get and delete a player

  @Api @Players
  Scenario Outline: A frontend makes call to GET /api/players/{id}
    Given I try to get a player with invalid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should be like Player not found

    Examples: This is the default Human Player

      | id   | avatar | alias     | human | aiLevel | cubits | securedLoan | HTTP status code |
      | 1234 | ELF    | Cukes Doe | true  | HUMAN   | 0      | 0           | 404              |

  @Api @Players
  Scenario Outline: A frontend makes call to POST /api/players
    Given I try to post a human "<human>" player having "<avatar>" and "<alias>" and "<aiLevel>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain human "<human>" player having "<avatar>" and "<alias>" and "<aiLevel>"

    Examples: This is the default Human Player

      | id     | avatar | alias     | human | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | ROMAN  | Alien1 Doe | false | LOW     | 0      | 0           | 201              |
      | latest | GOBLIN | Alien2 Doe | false | MEDIUM  | 0      | 0           | 201              |
      | latest | ELF    | Cukes1 Doe | true  | HUMAN   | 0      | 0           | 201              |
      | latest | ELF    | Cukes2 Doe | true  | HUMAN   | 0      | 0           | 201              |

  @Api @Players
  Scenario Outline: A frontend makes call to GET /api/players/{id}
    Given I try to get a player with valid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain human "<human>" player having "<avatar>" and "<alias>" and "<aiLevel>"

    Examples: This is the default Human Player

      | id      | avatar | alias      | human | aiLevel | cubits | securedLoan | HTTP status code |
      | latest  | ELF    | Cukes2 Doe | true  | HUMAN   | 0      | 0           | 200              |

  @Api @Players
  Scenario Outline: A frontend makes call to GET /api/players?human={boolean}
    Given I try to get all human "<human>" players
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain at least "<count>" players

    Examples: This is the default Human Player

      | human | count | HTTP status code |
      | true  | 2     | 200              |
      | false | 2     | 200              |

  @Api @Players
  Scenario Outline: A frontend makes call to GET /api/players
    Given I try to get all players
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain at least "<count>" players

    Examples: This is the default Human Player

      | count | HTTP status code |
      | 4     | 200              |

  @Api @Players
  Scenario Outline: A frontend makes call to PUT /api/players/{id}
    Given I try to put a player with "<id>" having human "<human>" avatar "<avatar>" and alias "<alias>" and "<aiLevel>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain human "<human>" player having "<avatar>" and "<alias>" and "<aiLevel>"

    Examples: This is the default Human Player

      | id     | avatar   | alias      | human | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | MAGICIAN | Cukes Doe2 | false | HUMAN   | 0      | 0           | 200              |

  @Api @Players
  Scenario Outline: A frontend makes call to DELETE /api/players/{id}
    Given I try to delete a player "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default Human Player

      | id     | avatar   | alias      | human | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | MAGICIAN | Cukes Doe2 | false | HUMAN   | 0      | 0           | 204              |

  @Api @Players
  Scenario Outline: A frontend makes call to DELETE /api/players?id={id},{id}
    Given I try to delete all players with "<ids>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default Human Player

      | ids | HTTP status code |
      | all | 204              |