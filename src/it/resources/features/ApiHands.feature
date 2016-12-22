Feature: Execute a lifecycle of a hand in the card game
  In order to execute the lifecycle of a hand
  I should call the api of /api/hands/ to post, put, get and delete a hand

  @Api @Hands
  Scenario Outline: A frontend makes call to GET /api/hands/{id}
    Given I try to get a hand with invalid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should be like Hand not found

    Examples: This is the Ace of Spades card in Hand

      | id   | player | casino | cards      | cardOrders | HTTP status code | count |
      | 1234 | latest | latest | AS         | 1          | 404              | 1     |

  @Api @Hands
  Scenario Outline: A frontend makes call to POST /api/games to make a game for a casino
    Given I try to post a gameType "<gameType>" game having "<winner>" and ante "<ante>" and state "<state>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a game for a hand

    Examples: This is the default ante HIGHLOW Game

      | id     | decks | winner | state           | gameType | currentRound | ante | HTTP status code |
      | latest | []    |        | ITERATE_PLAYERS | HIGHLOW  | 0            | 50   | 201              |

  @Api @Hands
  Scenario Outline: A frontend makes call to POST /api/players to make a player for hand
    Given I try to post a human "<human>" player having "<avatar>" and "<alias>" and "<aiLevel>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a player for a hand

    Examples: This is the default Human Player

      | id     | avatar | alias     | human | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | ELF    | Hands Doe | true  | HUMAN   | 0      | 0           | 201              |

  @Api @Hands
  Scenario Outline: A frontend makes call to POST /api/casinos?player={players}
    Given I try to post a casino for a hand with game "<game>" and players "<player>" having playingOrder "<playingOrder>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a list of casinos for a hand

    Examples: This is a standard Casino

      | id     | player | game   | playingOrder | hands | HTTP status code | count |
      | latest | latest | latest | 1            | []    | 201              | 1     |

  @Api @Hands
  Scenario Outline: A frontend makes call to POST /api/hands?card={card}
    Given I try to post a new hand with cards "<cards>" and orders "<cardOrders>" for a player "<player>" and a casino "<casino>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a list of cards "<cards>" hand with cardOrders "<cardOrders>" having "<player>" and casino "<casino>"

    Examples: This is a Hand

      | id     | player | casino | cards      | cardOrders | HTTP status code | count |
      | latest | latest | latest | AS         | 1          | 201              | 1     |

  @Api @Hands
  Scenario Outline: A frontend makes call to GET /api/hands/{id}
    Given I try to get a hand with valid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain the card "<card>" hand with cardOrder "<cardOrder>" having "<player>" and casino "<casino>"

    Examples: This is a Hand

      | id     | player | casino | card      | cardOrder | HTTP status code | count |
      | latest | latest | latest | AS        | 1         | 200              | 1     |

  @Api @Hands
  Scenario Outline: A frontend makes call to GET /api/hands
    Given I try to get all hands
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain at least "<count>" hands

    Examples: This is a Hand

      | id     | player | casino | cards      | cardOrders | HTTP status code | count |
      | latest | latest |        |            |            | 200              | 1     |

  @Api @Hands
  Scenario Outline: A frontend makes call to GET /api/hands?casino={casino}
    Given I try to get all hands for a casino "<casino>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain at least "<count>" hands

    Examples: This is a Hand

      | id     | player | casino | cards      | cardOrders | HTTP status code | count |
      | latest | latest | latest |            |            | 200              | 1     |

  @Api @Hands
  Scenario Outline: A frontend makes call to DELETE /api/hands/{id}
    Given I try to delete a hand with "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the Ace of Spades card in Hand

      | id     | player | casino | cards      | cardOrders | HTTP status code | count |
      | latest | latest | latest |            |            | 204              | 1     |

  @Api @Hands
  Scenario Outline: A frontend makes call to DELETE /api/casinos/{id} the casino
    Given I try to delete a casino for a hand with "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default HIGHLOW Casino

      | id     | hands | winner | state           | casinoType | currentRound | ante | HTTP status code |
      | latest | []    |        | ITERATE_PLAYERS | HIGHLOW    | 0            | 50   | 204              |

  @Api @Hands
  Scenario Outline: A frontend makes call to DELETE /api/games/{id} for a casino
    Given I try to delete a game for a casino for the hand with "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default HIGHLOW Game

      | id     | decks | winner | state           | gameType | currentRound | ante | HTTP status code |
      | latest | []    |        | ITERATE_PLAYERS | HIGHLOW  | 0            | 50   | 204              |

  @Api @Hands
  Scenario Outline: A frontend makes call to DELETE /api/players/{id} the player
    Given I try to delete the player for the hand "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default Human Player

      | id     | avatar | alias     | human | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | ELF    | Hands Doe | true  | HUMAN   | 0      | 0           | 204              |

  @Api @Hands
  Scenario Outline: A frontend makes call to DELETE /api/hands?id={id},{id}
    Given I try to delete all hands with "<ids>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default

      | ids | HTTP status code |
      | all | 204              |
