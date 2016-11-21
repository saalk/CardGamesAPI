Feature: Execute a lifecycle of a game in the card game
  In order to execute the lifecycle of a game
  I should call the api of /api/games/ to post, put, get and delete a game

  @api
  Scenario Outline: A game makes call to GET /api/games/{id}
    Given I try to get a game with invalid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should be like [{}]

    Examples: This is the default HIGHLOW Game

      | id   | decks | winner | state       | cardGameType | currentRound | ante | HTTP status code |
      | 1234 | []    | 0      | SELECT_GAME | HIGHLOW      | 0            | 0    | 404              |

  @api
  Scenario Outline: A game makes call to POST /api/games
    Given I try to post a cardGameType "<cardGameType>" game having "<winner>" and "<ante>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain cardGameType "<cardGameType>" game having "<winner>" and "<ante>"

    Examples: This is the default HIGHLOW Game

      | id     | decks | winner | state       | cardGameType | currentRound | ante | HTTP status code |
      | latest | []    | 0      | SELECT_GAME | HIGHLOW      | 0            | 50   | 201              |

  @api
  Scenario Outline: A game makes call to GET /api/games
    Given I try to get a game with valid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain cardGameType "<cardGameType>" game having "<winner>" and "<ante>"

    Examples: This is the default HIGHLOW Game

      | id     | decks | winner | state       | cardGameType | currentRound | ante | HTTP status code |
      | latest | []    | 0      | SELECT_GAME | HIGHLOW      | 0            | 50   | 200              |


  @api
  Scenario Outline: A game makes call to PUT /api/games/{id}
    Given I try to put a game with "<id>" having cardGameType "<cardGameType>" winner "<winner>" and ante "<ante>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain cardGameType "<cardGameType>" game having "<winner>" and "<ante>"

    Examples: This is the default HIGHLOW Game

      | id     | decks | winner | state       | cardGameType | currentRound | ante | HTTP status code |
      | latest | []    | 1      | SELECT_GAME | BLACKJACK      | 0          | 100  | 200              |


  @api
  Scenario Outline: A game makes call to DELETE /api/games/{id}
    Given I try to delete a game with "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default HIGHLOW Game

      | id     | decks | winner | state       | cardGameType | currentRound | ante | HTTP status code |
      | latest | []    | 1      | SELECT_GAME | BLACKJACK    | 0            | 100  | 204              |
