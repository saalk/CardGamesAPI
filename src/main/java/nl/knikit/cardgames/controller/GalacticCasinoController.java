package nl.knikit.cardgames.controller;
// @formatter:off
/*
@startuml src/main/resources/plantuml/GalacticCasinoController.png
skinparam classAttributeIconSize 0
package "nl.deknik.cardgames" {
package "controller" {
class GalacticCasinoController {
-State state
-Trigger trigger
+ {static} main()
..other methods..
}
enum State {
SELECT_GAME
SETUP_GAME
..
ITERATE_PLAYERS
ITERATE_TURNS
..
STOP_GAME
OFFER_FICHES
 }
enum Trigger {
GAME_SELECTED
GAME_STOPPED
GAME_SETUP
..
TURN_STARTED
ANOTHER_TURN
TURN_ENDED
..
ROUNDS_ENDED
PLAYER_WINS
DECK_EMPTY
..
GAME_FINISHED
OFFER_ACCEPTED
QUIT
}
enum State -> GalacticCasinoController
GalacticCasinoController --> enum Trigger
package "nl.deknik.cardgames" {
package "model" {
GalacticCasinoController .. GameOld
GalacticCasinoController .. PlayerOld
GalacticCasinoController .. PlayerOld.Predict
GalacticCasinoController .. AiLevel
GalacticCasinoController .. CardGame
GalacticCasinoController .. CardGameVariant
GalacticCasinoController .. Avatar
}
package "nl.deknik.cardgames" {
package "utils" {
GalacticCasinoController . Console
}
}
@enduml
*//*

*/
/*
@startuml

state GalacticCasinoController {
[*] -> SELECT_GAME
SELECT_GAME -down-> OFFER_FICHES: QUIT
SELECT_GAME: start : +1000 fiches
SELECT_GAME: offer : +500 fiches
note right of SELECT_GAME: Games\n HIGHLOW\n- simple double or nothing\n- three in a row
SELECT_GAME -down-> SETUP_GAME: GAME_SELECTED
SETUP_GAME --> SELECT_GAME: GAME_STOPPED
SETUP_GAME: select # bots
SETUP_GAME --> GameEngine: GAME_SETUP
OFFER_FICHES -up-> SELECT_GAME: OFFER_ACCEPTED
OFFER_FICHES --> [*]

state GameEngine {
[*] -> ITERATE_PLAYERS
ITERATE_PLAYERS --> ITERATE_PLAYERS: PLAYER_PASSES
ITERATE_PLAYERS --> ITERATE_TURNS: TURN_STARTED
ITERATE_PLAYERS --> STOP_GAME: DECK_EMPTY \n or ROUNDS_ENDED
ITERATE_TURNS --> ITERATE_TURNS: ANOTHER_TURN
ITERATE_TURNS --> ITERATE_PLAYERS: TURN_ENDED
ITERATE_TURNS --> STOP_GAME: DECK_EMPTY \n or PLAYERS_WINS
STOP_GAME -up-> SELECT_GAME: GAME_FINISHED
}
}
@enduml
*/
// @formatter:on

import java.util.Random;

import nl.knikit.cardgames.model.*;
import nl.knikit.cardgames.model.state.GalacticCasinoStateMachine;

import com.github.oxo42.stateless4j.StateMachineConfig;
//TODO import com.github.oxo42.stateless4j.delegates.Action;

import static nl.knikit.cardgames.model.state.GalacticCasinoStateMachine.State;
import static nl.knikit.cardgames.model.state.GalacticCasinoStateMachine.Trigger;

/**
 * <H1>GalacticCasinoController</H1>Main functionality: <ul> <li>a native casino adventure application that
 * runs on your desktop's console <li>offers card game 'HighLow' <li>each visitor starts with 1000
 * credits <li>choose between various card game variants <li>select up to 2 ai Players (bots) to
 * join a game <li>select their ai level (high, average, low) and try to beat them </ul>
 * <p>
 * <p><h2>GalacticCasinoController Class diagram</h2>Since this is class with the main method it uses lots of
 * 'HAS-A' relationship. Codified via <b>Associations</b><img src="../../../../../../src/main/resources/plantuml/GalacticCasinoController.png" alt="UML1">
 * <h2>Controller Class diagram 'state and triggers</h2><img src="../../../../../../src/main/resources/plantuml/StateMachine.png" alt="UML2">
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 **/

public class GalacticCasinoController {

    private static StateMachineConfig<State, Trigger> cardGameConfig = new StateMachineConfig<>();

    // @formatter:off
    static {
        cardGameConfig.configure(State.SELECT_GAME)
                .permit(Trigger.GAME_SELECTED, State.SETUP_GAME)
                .permit(Trigger.QUIT, State.SELECT_GAME);

        cardGameConfig.configure(State.SETUP_GAME)
                .permit(Trigger.GAME_STOPPED, State.SELECT_GAME)
                .permit(Trigger.GAME_SETUP, State.ITERATE_PLAYERS);

        cardGameConfig.configure(State.ITERATE_PLAYERS)
                .permit(Trigger.GAME_STOPPED, State.SELECT_GAME)
                .permit(Trigger.TURN_STARTED, State.ITERATE_TURNS)
                .permit(Trigger.DECK_EMPTY, State.STOP_GAME)
                .permit(Trigger.ROUNDS_ENDED, State.STOP_GAME);

        cardGameConfig.configure(State.ITERATE_TURNS)
                .permitReentry(Trigger.ANOTHER_TURN)
                .permit(Trigger.TURN_ENDED, State.ITERATE_PLAYERS)
                .permit(Trigger.DECK_EMPTY, State.STOP_GAME)
                .permit(Trigger.PLAYER_WINS, State.STOP_GAME);

        cardGameConfig.configure(State.STOP_GAME)
                .permit(Trigger.GAME_FINISHED, State.SELECT_GAME);

        // @formatter:on
    }
//        // SETUP CONSOLE
//        Console console = new Console(null, null);
//        String[] dummy = {"Continue"};
//        int index = 0;
//        String question;
//        int answer = 0;
//        int secondAnswer = 0;
//        String[] action = null;
//
//        answer = console.getAnswerFromConsole("> Welcome to The Galactic Casino, space-traveller", dummy);
//        System.lineSeparator();
//
//        // SETUP VISITOR
//        String[] avatar = {"Elf", "Magician", "Goblin", "Warrior"};
//        answer = console.getAnswerFromConsole("> What is your avatar? ", avatar);
//
//        Avatar avatarChosen = null;
//        switch (answer) {
//            case 1:
//                avatarChosen = Avatar.ELF;
//                break;
//            case 2:
//                avatarChosen = Avatar.MAGICIAN;
//                break;
//            case 3:
//                avatarChosen = Avatar.GOBLIN;
//                break;
//            case 4:
//                avatarChosen = Avatar.ROMAN;
//                break;
//            default:
//                avatarChosen = Avatar.MAGICIAN;
//                break;
//        }
//        PlayerOld visitor = new PlayerOld(avatarChosen, null, true);
//
//        Random random = new Random();
//        int number = (random.nextInt(30) + 1);
//        System.out.println(
//                "> You're the " + number + "th visitor today " + visitor.getAlias() + "; fromRankName 1000 chips for free!");
//        visitor.setCubits(1000);
//        System.out.println(visitor.displayCubits());
//
//        // SETUP CASINO
//        StateMachine<State, Trigger> galacticCasino = new StateMachine<>(State.SELECT_GAME, cardGameConfig);
//        CardGameType cardGameTypeSelected;
//        CardGameVariant cardGameVariantSelected;
//        // why not null; instead of new GameOld(null); or just GameOld currentGameOld; ?
//        GameOld currentGameOld = new GameOld(null);
//        PlayerOld currentPlayerOld = new PlayerOld(visitor.getAvatar(), null, false);
//        PlayerOld.Predict prediction;
//        PlayerOld.Predict real;
//        int turn = 0;
//
//        // CONTROLLER
//        while (!galacticCasino.getState().equals(State.OFFER_FICHES)) {
//
//            switch (galacticCasino.getState()) {
//                case SELECT_GAME:
//
//                    // CHOOSE GAME
//                    String[] displayCardGames = new String[CardGameType.cardGamesListType.size() + 1];
//                    index = 0;
//                    for (CardGameType cg : CardGameType.cardGamesListType) {
//                        displayCardGames[index] = cg.toString();
//                        index++;
//                    }
//                    displayCardGames[index] = "Leave casino";
//
//                    answer = console.getAnswerFromConsole("> Select a game-table", displayCardGames);
//                    System.lineSeparator();
//                    if (answer == 1) {
//
//                        // make a list of Strings available from CardGames enum's
//                        // toString
//                        cardGameTypeSelected = CardGameType.HIGHLOW;
//
//                        String[] displayCardGameVariants = new String[CardGameVariant.highlowCardGameVariants.size()];
//                        index = 0;
//                        for (CardGameVariant cgv : CardGameVariant.highlowCardGameVariants) {
//                            displayCardGameVariants[index] = cgv.toString();
//                            index++;
//                        }
//
//                        answer = console.autoAnswerOnConsole("> Select playing rules ", displayCardGameVariants, 1);
//                        cardGameVariantSelected = CardGameVariant.HILOW_1ROUND;
//
//                        // SETUP GAME VARIANT
//                        currentGameOld = new GameOld(cardGameVariantSelected);
//                        galacticCasino.fire(Trigger.GAME_SELECTED);
//
//                    } else {
//                        galacticCasino.fire(Trigger.QUIT);
//                    }
//                    break;
//
//                case SETUP_GAME:
//
//                    // CHOOSE OPPONENTS
//                    action = new String[]{"1 opponent", "2 opponents", "No Opponent", "Leave table"};
//                    answer = console.getAnswerFromConsole("> Select opponents", action);
//
//                    if (answer == 4) {
//                        galacticCasino.fire(Trigger.GAME_STOPPED);
//                        break;
//                    }
//
//                    // SET GAME WITH VISITOR, OPPONENTS (IF ANY), AND SHUFFLED DECK
//                    currentGameOld.getDeck().shuffle();
//                    currentGameOld.removeAllPlayers();
//                    currentGameOld.setPlayer(visitor).emptyHand();
//
//                    // TODO choose who may begin
//                    currentGameOld.setBegins(visitor);
//
//                    if (answer <= 2) {
//
//                        // CHOOSE AI
//                        String[] displayAiLevels = new String[AiLevel.aiLevels.size()];
//                        index = 0;
//                        for (AiLevel al : AiLevel.aiLevels) {
//                            displayAiLevels[index] = al.toString();
//                            index++;
//                        }
//
//                        secondAnswer = console.getAnswerFromConsole("> Opponent strenght", displayAiLevels);
//                        System.lineSeparator();
//                        for (int i = 0; i < answer; i++) {
//                            switch (secondAnswer) {
//                                case 1:
//                                    currentPlayerOld = currentGameOld.setPlayer(new PlayerOld(avatarChosen, AiLevel.LOW, false));
//                                    currentPlayerOld.setCubits(1000);
//                                    break;
//                                case 2:
//                                    currentPlayerOld = currentGameOld.setPlayer(new PlayerOld(avatarChosen, AiLevel.MEDIUM, false));
//                                    currentPlayerOld.setCubits(1000);
//                                    break;
//                                case 3:
//                                    currentPlayerOld = currentGameOld.setPlayer(new PlayerOld(avatarChosen, AiLevel.HIGH, false));
//                                    currentPlayerOld.setCubits(1000);
//                                    break;
//                                default:
//                                    currentPlayerOld = currentGameOld.setPlayer(new PlayerOld(avatarChosen, AiLevel.MEDIUM, false));
//                                    currentPlayerOld.setCubits(1000);
//                                    break;
//                            }
//                            currentPlayerOld.emptyHand();
//                        }
//                    }
//                    currentPlayerOld = null;
//                    for (PlayerOld cp : currentGameOld.getPlayerOlds()) {
//                        if (!cp.isHuman()) {
//                            System.out.println("  " + cp.getAlias() + " joined the game");
//                        }
//                    }
//                    // SET ANTE
//                    answer = console.getAnswerFromConsole("> Set ante for this table",
//                            new String[]{"Stake is 20", "50", "100"});
//
//                    switch (answer) {
//                        case 1:
//                            currentGameOld.setAnte(20);
//                            break;
//                        case 2:
//                            currentGameOld.setAnte(50);
//                            break;
//                        case 3:
//                            currentGameOld.setAnte(100);
//                            break;
//                        default:
//                            currentGameOld.setAnte(20);
//                    }
//                    galacticCasino.fire(Trigger.GAME_SETUP);
//                    break;
//
//                case ITERATE_PLAYERS:
//
//                    // SETUP PLAYER AND ROUNDS, STOP WHEN ZERO ROUNDS LEFT
//                    currentPlayerOld = currentGameOld.getNextPlayer(currentPlayerOld);
//                    if (currentPlayerOld == currentGameOld.getBegins() && !(currentPlayerOld.getHandOld().countNumberOfCards() == 0)) {
//                        currentGameOld.decreaseRoundsLeft();
//                        if (currentGameOld.getRoundsLeft() == 0) {
//                            answer = console.getAnswerFromConsole("> Last round played, game ended", dummy);
//                            galacticCasino.fire(Trigger.ROUNDS_ENDED);
//                            break;
//                        }
//                    }
//                    turn = 0;
//
//                    // CHECK CARDS LEFT STOP WHEN ZERO
////                    if (currentGameOld.getDeck().searchNextCardNotInHand() == -1) {
////                        answer = console.getAnswerFromConsole("> No more cards, the game ends " + currentPlayerOld.getAlias(),
////                                dummy);
////                        galacticCasino.fire(Trigger.DECK_EMPTY);
////                    }
//
//                    // debug logging
//                    log = new StringBuilder();
//                    log.append(System.lineSeparator());
//                    log.append("= Next player=============================");
//                    log.append(System.lineSeparator());
//                    log.append("  State: " + galacticCasino.getState());
//                    log.append(System.lineSeparator());
//                    log.append("  Permitted trigger: " + galacticCasino.getPermittedTriggers());
//                    log.append(System.lineSeparator());
//                    log.append("  Current GameOld: " + currentGameOld);
//                    log.append(System.lineSeparator());
//                    log.append("  Current PlayerOld: " + currentPlayerOld);
//                    log.append(System.lineSeparator());
//                    log.append("= End ====================================");
//                    logger.debug(log.toString());
//
//                    // DEAL A INITIAL CARD AND SHOW
////                    currentPlayerOld.addCardToHand(currentGameOld.getDeck().deal(currentPlayerOld.getId()));
////                    System.out.println("" + currentPlayerOld.toStringPlayers());
//
//                    galacticCasino.fire(Trigger.TURN_STARTED);
//                    break;
//
//                case ITERATE_TURNS:
//
//                    // CHECK CARDS LEFT IN DECK
////                    if (currentGameOld.getDeck().searchNextCardNotInHand() == -1) {
////                        answer = console.getAnswerFromConsole("> No more cards, the game ends " + currentPlayerOld.getAlias()
////                                + ", keep the " + (currentGameOld.getAnte() * (int) Math.pow(2, turn - 1)), dummy);
////                        currentPlayerOld.setCubits(currentGameOld.getAnte() * (int) Math.pow(2, turn - 1));
////                        galacticCasino.fire(Trigger.DECK_EMPTY);
////                        break;
////                    }
////                    turn++;
//
//                    // GET (AI) PLAYER ACTION
//                    if (turn == 1) {
//                        action = new String[]{"Lower", "Higher"};
//                        question = "> Stake is set to " + currentGameOld.getAnte() + ", what will your next casino be "
//                                + currentPlayerOld.getAlias() + "?";
//                    } else {
//                        action = new String[]{"Lower", "Higher", "Pass"};
//                        question = "> Double or nothing " + (currentGameOld.getAnte() * (int) Math.pow(2, turn - 1))
//                                + ", what will your next card be " + currentPlayerOld.getAlias() + "?";
//                    }
//
//                    if (currentPlayerOld.isHuman()) {
//                        answer = console.getAnswerFromConsole(question, action);
//                        switch (answer) {
//                            case 1:
//                                prediction = PlayerOld.Predict.LOW;
//                                break;
//                            case 2:
//                                prediction = PlayerOld.Predict.HIGH;
//                                break;
//                            default:
//                                prediction = PlayerOld.Predict.INDECISIVE;
//                                break;
//                        }
//                    } else {
//                        prediction = currentPlayerOld.predictNextCard((turn == 1) ? true : false);
//                        switch (prediction) {
//                            case LOW:
//                                console.autoAnswerOnConsole(question, action, 1);
//                                break;
//                            case HIGH:
//                                console.autoAnswerOnConsole(question, action, 2);
//                                break;
//                            default:
//                                console.autoAnswerOnConsole(question, action, 3);
//                                break;
//                        }
//                    }
//
//                    // TURN ENDS WHEN INDECISIVE
//                    if (prediction == PlayerOld.Predict.INDECISIVE) {
//                        answer = console.getAnswerFromConsole("> A safe choice " + currentPlayerOld.getAlias() + ", keep the "
//                                + ((currentGameOld.getAnte() * (int) Math.pow(2, turn - 2))), dummy);
//                        currentPlayerOld.setCubits(currentGameOld.getAnte() * (int) Math.pow(2, turn - 2));
//                        System.out.println(currentPlayerOld.toStringPlayers());
//                        System.out.println(currentGameOld);
//                        galacticCasino.fire(Trigger.TURN_ENDED);
//                        break;
//                    }
//
//                    // TURN THE CARD AND SHOW
////                    real = currentPlayerOld.addCardToHand(currentGameOld.getDeck().deal(currentPlayerOld.getId()));
////                    System.out.println(currentPlayerOld.toStringPlayers());
//
//                    // debug logging
//                    log = new StringBuilder();
//                    log.append("");
//                    log.append(System.lineSeparator());
//                    log.append("= Loop Turn for current player ===========");
//                    log.append(System.lineSeparator());
//                    log.append("  State: " + galacticCasino.getState());
//                    log.append(System.lineSeparator());
//                    log.append("  Permitted trigger: " + galacticCasino.getPermittedTriggers());
//                    log.append(System.lineSeparator());
//                    log.append("  Current GameOld: " + currentGameOld);
//                    log.append(System.lineSeparator());
//                    log.append("  Anwer from player: " + prediction);
//                    log.append(System.lineSeparator());
////                    log.append("  Result from dealing: " + real);
////                    log.append(System.lineSeparator());
//                    log.append("= End ====================================");
//                    logger.debug(log.toString());
//
//                    // WIN, LOOSE OR JUST NEXT TURN
////                    if (prediction == real) {
////                        if (turn >= currentGameOld.getTurnsToWin()) {
////                            // TODO depend upon game variant
////                            answer = console.getAnswerFromConsole("> " + turn + " in a row, you won!", dummy);
////                            currentPlayerOld.setCubits((currentGameOld.getAnte() * (int) Math.pow(2, turn - 1)));
////                            currentGameOld.setWinner(currentPlayerOld);
////
////                            System.out.println(currentGameOld);
////                            galacticCasino.fire(Trigger.PLAYER_WINS);
////                        } else {
////                            answer = console.getAnswerFromConsole("> Nice guess " + currentPlayerOld.getAlias() + "!", dummy);
////                            galacticCasino.fire(Trigger.ANOTHER_TURN);
////                        }
////                    } else {
////                        if (real == PlayerOld.Predict.EQUAL) {
////                            answer = console.getAnswerFromConsole("> Equal cards " + currentPlayerOld.getAlias()
////                                    + ", so hand over the " + (currentGameOld.getAnte() * (int) Math.pow(2, turn - 1)), dummy);
////                        } else {
////                            answer = console.getAnswerFromConsole("> Bad guess " + currentPlayerOld.getAlias()
////                                    + ", i'll take the " + (currentGameOld.getAnte() * (int) Math.pow(2, turn - 1)), dummy);
////                        }
////                        galacticCasino.fire(Trigger.TURN_ENDED);
////                        currentPlayerOld.setCubits(-(currentGameOld.getAnte() * (int) Math.pow(2, turn - 1)));
////                        System.out.println(currentGameOld);
////                    }
////                    break;
//
//                case STOP_GAME:
//
//                    // SHOW STATS
//                    System.out.println("  > Statistics");
//                    for (PlayerOld cp : currentGameOld.getPlayerOlds()) {
//                        System.out.println(cp.displayCubits());
//                    }
//                    answer = console.getAnswerFromConsole("> Have a look at the outcome " + visitor.getAlias(), dummy);
//                    galacticCasino.fire(Trigger.GAME_FINISHED);
//                    break;
//
//                default:
//
//                    galacticCasino.fire(Trigger.QUIT);
//                    break;
//
//            }
//
//            if (galacticCasino.getState().equals(State.OFFER_FICHES)) {
//                action = new String[]{"Stay and fromRankName another 500 chips for free!", "Cash and leave"};
//                answer = console.getAnswerFromConsole("> Leaving Casino", action);
//
//                if (answer == 1) {
//                    visitor.setCubits(500);
//                    galacticCasino.fire(Trigger.OFFER_ACCEPTED);
//                    System.out.println(visitor.displayCubits());
//                } else {
//                    galacticCasino.fire(Trigger.QUIT);
//                }
//            }
//        }
//        System.out.println("> Cashing your chips " + visitor.getAlias() + ", see you again!");
//        System.out.println(visitor.displayCubits());
//
//        System.exit(0);

}


