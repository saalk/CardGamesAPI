Feature: Execute a lifecycle of a cardGame in the card game
  In order to execute the lifecycle of a cardGame
  I should call the api of /api/cardgames/ with

  0 - no trigger -> all states:
  //GET    api/cardgames/1

  1 - trigger init -> state becomes IS_CONFIGURED after POST:
  //POST   api/cardgames/init           ?gameType/ante
  //PUT    api/cardgames/1/init         ?gameType/ante

  2 - trigger setup -> state becomes HAS_PLAYERS after POST:
  //POST   api/cardgames/init/human/2         ?gameType/ante
  //POST   api/cardgames/1/setup/human        ?alias/avatar/securedLoan            // no dealing yet
  //POST   api/cardgames/1/setup/ai           ?alias/avatar/securedLoan/aiLevel
  //PUT    api/cardgames/1/setup/players/2    ?alias/avatar/securedLoan/aiLevel/playingOrder
  //DELETE api/cardgames/1/setup/players/3    // only for ai players, possible no dealing yet

  3 - trigger shuffle -> state becomes IS_SHUFFLED after POST:
  //POST   api/cardgames/1/shuffle/cardsindeck  ?jokers                             // no dealing yet

  4 - trigger turn or autoturn -> state becomes PLAYING/GAME_WON/NO_WINNER after PUT:
  //PUT    api/cardgames/1/turn/    players/2   ?action=deal/higher/lower/pass for human player
  //PUT    api/cardgames/1/autoturn/players/3   // auto deal, higher, lower or pass for ai player

  @Api @CardGames
  Scenario Outline: INIT a NEW CardGame with POST /api/cardgames/init?gameType=<gameType>&ante=<ante>
    Given I try to init a gameType "<gameType>" cardGame with playerId "<playerId>" and ante "<ante>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a cardGame with response "<response>"

    Examples: This is the default ante HIGHLOW CardGame

      | id     | decks | casinos | playerId | state         | gameType  | ante | HTTP status code | response |
      | latest | []    | []      |          | IS_CONFIGURED | HIGHLOW   | 1000 | 201              | api/cardgames/init = GameType in param=HIGHLOW Ante in param=1000 |
      | latest | []    | []      |          | IS_CONFIGURED | BLACKJACK |  500 | 201              | api/cardgames/init = GameType in param=BLACKJACK Ante in param=500 |
      | latest | []    | []      |          | IS_CONFIGURED | BLACKJACK |      | 201              | api/cardgames/init = GameType in param=BLACKJACK No ante in param specified |
      | latest | []    | []      |          | IS_CONFIGURED |           |  750 | 201              | api/cardgames/init = No gameType in param specified Ante in param=750 |
      | latest | []    | []      |          | IS_CONFIGURED |           |      | 201              | api/cardgames/init = No gameType in param specified No ante in param specified |

  @Ignore
  Scenario Outline: A frontend makes call to POST /api/players to make a player
    Given I try to post a human "<human>" player having "<avatar>" and "<alias>" and "<aiLevel>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a player

    Examples: This is the default Human Player

      | id     | avatar | alias        | human | aiLevel | cubits | securedLoan | HTTP status code |
      | latest | ELF    | CardGame Doe | true  | HUMAN   | 0      | 0           | 201              |

  @Api @CardGames
  Scenario Outline: INIT a NEW CardGame for a player with POST /api/cardgames/init/human/{playerId}?gameType=<gameType>&ante=<ante>
    Given I try to init a gameType "<gameType>" cardGame with playerId "<playerId>" and ante "<ante>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a cardGame with response "<response>"

    Examples: This is the default ante HIGHLOW CardGame

      | id     | decks | casinos | playerId | state         | gameType  | ante | HTTP status code | response |
      | latest | []    | []      | 1        | IS_CONFIGURED | HIGHLOW   | 1000 | 201              | api/cardgames/init/human/{id} = PlayerId in path=1 GameType in param=HIGHLOW Ante in param=1000 |
      | latest | []    | []      | 2        | IS_CONFIGURED | BLACKJACK |  500 | 201              | api/cardgames/init/human/{id} = PlayerId in path=2 GameType in param=BLACKJACK Ante in param=500 |
      | latest | []    | []      | 3        | IS_CONFIGURED | BLACKJACK |      | 201              | api/cardgames/init/human/{id} = PlayerId in path=3 GameType in param=BLACKJACK No ante in param specified |
      | latest | []    | []      | 4        | IS_CONFIGURED |           |  750 | 201              | api/cardgames/init/human/{id} = PlayerId in path=4 No gameType in param specified Ante in param=750 |
      | latest | []    | []      | 5        | IS_CONFIGURED |           |      | 201              | api/cardgames/init/human/{id} = PlayerId in path=5 No gameType in param specified No ante in param specified |

  @Api @CardGames
  Scenario Outline: INIT some CHANGES for a CardGame with PUT /api/cardgames/{id}/init?gameType=<gameType>&ante=<ante>
    Given I try to init changes to a cardGame with id "<id>" having gameType "<gameType>" and ante "<ante>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a cardGame with response "<response>"

    Examples: This is the default HIGHLOW CardGame

      | id     | decks | casinos | playerId | state         | gameType  | ante | HTTP status code | response |
      | 97     | []    | []      | 1        | IS_CONFIGURED | HIGHLOW   | 1000 | 200              | api/cardgames/{id}/init = Id in path=97 GameType in param=HIGHLOW Ante in param=1000 |
      | 98     | []    | []      | 2        | IS_CONFIGURED | BLACKJACK |  500 | 200              | api/cardgames/{id}/init = Id in path=98 GameType in param=BLACKJACK Ante in param=500 |
      | 99     | []    | []      | 3        | IS_CONFIGURED | BLACKJACK |      | 200              | api/cardgames/{id}/init = Id in path=99 GameType in param=BLACKJACK No ante in param specified |
      | 100    | []    | []      | 4        | IS_CONFIGURED |           |  750 | 200              | api/cardgames/{id}/init = Id in path=100 No gameType in param specified Ante in param=750 |
      | 101    | []    | []      | 3        | IS_CONFIGURED |           |      | 200              | api/cardgames/{id}/init = Id in path=101 No gameType in param specified No ante in param specified |

  @Api @CardGames
  Scenario Outline: SETUP a human or ai player for a CardGame with POST /api/cardgames/{id}/setup/"<HumanOrAi>"?alias="<alias>"&avatar="<avatar>"&securedLoan="<securedLoan>"&aiLevel="<aiLevel>"
    Given I try to setup a HumanOrAi "<HumanOrAi>" player for cardGame with id "<id>" having "<alias>" and "<avatar>" and "<securedLoan>" and "<aiLevel>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a cardGame with response "<response>"

    Examples: This is the default Human Player

      | id     | avatar | alias              | HumanOrAi | aiLevel | cubits | securedLoan | HTTP status code | response |
      | 97     | ELF    | CardGame human Doe | human     | HUMAN   | 0      | 0           | 201              | api/cardgames/{id}/setup/{humanOrAi} = Id in path=97 HumanOrAi in path=human Alias in param=CardGame human Doe Avatar in param=ELF AiLevel in param=CardGame human Doe SecuredLoan in param=0 |
      | 98     |        | CardGame ai Doe    | ai        | LOW     | 0      | 0           | 201              | api/cardgames/{id}/setup/{humanOrAi} = Id in path=98 HumanOrAi in path=ai Alias in param=CardGame ai Doe No avatar in param specified AiLevel in param=CardGame ai Doe SecuredLoan in param=0 |
      | 99     | ELF    | CardGame ai Doe    | human     | LOW     | 0      | 0           | 201              | api/cardgames/{id}/setup/{humanOrAi} = Id in path=99 HumanOrAi in path=human Alias in param=CardGame ai Doe Avatar in param=ELF AiLevel in param=CardGame ai Doe SecuredLoan in param=0 |
      | 100    | ELF    | CardGame ai Doe    | ai        |         | 0      | 0           | 201              | api/cardgames/{id}/setup/{humanOrAi} = Id in path=100 HumanOrAi in path=ai Alias in param=CardGame ai Doe Avatar in param=ELF No aiLevel in param specified SecuredLoan in param=0 |
      | 101    | ELF    |                    | ai        | LOW     | 0      | 0           | 201              | api/cardgames/{id}/setup/{humanOrAi} = Id in path=101 HumanOrAi in path=ai No alias in param specified Avatar in param=ELF AiLevel in param= SecuredLoan in param=0 |

  @Api @CardGames
  Scenario Outline: SETUP some CHANGES to a CardGame with PUT /api/cardgames/{id}/setup/{playerId}?alias/avatar/aiLevel/securedLoan
    Given I try to setup changes to a cardGame with id "<id>" having "<playerId>" player with "<alias>" and "<avatar>" and "<securedLoan>" and "<aiLevel>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a cardGame with response "<response>"

    Examples: This is the default ante HIGHLOW CardGame

      | id     | playerId | avatar | alias              | human | aiLevel | cubits | securedLoan | HTTP status code | response |
      | 97     | 1        | ELF    | CardGame human Doe | true  | HUMAN   | 0      | 0           | 200              | api/cardgames/{id}/setup/players/{playerId} = Id in path=97 PlayerId in path=1 Alias in param=CardGame human Doe Avatar in param=ELF AiLevel in param=CardGame human Doe SecuredLoan in param=0 No playingOrder in param specified |
      | 98     | 2        | ELF    | CardGame ai Doe    | false | LOW     | 0      | 0           | 200              | api/cardgames/{id}/setup/players/{playerId} = Id in path=98 PlayerId in path=2 Alias in param=CardGame ai Doe Avatar in param=ELF AiLevel in param=CardGame ai Doe SecuredLoan in param=0 No playingOrder in param specified |
      | 99     | 3        |        | CardGame ai Doe    | ai    | LOW     | 0      | 0           | 200              | api/cardgames/{id}/setup/players/{playerId} = Id in path=99 PlayerId in path=3 Alias in param=CardGame ai Doe No avatar in param specified AiLevel in param=CardGame ai Doe SecuredLoan in param=0 No playingOrder in param specified |
      | 100    | 4        | ELF    | CardGame ai Doe    | human | LOW     | 0      | 0           | 200              | api/cardgames/{id}/setup/players/{playerId} = Id in path=100 PlayerId in path=4 Alias in param=CardGame ai Doe Avatar in param=ELF AiLevel in param=CardGame ai Doe SecuredLoan in param=0 No playingOrder in param specified |
      | 101    | 5        | ELF    | CardGame ai Doe    | ai    |         | 0      | 0           | 200              | api/cardgames/{id}/setup/players/{playerId} = Id in path=101 PlayerId in path=5 Alias in param=CardGame ai Doe Avatar in param=ELF No aiLevel in param specified SecuredLoan in param=0 No playingOrder in param specified |
      | 102    | 6        | ELF    |                    | ai    | LOW     | 0      | 0           | 200              | api/cardgames/{id}/setup/players/{playerId} = Id in path=102 PlayerId in path=6 No alias in param specified Avatar in param=ELF AiLevel in param= SecuredLoan in param=0 No playingOrder in param specified |

  @Api @CardGames
  Scenario Outline: DELETE a player for a CardGame with DELETE /api/cardgames/{id}/"<HumanOrAi>"/{playerId}
    Given I try to delete a HumanOrAi "<HumanOrAi>" player with "<playerId>" for a cardGame with id "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a cardGame with response "<response>"

    Examples: This is the default ante HIGHLOW CardGame

      | id     | HumanOrAi | playerId | avatar | alias              | human | aiLevel | cubits | securedLoan | HTTP status code | response |
      | 97     | human     | 1        | ELF    | CardGame human Doe | true  | HUMAN   | 0      | 0           | 200              | api/cardgames/{id}/setup/{humanOrAi}/(playerId} = Id in path=97 HumanOrAi in path=human PlayerId in path=1 |
      | 98     | ai        | 2        | ELF    | CardGame ai Doe    | false | LOW     | 0      | 0           | 200              | api/cardgames/{id}/setup/{humanOrAi}/(playerId} = Id in path=98 HumanOrAi in path=ai PlayerId in path=2 |

  @Api @CardGames
  Scenario Outline: SHUFFLE a CardGame with POST /api/cardgames/{id}/shuffle/cards?jokers
    Given I try to shuffle a cardGame with id "<id>" and jokers "<jokers>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a cardGame with response "<response>"

    Examples: This is the default Human Player

      | id     | jokers | HTTP status code | response |
      | 97     | 0      | 201              | api/cardgames/{id}/shuffle = Id in path=97 Jokers in param=0 |
      | 98     | 1      | 201              | api/cardgames/{id}/shuffle = Id in path=98 Jokers in param=1 |
      | 99     |        | 201              | api/cardgames/{id}/shuffle = Id in path=99 No jokers in param specified |

  @Api @CardGames
  Scenario Outline: TURN for a player in a CardGame with PUT /api/cardgames/{id}/turn/players/(playerId}
    Given I try to make a turn with "<action>" action for player with id "<playerId>" in a cardGame with id "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a cardGame with response "<response>"

    Examples: This is the default Human Player

      | id     | playerId | action | HTTP status code | response |
      | 97     | 2        | HIGHER | 200              | api/cardgames/{id}/turn/players/{playerId} = Id in path=97 PlayerId in path=2 Action in param=HIGHER |
      | 98     | 2        | LOWER  | 200              | api/cardgames/{id}/turn/players/{playerId} = Id in path=98 PlayerId in path=2 Action in param=LOWER |
      | 99     | 2        | LOWER  | 200              | api/cardgames/{id}/turn/players/{playerId} = Id in path=99 PlayerId in path=2 Action in param=LOWER |
      | 100    | 3        | AUTO   | 200              | api/cardgames/{id}/turn/players/{playerId} = Id in path=100 PlayerId in path=3 Action in param=AUTO |

  @Ignore
  Scenario Outline: A frontend makes call to DELETE /api/cardgames/{id}
    Given I try to delete a cardGame "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response should contain a cardGame with response "<response>"


    Examples: This is the default HIGHLOW CardGame

      | id     | decks | casinos | human | state         | gameType  | ante | HTTP status code | response |
      | 97     | []    | []      | 1     | IS_CONFIGURED | HIGHLOW   | 1000 | 204              | api/cardgames/init =  GameType in param=HIGHLOW Ante in param=1000 |
      | 98     | []    | []      | 2     | IS_CONFIGURED | BLACKJACK |  500 | 204              | api/cardgames/init =  GameType in param=BLACKJACK Ante in param=500 |
      | 99     | []    | []      | 3     | IS_CONFIGURED |           |      | 204              | api/cardgames/init =  No gameType in param specified No ante in param specified |

  @Ignore
  Scenario Outline: A frontend makes call to DELETE /api/players/{id} cardGame player
    Given I try to delete a player "<id>"
    Then I should see that the response has HTTP status "<HTTP status code>"
    And The json response body should have no content

    Examples: This is the default Human Player

      | id     | avatar   | alias      | human | aiLevel | cubits | securedLoan | HTTP status code |
      | 1      | MAGICIAN | Cukes Doe2 | false | HUMAN   | 0      | 0           | 204              |

