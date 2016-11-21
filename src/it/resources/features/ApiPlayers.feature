Feature: Execute a lifecycle of a player in the card game
  In order to execute the lifecycle of a player
  I should call the api of /api/players/ to post, put, get and delete a player

  @api
  Scenario Outline: A player makes call to GET /api/players/{id}
    Given I try to get a player with invalid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should be like [{}]

    Examples: This is the default Human Player

      | id   | avatar | alias     | isHuman | aiLevel | cubits | securedLoan | HTTP status code |
      | 1234 | ELF    | Cukes Doe | true    | HUMAN   | 0      | 0           | 404              |

  @api
  Scenario Outline: A player makes call to POST /api/players
    Given I try to post a isHuman "<isHuman>" player having "<avatar>" and "<alias>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain isHuman "<isHuman>" player having "<avatar>" and "<alias>"

    Examples: This is the default Human Player

      | id     | avatar | alias     | isHuman | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | ELF    | Cukes Doe | true    | HUMAN   | 0      | 0           | 201              |

  @api
  Scenario Outline: A player makes call to GET /api/players
    Given I try to get a player with valid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain isHuman "<isHuman>" player having "<avatar>" and "<alias>"

    Examples: This is the default Human Player

      | id      | avatar | alias     | isHuman | aiLevel | cubits | securedLoan | HTTP status code |
      | latest  | ELF    | Cukes Doe | true    | HUMAN   | 0      | 0           | 200              |


  @api
  Scenario Outline: A player makes call to PUT /api/players/{id}
    Given I try to put a player with "<id>" having isHuman "<isHuman>" avatar "<avatar>" and alias "<alias>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain isHuman "<isHuman>" player having "<avatar>" and "<alias>"

    Examples: This is the default Human Player

      | id     | avatar   | alias      | isHuman | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | MAGICIAN | Cukes Doe2 | false   | HUMAN   | 0      | 0           | 200              |

  @api
  Scenario Outline: A player makes call to DELETE /api/players/{id}
    Given I try to delete a player with "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default Human Player

      | id     | avatar   | alias      | isHuman | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | MAGICIAN | Cukes Doe2 | false   | HUMAN   | 0      | 0           | 204              |
