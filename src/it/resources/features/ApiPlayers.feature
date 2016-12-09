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
      | latest | ELF    | Cukes Doe | true  | HUMAN   | 0      | 0           | 201              |

  @Api @Players
  Scenario Outline: A frontend makes call to GET /api/players
    Given I try to get a player with valid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain human "<human>" player having "<avatar>" and "<alias>" and "<aiLevel>"

    Examples: This is the default Human Player

      | id      | avatar | alias     | human | aiLevel | cubits | securedLoan | HTTP status code |
      | latest  | ELF    | Cukes Doe | true  | HUMAN   | 0      | 0           | 200              |


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
    Given I try to delete a player with "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default Human Player

      | id     | avatar   | alias      | human | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | MAGICIAN | Cukes Doe2 | false | HUMAN   | 0      | 0           | 204              |
