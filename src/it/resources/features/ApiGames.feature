Feature: Execute a lifecycle of a game in the card game
  In order to execute the lifecycle of a game
  I should call the api of /api/games/ to post, put, get and delete a game

  @Api @Games
  Scenario Outline: A frontend makes call to GET /api/games/{id}
    Given I try to get a game with invalid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should be like Game not found

    Examples: This is the no ante HIGHLOW Game

      | id   | decks | winner | state       | gameType | currentRound | ante | HTTP status code |
      | 1234 | []    |        | IS_SETUP | HIGHLOW  | 0            | 0    | 404              |

  @Api @Games
  Scenario Outline: A frontend makes call to POST /api/games
    Given I try to post a gameType "<gameType>" game having "<winner>" and ante "<ante>" and state "<state>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain gameType "<gameType>" game having "<winner>" and ante "<ante>" and state "<state>"

    Examples: This is the default ante HIGHLOW Game

      | id     | decks | winner | state       | gameType  | currentRound | ante | HTTP status code |
      | latest | []    |        | IS_SETUP | BLACKJACK | 0            | 10   | 201              |
      | latest | []    |        | IS_SETUP | HIGHLOW   | 0            | 200  | 201              |
      | latest | []    |        | IS_SETUP | HIGHLOW   | 0            | 50   | 201              |

  @Api @Games
  Scenario Outline: A frontend makes call to GET /api/games/{id}
    Given I try to get a game with valid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain gameType "<gameType>" game having "<winner>" and ante "<ante>" and state "<state>"

    Examples: This is the default ante HIGHLOW Game

      | id     | decks | winner | state       | gameType | currentRound | ante | HTTP status code |
      | latest | []    |        | IS_SETUP | HIGHLOW  | 0            | 50   | 200              |

  @Api @Games
  Scenario Outline: A frontend makes call to GET /api/games?gameType={gameType}
    Given I try to get all gameType "<gameType>" games
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain at least "<count>" games

    Examples: This is the default games

      | gameType   | count | HTTP status code |
      | HIGHLOW    | 2     | 200              |
      | BLACKJACK  | 1     | 200              |

  @Api @Games
  Scenario Outline: A frontend makes call to GET /api/games
    Given I try to get all games
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain at least "<count>" games

    Examples: This is the default games

      | count | HTTP status code |
      | 2     | 200              |

  @Api @Games
  Scenario Outline: A frontend makes call to PUT /api/games/{id}
    Given I try to put a game with "<id>" having gameType "<gameType>" winner "<winner>" and ante "<ante>" and state "<state>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain gameType "<gameType>" game having "<winner>" and ante "<ante>" and state "<state>"

    Examples: This is the default HIGHLOW Game

      | id     | decks | winner | state       | gameType  | currentRound | ante | HTTP status code |
      | latest | []    |        | IS_SETUP | BLACKJACK | 0            | 100  | 200              |

  @Api @Games
  Scenario Outline: A frontend makes call to POST /api/players to make a winner
    Given I try to post a human "<human>" winner having "<avatar>" and "<alias>" and "<aiLevel>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a player

    Examples: This is the default Human Player

      | id     | avatar | alias      | human | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | ELF    | Winner Doe | true  | HUMAN   | 0      | 0           | 201              |

  @Api @Games
  Scenario Outline: A frontend makes call to PUT /api/games/{id}?winner={winner}
    Given I try to put a game with "<id>" having winner "<winner>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain gameType "<gameType>" game having "<winner>" and ante "<ante>" and state "<state>"

    Examples: This is the default HIGHLOW Game

      | id     | decks | winner | state       | gameType  | currentRound | ante | HTTP status code |
      | latest | []    | latest | IS_SETUP | BLACKJACK | 0            | 100  | 200              |

  @Api @Games
  Scenario Outline: A frontend makes call to DELETE /api/games/{id}
    Given I try to delete a game "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default HIGHLOW Game

      | id     | decks | winner | state       | gameType  | currentRound | ante | HTTP status code |
      | latest | []    | latest | IS_SETUP | BLACKJACK | 0            | 100  | 204              |

  @Api @Games
  Scenario Outline: A frontend makes call to DELETE /api/players/{id} winner
    Given I try to delete a player "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default Human Player

      | id     | avatar   | alias      | human | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | MAGICIAN | Cukes Doe2 | false | HUMAN   | 0      | 0           | 204              |

  @Api @Games
  Scenario Outline: A frontend makes call to DELETE /api/games?id={id},{id}
    Given I try to delete all games with "<ids>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default

      | ids | HTTP status code |
      | all | 204              |
