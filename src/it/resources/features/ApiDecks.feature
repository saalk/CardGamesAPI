Feature: Execute a lifecycle of a deck in the card game
  In order to execute the lifecycle of a deck
  I should call the api of /api/decks/ to post, put, get and delete a deck

  @Api @Decks
  Scenario Outline: A frontend makes call to GET /api/decks/<id>
    Given I try to get a deck with invalid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should be like Deck not found

    Examples: This is the Ace of Spades card in Deck

      | id   | dealtTo | game | card | cardOrder | shuffle | HTTP status code |
      | 1234 |         |      | AS   | 0         | true    | 404              |

  @Api @Decks
  Scenario Outline: A frontend makes call to POST /api/games to make a game for a deck
    Given I try to post a gameType "<gameType>" game having "<winner>" and ante "<ante>" and state "<state>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a game for a deck

    Examples: This is the default ante HIGHLOW Game

      | id     | decks | winner | state      | gameType | currentRound | ante | HTTP status code |
      | latest | []    |        | SETUP_GAME | HIGHLOW  | 0            | 50   | 201              |

  @Api @Decks
  Scenario Outline: A frontend makes call to POST /api/decks
    Given I try to post a new deck with shuffle "<shuffle>" for game "<game>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain at least "<count>" decks

    Examples: This is a Deck

      | id     | dealtTo | game   | card | cardOrder | shuffle | HTTP status code | count |
      | latest |         | latest |      |           | true    | 201              | 52    |

  @Api @Decks
  Scenario Outline: A frontend makes call to GET /api/decks/<id>
    Given I try to get a deck with valid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain card "<card>" deck with shuffle "<shuffle>" having "<dealtTo>" and "<cardOrder>" for game "<game>"

    Examples: This is a Deck

      | id     | dealtTo | game   | card | cardOrder | shuffle | HTTP status code |
      | latest |         | latest |      |           | true    | 200              |

  @Api @Decks
  Scenario Outline: A frontend makes call to GET /api/decks
    Given I try to get all decks
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain at least "<count>" decks

    Examples: This is the default decks

      | count | HTTP status code |
      | 52    | 200              |

  @Api @Decks
  Scenario Outline: A frontend makes call to GET /api/decks?game={game}
    Given I try to get all decks for game "<game>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain exactly "<count>" decks

    Examples: This is the default decks

      | game   | count | HTTP status code |
      | latest | 52     | 200              |

  @Api @Decks
  Scenario Outline: A frontend makes call to POST /api/players to make a dealtTo
    Given I try to post a human "<human>" dealtTo having "<avatar>" and "<alias>" and "<aiLevel>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a dealtTo

    Examples: This is the default Human Player

      | id     | avatar | alias       | human | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | ELF    | DealtTo Doe | true  | HUMAN   | 0      | 0           | 201              |

  @Api @Decks
  Scenario Outline: A frontend makes call to PUT /api/decks/<id>?dealTo=<dealtTo>
    Given I try to put a deck with "<id>" having dealtTo "<dealtTo>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain card "<card>" deck with shuffle "<shuffle>" having "<dealtTo>" and "<cardOrder>" for game "<game>"

    Examples: This is the Ace of Spades card in Deck

      | id     | dealtTo | game   | card | cardOrder | shuffle | HTTP status code |
      | latest | latest  | latest |      |           | true    | 200              |

  @Api @Decks
  Scenario Outline: A frontend makes call to DELETE /api/decks/<id>
    Given I try to delete a deck with "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the Ace of Spades card in Deck

      | id     | dealtTo | game | card | cardOrder | shuffle | HTTP status code |
      | latest | latest  |      | AS   | 9         | true    | 204              |

  @Api @Decks
  Scenario Outline: A frontend makes call to DELETE /api/players/<id> the dealtTo
    Given I try to delete the dealtTo "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default Human Player

      | id     | avatar | alias       | human | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | ELF    | DealtTo Doe | true  | HUMAN   | 0      | 0           | 204              |

  @Api @Decks
  Scenario Outline: A frontend makes call to DELETE /api/decks?id={id},{id}
    Given I try to delete all decks with "<ids>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default

      | ids | HTTP status code |
      | all | 204              |

  @Api @Decks
  Scenario Outline: A frontend makes call to DELETE /api/games/<id> for a deck
    Given I try to delete a game for a deck with "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default HIGHLOW Game

      | id     | decks | winner | state      | type      | currentRound | ante | HTTP status code |
      | latest | []    | latest | SETUP_GAME | BLACKJACK | 0            | 100  | 204              |
