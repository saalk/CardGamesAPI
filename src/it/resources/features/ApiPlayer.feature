Feature: Execute a lifecycle of a player in the card game
  In order to execute the lifecycle of a player
  I should call the api of /api/players/ to post, put, get and delete a player

  @api
  Scenario Outline: A player makes call to GET /api/players/{id}
    Given I try to get a player with "<id>"
    Then I should see that the response has "<HTTP status code>"
    And The json response body should be like {}

    Examples: This is the default Human Player

      | id   | avatar | alias     | isHuman | aiLevel | cubits | securedLoan | HTTP status code |
      | 1234 | ELF    | Cukes Doe | true    | HUMAN   | 0      | 0           | 404              |

  @api
  Scenario Outline: A player makes call to POST /api/players
    Given I try to post a "<isHuman>" player having "<avatar>" and "<alias>"
    Then I should see that the response has "<HTTP status code>"
    And The json response body should be like [{"human": true,"avatar": "ELF","alias": "Cukes Doe","isHuman": true,"aiLevel": "HUMAN","cubits": 0,"securedLoan": 0}]
    Examples: This is the default Human Player

      | id | avatar | alias     | isHuman | aiLevel | cubits | securedLoan | HTTP status code |
      | ?  | ELF    | Cukes Doe | true    | HUMAN   | 0      | 0           | 200              |


  @live
  Scenario: client makes call to POST /api/players/{id} with new id
    Given the client calls /players/1234567890
    When POST to /players with body:
    """
      {
      "gameObjs": [],
      "human": true,
      "playerId": 1234567890,
      "created": "161010-10:00-00000",
      "avatar": "ELF",
      "alias": "Cukes Doe",
      "isHuman": true,
      "aiLevel": "HUMAN",
      "cubits": 1234,
      "securedLoan": 1234
      },
      {
    """
    Then the HTTP status code should be 200
    Then json should be like:
    """
      [
      {
      "@id": "1",
      "gameObjs": [],
      "human": true,
      "playerId": 1234567890,
      "created": "161010-10:00-00000",
      "avatar": "ELF",
      "alias": "Cukes Doe",
      "isHuman": true,
      "aiLevel": "HUMAN",
      "cubits": 1234,
      "securedLoan": 1234
      },
      ]
    """
