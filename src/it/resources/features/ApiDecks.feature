Feature: Execute a lifecycle of a deck in the card game
  In order to execute the lifecycle of a deck
  I should call the api of /api/decks/ to post, put, get and delete a deck

  @api @decks
  Scenario Outline: A frontend makes call to GET /api/decks/{id}
    Given I try to get a deck with invalid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should be like [{}]

    Examples: This is the Ace of Spades card in Deck

      | id   | dealtTo | gameObj | card | cardOrder | shuffle | HTTP status code |
      | 1234 |         |         | AS   | 0         | true    | 404              |

  @api @decks
  Scenario Outline: A frontend makes call to POST /api/decks
    Given I try to post a card "<card>" deck having "<dealtTo>" and "<cardOrder>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain card "<card>" deck having "<dealtTo>" and "<cardOrder>"

    Examples: This is the Ace of Spades card in Deck

      | id     | dealtTo | gameObj | card | cardOrder | shuffle | HTTP status code |
      | latest |         |         | AS   | 1         | true    | 201              |

  @api @decks
  Scenario Outline: A frontend makes call to GET /api/decks
    Given I try to get a deck with valid "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain card "<card>" deck having "<dealtTo>" and "<cardOrder>"

    Examples: This is the Ace of Spades card in Deck


      | id     | dealtTo | gameObj | card | cardOrder | shuffle | HTTP status code |
      | latest |         |         | AS   | 1         | true    | 200              |

  @api @decks
  Scenario Outline: A frontend makes call to PUT /api/decks/{<id>}
    Given I try to put a deck with "<id>" having card "<card>" dealtTo "<dealtTo>" and cardOrder "<cardOrder>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain card "<card>" deck having "<dealtTo>" and "<cardOrder>"

     Examples: This is the Ace of Spades card in Deck

       | id     | dealtTo | gameObj | card | cardOrder | shuffle | HTTP status code |
       | latest |         |         | AS   | 9         | true    | 200              |

  @api @decks
  Scenario Outline: A frontend makes call to POST /api/players to make a dealtTo
    Given I try to post a isHuman "<isHuman>" dealtTo having "<avatar>" and "<alias>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a dealtTo

    Examples: This is the default Human Player

      | id     | avatar | alias       | isHuman | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | ELF    | DealtTo Doe | true    | HUMAN   | 0      | 0           | 201              |

  @api @decks
  Scenario Outline: A frontend makes call to PUT /api/decks/{<id>}?dealtTo=<dealtTo>
    Given I try to put a deck with "<id>" having dealtTo "<dealtTo>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain card "<card>" deck having "<dealtTo>" and "<cardOrder>"

    Examples: This is the Ace of Spades card in Deck

      | id     | dealtTo | gameObj | card | cardOrder | shuffle | HTTP status code |
      | latest | latest  |         | AS   | 9         | true    | 200              |

  @api @decks
  Scenario Outline: A frontend makes call to DELETE /api/decks/{id}
    Given I try to delete a deck with "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the Ace of Spades card in Deck

      | id     | dealtTo | gameObj | card | cardOrder | shuffle | HTTP status code |
      | latest | latest  |         | AS   | 9         | true    | 204              |

  @api @decks
  Scenario Outline: A frontend makes call to DELETE /api/players/{id} the dealtTo
    Given I try to delete the dealtTo "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default Human Player

      | id     | avatar | alias       | isHuman | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | ELF    | DealtTo Doe | true    | HUMAN   | 0      | 0           | 204              |
