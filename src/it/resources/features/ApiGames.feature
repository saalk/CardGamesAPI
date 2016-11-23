Feature: Execute a lifecycle of a game in the card game
  In order to execute the lifecycle of a game
  I should call the api of /api/games/ to post, put, get and delete a game

  @api @games
  Scenario Outline: A frontend makes call to GET /api/games/{id}
    Given I try to get a game with invalid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should be like [{}]

    Examples: This is the no ante HIGHLOW Game

      | id   | decks | winner | state       | cardGameType | currentRound | ante | HTTP status code |
      | 1234 | []    |        | SELECT_GAME | HIGHLOW      | 0            | 0    | 404              |

  @api @games
  Scenario Outline: A frontend makes call to POST /api/games
    Given I try to post a cardGameType "<cardGameType>" game having "<winner>" and "<ante>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain cardGameType "<cardGameType>" game having "<winner>" and "<ante>"

    Examples: This is the default ante HIGHLOW Game

      | id     | decks | winner | state       | cardGameType | currentRound | ante | HTTP status code |
      | latest | []    |        | SELECT_GAME | HIGHLOW      | 0            | 50   | 201              |

  @api @games
  Scenario Outline: A frontend makes call to GET /api/games
    Given I try to get a game with valid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain cardGameType "<cardGameType>" game having "<winner>" and "<ante>"

    Examples: This is the default ante HIGHLOW Game

      | id     | decks | winner | state       | cardGameType | currentRound | ante | HTTP status code |
      | latest | []    |        | SELECT_GAME | HIGHLOW      | 0            | 50   | 200              |


  @api @games
  Scenario Outline: A frontend makes call to PUT /api/games/{<id>}
    Given I try to put a game with "<id>" having cardGameType "<cardGameType>" winner "<winner>" and ante "<ante>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain cardGameType "<cardGameType>" game having "<winner>" and "<ante>"

    Examples: This is the default HIGHLOW Game

      | id     | decks | winner | state       | cardGameType | currentRound | ante | HTTP status code |
      | latest | []    |        | SELECT_GAME | BLACKJACK    | 0            | 100  | 200              |

  @api @games
  Scenario Outline: A frontend makes call to POST /api/players to make a winner
    Given I try to post a isHuman "<isHuman>" winner having "<avatar>" and "<alias>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a winner

    Examples: This is the default Human Player

      | id     | avatar | alias      | isHuman | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | ELF    | Winner Doe | true    | HUMAN   | 0      | 0           | 201              |

  @api @games
  Scenario Outline: A frontend makes call to PUT /api/games/{<id>}?winner=<winner>
    Given I try to put a game with "<id>" having winner "<winner>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain cardGameType "<cardGameType>" game having "<winner>" and "<ante>"

    Examples: This is the default HIGHLOW Game

      | id     | decks | winner | state       | cardGameType | currentRound | ante | HTTP status code |
      | latest | []    | latest | SELECT_GAME | BLACKJACK      | 0          | 100  | 200              |

  @api @games
  Scenario Outline: A frontend makes call to DELETE /api/games/{id}
    Given I try to delete a game with "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default HIGHLOW Game

      | id     | decks | winner | state       | cardGameType | currentRound | ante | HTTP status code |
      | latest | []    | latest | SELECT_GAME | BLACKJACK    | 0            | 100  | 204              |

  @api @games
  Scenario Outline: A frontend makes call to DELETE /api/players/{id} the winner
    Given I try to delete the winner "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default Human Player

      | id     | avatar   | alias      | isHuman | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | MAGICIAN | Cukes Doe2 | false   | HUMAN   | 0      | 0           | 204              |

