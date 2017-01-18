Feature: Execute a lifecycle of a cardGame in the card game
  In order to execute the lifecycle of a cardGame
  I should call the api of /api/cardgames/ with

  0 - no trigger -> all states:
  //GET    api/cardgames/1

  1 - trigger init -> state becomes/stays IS_CONFIGURED after POST/PUT:
  //POST   api/cardgames/init           ?gameType/ante
  //PUT    api/cardgames/1/init         ?gameType/ante

  2 - trigger setup -> state becomes HAS_PLAYERS after POST:
  //POST   api/cardgames/init/human/2         ?gameType/ante
  //POST   api/cardgames/1/setup/human        ?alias/avatar/securedLoan            // br: no shuffle if only player
  //POST   api/cardgames/1/setup/ai           ?alias/avatar/securedLoan/aiLevel    // br: human first
  //PUT    api/cardgames/1/setup/players/2    ?alias/avatar/securedLoan/aiLevel/playingOrder // br: playingOrder +1, -1 only
  //DELETE api/cardgames/1/setup/players/3    // br: only for ai players, br: possible no dealing yet

  3 - trigger shuffle -> state becomes IS_SHUFFLED after POST:
  //POST   api/cardgames/1/shuffle/cardsindeck  ?jokers                             // br: humand and one or more ai

  4 - trigger turn or autoturn -> state becomes PLAYING/GAME_WON/NO_WINNER after PUT:
  //PUT    api/cardgames/1/turn/players/2     ?action=deal/higher/lower/pass/next for human or ai player

  @Api @CardGames
  Scenario Outline: INIT a NEW CardGame with POST /api/cardgames/init?gameType=<gameType>&ante=<ante>
    Given I try to init a gameType "<gameType>" cardGame with playerId "<playerId>" and ante "<ante>"
    Then I should see that the response has HTTP status "<HTTP status code>"

    Examples: CardGames - Game entity with relative id to refer to in later scenarios

      | id | deckId | casinoId | playerId | gameType  | ante | HTTP status code | resultState   | resultWinner |
      | 1  | null   | null     | null     | Hi-Lo     | 1000 | 201              | IS_CONFIGURED | null         |
      | 2  | null   | null     | null     | Hi-Lo     | 2000 | 201              | IS_CONFIGURED | null         |
      | 3  | null   | null     | null     | Hi-Lo     |      | 201              | IS_CONFIGURED | null         |
      | 4  | null   | null     | null     | Hi-Lo     |      | 201              | IS_CONFIGURED | null         |
      | 5  | null   | null     | null     | Hi-Lo     | 5000 | 201              | IS_CONFIGURED | null         |
      | 6  | null   | null     | null     | Hi-Lo     |      | 201              | IS_CONFIGURED | null         |

  @Api @CardGames
  Scenario Outline: A frontend makes call to POST /api/players to make a player
    Given I try to post a human "<human>" player for a new card game having "<avatar>" and "<alias>" and "<aiLevel>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a player

    Examples: CardGames - Player entity with relative id to refer to in later scenarios

      | id     | avatar   | alias            | human | aiLevel | cubits | securedLoan | HTTP status code |
      | 1      | Magician | Cukes human Doe1 | true  | Human   | 0      | 0           | 201              |
      | 2      | Magician | Cukes ai Doe2    | false | Low     | 0      | 0           | 201              |
      | 3      | Magician | Cukes ai Doe3    | false | Medium  | 0      | 0           | 201              |
      | 4      | Magician | Cukes ai Doe4    | false | High    | 0      | 0           | 201              |
      | 5      | Magician | Cukes human Doe5 | true  | Human   | 0      | 0           | 201              |
      | 6      | Magician | Cukes human Doe6 | true  | Human   | 0      | 0           | 201              |

  @Api @CardGames
  Scenario Outline: INIT a NEW CardGame for a player with POST /api/cardgames/init/human/{playerId}?gameType=<gameType>&ante=<ante>
    Given I try to init a gameType "<gameType>" cardGame with playerId "<playerId>" and ante "<ante>"
    Then I should see that the response has HTTP status "<HTTP status code>"

    Examples: CardGmes (Game entity) with reference to previously created winner and relative id to refer to in later scenarios

      | id | deckId | casinoId | playerId | gameType  | ante  | HTTP status code | resultState   | resultWinner |
      | 7  | null   | null     | 1        | Blackjack | 7000  | 201              | HAS_PLAYERS   | null         |
      | 8  | null   | null     | 2        | Blackjack | 8000  | 201              | IS_CONFIGURED | null         |
      | 9  | null   | null     | 3        | Blackjack |       | 201              | IS_CONFIGURED | null         |
      | 10 | null   | null     | 4        | Blackjack |       | 201              | IS_CONFIGURED | null         |
      | 11 | null   | null     | 5        | Blackjack | 11000 | 201              | HAS_PLAYERS   | null         |
      | 12 | null   | null     | 6        | Blackjack |       | 201              | HAS_PLAYERS   | null         |

  @Api @CardGames
  Scenario Outline: INIT some CHANGES for a CardGame with PUT /api/cardgames/{id}/init?gameType=<gameType>&ante=<ante>
    Given I try to init changes to a cardGame with id "<id>" having gameType "<gameType>" and ante "<ante>"
    Then I should see that the response has HTTP status "<HTTP status code>"

    Examples: This is the default HIGHLOW CardGame

      | id | deckId | casinoId | playerId | gameType  | ante | HTTP status code | resultState   | resultWinner |
      | 1  | null   | null     | 1        | Hi-Lo     | 100  | 200              | HAS_PLAYERS   | null         |
      | 2  | null   | null     | 2        | Hi-Lo     | 200  | 200              | IS_CONFIGURED | null         |
      | 3  | null   | null     | 3        | Hi-Lo     | 300  | 200              | IS_CONFIGURED | null         |
      | 7  | null   | null     | 1        | Blackjack | 700  | 200              | HAS_PLAYERS   | null         |
      | 8  | null   | null     | 2        | Blackjack | 800  | 200              | IS_CONFIGURED | null         |
      | 9  | null   | null     | 3        | Blackjack | 900  | 200              | IS_CONFIGURED | null         |

  @Api @CardGames
  Scenario Outline: SETUP a human or ai player for a CardGame with POST /api/cardgames/{id}/setup/"<HumanOrAi>"?alias="<alias>"&avatar="<avatar>"&securedLoan="<securedLoan>"&aiLevel="<aiLevel>"
    Given I try to setup a HumanOrAi "<HumanOrAi>" player for cardGame with id "<id>" having "<alias>" and "<avatar>" and "<securedLoan>" and "<aiLevel>"
    Then I should see that the response has HTTP status "<HTTP status code>"

    Examples: This is the default Human Player

      | id   | playerId | avatar | alias             | HumanOrAi | aiLevel | cubits | securedLoan | HTTP status code | resultState   | resultWinner |
      | 1    | 7        | Goblin | Cukes human Doe7  | human     | Human   | 0      | 1           | 201              | HAS_PLAYERS   | null         |
      | 2    | 8        | Goblin | Cukes ai Doe8     | ai        | Low     | 0      | 2           | 201              | IS_CONFIGURED | null         |
      | 3    | 9        | Goblin | Cukes ai Doe9     | ai        | Low     | 0      | 3           | 201              | IS_CONFIGURED | null         |
      | 7    | 10       | Goblin | Cukes human Doe10 | human     | Human   | 0      | 7           | 201              | HAS_PLAYERS   | null         |
      | 8    | 11       | Goblin | Cukes ai Doe11    | ai        | Low     | 0      | 8           | 201              | IS_CONFIGURED | null         |
      | 9    | 12       | Goblin | Cukes ai Doe12    | ai        | Low     | 0      | 9           | 201              | IS_CONFIGURED | null         |

  @Api @CardGames
  Scenario Outline: SETUP some CHANGES to a CardGame with PUT /api/cardgames/{id}/setup/{playerId}?alias/avatar/aiLevel/securedLoan/playingOrder
    Given I try to setup changes to a cardGame with id "<id>" having "<playerId>" player with "<alias>" and "<avatar>" and "<securedLoan>" and "<aiLevel>" and "<playingOrder>"
    Then I should see that the response has HTTP status "<HTTP status code>"

    Examples: This is the default ante HIGHLOW CardGame

      | id   | playerId | avatar | alias            | HumanOrAi | aiLevel | cubits | securedLoan | playingOrder | HTTP status code | resultState   | resultWinner |
      | 1    | 1        | Roman  | Cukes human Doe1 | human     | Human   | 0      | 11          |              | 200              | HAS_PLAYERS   | null         |
      | 2    | 2        | Roman  | Cukes ai Doe2    | ai        | Low     | 0      | 12          | 1            | 200              | IS_CONFIGURED | null         |
      | 3    | 3        | Roman  | Cukes ai Doe3    | ai        | Low     | 0      | 13          | -1           | 200              | IS_CONFIGURED | null         |
      | 7    | 7        | Roman  | Cukes human Doe7 | human     | Human   | 0      | 17          |              | 200              | HAS_PLAYERS   | null         |
      | 8    | 8        | Roman  | Cukes ai Doe8    | ai        | Low     | 0      | 18          | -1           | 200              | IS_CONFIGURED | null         |
      | 9    | 9        | Roman  | Cukes ai Doe9    | ai        | Low     | 0      | 19          | 1            | 200              | IS_CONFIGURED | null         |

  @Api @CardGames
  Scenario Outline: DELETE a player for a CardGame with DELETE /api/cardgames/{id}/"<HumanOrAi>"/{playerId}
    Given I try to delete a HumanOrAi "<HumanOrAi>" player with "<playerId>" for a cardGame with id "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"

    Examples: This is the default ante HIGHLOW CardGame

      | id   | playerId | avatar | alias            | HumanOrAi | aiLevel | cubits | securedLoan | HTTP status code | resultState   | resultWinner |
      | 7    | 1        | Roman  | Cukes human Doe1 | human     | Human   | 0      | 14          | 204              | HAS_PLAYERS   | null         |
      | 8    | 2        | Roman  | Cukes ai Doe2    | ai        | Low     | 0      | 15          | 204              | IS_CONFIGURED | null         |
      | 9    | 3        | Roman  | Cukes ai Doe3    | ai        | Low     | 0      | 16          | 204              | IS_CONFIGURED | null         |

  @Api @CardGames
  Scenario Outline: SHUFFLE a CardGame with POST /api/cardgames/{id}/shuffle/cards?jokers
    Given I try to shuffle a cardGame with id "<id>" and jokers "<jokers>"
    Then I should see that the response has HTTP status "<HTTP status code>"

    Examples: This is the default

      | id    | jokers | HTTP status code | resultState   | resultWinner |
      | 1     | 0      | 201              | resultState   | resultWinner |
      | 2     | 1      | 500              | HAS_PLAYERS   | null         |
      | 3     |        | 201              | HAS_PLAYERS   | null         |

  @Ignore
  Scenario Outline: TURN for a player in a CardGame with PUT /api/cardgames/{id}/turn/players/(playerId}
    Given I try to make a turn with "<action>" action for player with id "<playerId>" in a cardGame with id "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"

    Examples: This is the default Human Player

      | id    | playerId | action | HTTP status code | resultState   | resultWinner |
      | 7     | 1        | Deal   | 200              | resultState   | resultWinner |
      | 7     | 1        | Higher | 200              | resultState   | resultWinner |
      | 12    | 3        | Higher | 500              | resultState   | resultWinner |

  @Ignore
  Scenario Outline: A frontend makes call to DELETE /api/cardgames/{id}
    Given I try to delete a cardGame "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a cardGame with response "<response>"


    Examples: This is the default HIGHLOW CardGame

      | id     | decks | casinos | human | state         | gameType  | ante | HTTP status code | resultState   | resultWinner |
      | 1      | []    | []      | 1     | IS_CONFIGURED | Hi-Lo     | 1000 | 204              | resultState   | resultWinner |
      | 2      | []    | []      | 1     | IS_CONFIGURED | Hi-Lo     | 1000 | 204              | resultState   | resultWinner |
      | 3      | []    | []      | 1     | IS_CONFIGURED | Hi-Lo     | 1000 | 204              | resultState   | resultWinner |
      | 4      | []    | []      | 1     | IS_CONFIGURED | Hi-Lo     | 1000 | 204              | resultState   | resultWinner |
      | 5      | []    | []      | 1     | IS_CONFIGURED | Hi-Lo     | 1000 | 204              | resultState   | resultWinner |
      | 6      | []    | []      | 1     | IS_CONFIGURED | Hi-Lo     | 1000 | 204              | resultState   | resultWinner |
      | 7      | []    | []      | 1     | IS_CONFIGURED | Hi-Lo     | 1000 | 204              | resultState   | resultWinner |
      | 8      | []    | []      | 1     | IS_CONFIGURED | Hi-Lo     | 1000 | 204              | resultState   | resultWinner |
      | 9      | []    | []      | 1     | IS_CONFIGURED | Hi-Lo     | 1000 | 204              | resultState   | resultWinner |
      | 10     | []    | []      | 1     | IS_CONFIGURED | Hi-Lo     | 1000 | 204              | resultState   | resultWinner |
      | 11     | []    | []      | 1     | IS_CONFIGURED | Hi-Lo     | 1000 | 204              | resultState   | resultWinner |
      | 12     | []    | []      | 1     | IS_CONFIGURED | Hi-Lo     | 1000 | 204              | resultState   | resultWinner |

  @Api @CardGames
  Scenario Outline: A frontend makes call to DELETE /api/players/{id} cardGame player
    Given I try to delete a cardGame player "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default Human Player

      | id     | avatar   | alias      | human | aiLevel | cubits | securedLoan | HTTP status code |
      | 1      | Magician | Cukes Doe2 | false | Human   | 0      | 0           | 204              |
      | 2      | Magician | Cukes Doe2 | false | Human   | 0      | 0           | 204              |
      | 3      | Magician | Cukes Doe2 | false | Human   | 0      | 0           | 204              |
      | 4      | Magician | Cukes Doe2 | false | Human   | 0      | 0           | 204              |
      | 5      | Magician | Cukes Doe2 | false | Human   | 0      | 0           | 204              |
      | 6      | Magician | Cukes Doe2 | false | Human   | 0      | 0           | 204              |

