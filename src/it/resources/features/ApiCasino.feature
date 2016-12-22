Feature: Execute a lifecycle of a casino in the card game
  In order to execute the lifecycle of a casino
  I should call the api of /api/casinos/ to post, put, get and delete a casino

  @Api @Casinos
  Scenario Outline: A frontend makes call to GET /api/casinos/{id}
    Given I try to get a casino with invalid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should be like Casino not found

    Examples: This is the Ace of Spades card in Casino

      | id   | dealtTo | game | card | cardOrder | shuffle | HTTP status code |
      | 1234 |         |      | AS   | 0         | true    | 404              |

  @Api @Casinos
  Scenario Outline: A frontend makes call to POST /api/games to make a game for a casino
    Given I try to post a gameType "<gameType>" game having "<winner>" and ante "<ante>" and state "<state>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a game for a casino

    Examples: This is the default ante HIGHLOW Game

      | id     | casinos | winner | state         | gameType | currentRound | ante | HTTP status code |
      | latest | []      |        | ITERATE_TURNS | HIGHLOW  | 0            | 500  | 201              |

  @Api @Casinos
  Scenario Outline: A frontend makes call to POST /api/players to make a player
    Given I try to post a human "<human>" casino player having "<avatar>" and "<alias>" and "<aiLevel>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a player for a casino

    Examples: This is the default Human Player

      | id     | avatar | alias      | human | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | ELF    | Casino Doe | true  | HUMAN   | 0      | 0           | 201              |

  @Api @Casinos
  Scenario Outline: A frontend makes call to POST /api/casinos?player={players}
    Given I try to post a casino for game "<game>" and players "<player>" having playingOrder "<playingOrder>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a list of casino having game "<game>" and player "<player>" for playingOrder "<playingOrder>"

    Examples: This is a standard Casino

      | id     | player | game   | playingOrder | hands | HTTP status code | count |
      | latest | latest | latest | 1            | []    | 201              | 1     |

  @Api @Casinos
  Scenario Outline: A frontend makes call to PUT /api/casinos/{id}?playingOrder={playingOrder}
    Given I try to put a casino "<id>" with a new playingOrder "<playingOrder>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a casino "<id>" with a new playingOrder "<playingOrder>"

    Examples: This is a standard Casino

      | id     | player | game   | playingOrder | hands | HTTP status code | count |
      | latest | latest | latest | 9            | []    | 200              | 1     |

  @Api @Casinos
  Scenario Outline: A frontend makes call to GET /api/casinos/{id}
    Given I try to get a casino with valid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a casino having game "<game>" and player "<player>" for playingOrder "<playingOrder>"

    Examples: This is a standard Casino

      | id     | player | game   | playingOrder | hands | HTTP status code | count |
      | latest | latest | latest | 9            | []    | 200              | 1    |

  @Api @Casinos
  Scenario Outline: A frontend makes call to GET /api/casinos
    Given I try to get all casinos
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain at least "<count>" casinos

    Examples: This is the default casinos

      | count | HTTP status code |
      | 1     | 200              |

  @Api @Casinos
  Scenario Outline: A frontend makes call to GET /api/casinos?game={game}
    Given I try to get all casinos for game "<game>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain exactly "<count>" casinos

    Examples: This is the default casinos

      | game   | count | HTTP status code |
      | latest | 1     | 200              |

  @Api @Casinos
  Scenario Outline: A frontend makes call to DELETE /api/casinos/{id}
    Given I try to delete a casino with "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is a standard Casino

      | id     | player | game   | playingOrder | hands | HTTP status code | count |
      | latest | latest | latest | 1            | []    | 204              | 1    |

  @Api @Casinos
  Scenario Outline: A frontend makes call to DELETE /api/players/{id} the player
    Given I try to delete the player "<id>" for the casino
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default Human Player

      | id     | avatar | alias      | human | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | ELF    | Casino Doe | true  | HUMAN   | 0      | 0           | 204              |

  @Api @Casinos
  Scenario Outline: A frontend makes call to DELETE /api/casinos?id={id},{id}
    Given I try to delete all casinos with "<ids>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default

      | ids | HTTP status code |
      | all | 204              |

  @Api @Casinos
  Scenario Outline: A frontend makes call to DELETE /api/games/{id} for a casino for a hand
    Given I try to delete a game for a casino for a hand with "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default HIGHLOW Game

      | id     | casinos | winner | state         | type         | currentRound | ante | HTTP status code |
      | latest | []      | latest | ITERATE_TURNS | BLACKJACK | 0            | 100  | 204              |
