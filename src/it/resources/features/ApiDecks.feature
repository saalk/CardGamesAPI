Feature: Execute a lifecycle of a deck in the card game
  In order to execute the lifecycle of a deck
  I should call the api of /api/decks/ to post, put, get and delete a deck

  @Api @Decks
  Scenario Outline: A frontend makes call to GET /api/decks/{id}
    Given I try to get a deck with invalid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should be like {}

    Examples: This is the Ace of Spades card in Deck

      | id   | dealtTo | game | card | cardOrder | shuffle | HTTP status code |
      | 1234 |         |      | AS   | 0         | true    | 404              |

  @Api @Decks
  Scenario Outline: A frontend makes call to POST /api/games to make a deck
    Given I try to post a type "<type>" game having "<winner>" and "<ante>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a game for a deck

    Examples: This is the default ante HIGHLOW Game

      | id     | decks | winner | state       | type    | currentRound | ante | HTTP status code |
      | latest | []    |        | SELECT_GAME | HIGHLOW | 0            | 50   | 201              |

  @Api @Decks
  Scenario Outline: A frontend makes call to POST /api/decks
    Given I try to post a new deck with shuffle "<shuffle>" for game "<game>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the Ace of Spades card in Deck

      | id     | dealtTo | game   | card | cardOrder | shuffle | HTTP status code |
      | latest |         | latest |      |           | true    | 201              |

  @Api @Decks
  Scenario Outline: A frontend makes call to GET /api/decks/{id}
    Given I try to get a deck with valid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain card "<card>" deck with shuffle "<shuffle>" having "<dealtTo>" and "<cardOrder>" for game "<game>"

    Examples: This is the Ace of Spades card in Deck


      | id     | dealtTo | game   | card | cardOrder | shuffle | HTTP status code |
      | latest |         | latest |      |           | true    | 200              |

  @Api @Decks
  Scenario Outline: A frontend makes call to POST /api/players to make a dealtTo
    Given I try to post a human "<human>" dealtTo having "<avatar>" and "<alias>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a dealtTo

    Examples: This is the default Human Player

      | id     | avatar | alias       | human | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | ELF    | DealtTo Doe | true    | HUMAN   | 0      | 0           | 201              |

  @Api @Decks
  Scenario Outline: A frontend makes call to PUT /api/decks/{<id>}?dealTo=<dealtTo>
    Given I try to put a deck with "<id>" having dealtTo "<dealtTo>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain card "<card>" deck with shuffle "<shuffle>" having "<dealtTo>" and "<cardOrder>" for game "<game>"

    Examples: This is the Ace of Spades card in Deck

      | id     | dealtTo | game   | card | cardOrder | shuffle | HTTP status code |
      | latest | latest  | latest |      |           | true    | 200              |

  @Api @Decks
  Scenario Outline: A frontend makes call to DELETE /api/games/{id} for a deck
    Given I try to delete a game for a deck with "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default HIGHLOW Game

      | id     | decks | winner | state       | type      | currentRound | ante | HTTP status code |
      | latest | []    | latest | SELECT_GAME | BLACKJACK | 0            | 100  | 204              |

  @Api @Decks
  Scenario Outline: A frontend makes call to DELETE /api/decks/{id}
    Given I try to delete a deck with "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the Ace of Spades card in Deck

      | id     | dealtTo | game | card | cardOrder | shuffle | HTTP status code |
      | latest | latest  |      | AS   | 9         | true    | 204              |

  @Api @Decks
  Scenario Outline: A frontend makes call to DELETE /api/players/{id} the dealtTo
    Given I try to delete the dealtTo "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default Human Player

      | id     | avatar | alias       | human | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | ELF    | DealtTo Doe | true    | HUMAN   | 0      | 0           | 204              |
