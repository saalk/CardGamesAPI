package nl.knikit.cardgames.model;

import java.util.EnumSet;
import java.util.Set;

import lombok.Getter;


/**
 * <H1>GameVariant</H1> A selection of variants to a specific card games that can be selected to
 * play. <p> More variants will be added in future.
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */

@Getter
public enum GameVariant {

    /**
     * verwijs in deze enum van Regels naar de enum van CardGame voor 'nested enums', anders zullen
     * 2 enums naar eenzelfde interface moeten verwijzen en die is nu niet nodig
     */
    HIGHLOW_1ROUND(GameType.HIGHLOW, "One round double or nothing "),
    HIGHLOW_3_IN_A_ROW_1SUIT(GameType.HIGHLOW, "make '3-in-a-row' one suit"),
    HIGHLOW_5_IN_A_ROW(GameType.HIGHLOW, "'5-in-a-row'");


    //HILOW_DRINKING_WITH_OPPONENTS(CardGame.HIGHLOW, "Drinking with opponent(s)"),
    // If wrong, s/he drinks once, twice or three times
    // After taking at least three cards, the player may choose to continue or
    // pass, BUT ONLY after having taken at least three cards. If the player
    // pass, the next player starts where the previous left off.
    // extra: The next player has to take a drink for each card the first player
    // won.

    //HILOW_LINEOFNINE(CardGame.HIGHLOW, "Line of Nine"),
    //HILOW_JACK(CardGame.HIGHLOW, "Teams of 2");
    // High Low Jack, also known as Hi Low Jack and Pitch, is played with a
    // standard 52-card deck. Partnership, the most commonly played version of
    // the game, places players in teams of two that score points collectively.
    // Varieties include Cutthroat, in which each player scores points
    // individually, and Nine Card, which deals three extra cards and awards
    // points for both the trump five and highest spade in play.

    /*
     * The Set implementations are grouped into general-purpose (eg. HashSet)
     * and special-purpose implementations (eg. EnumSet). The Set
     * implementations are grouped into general-purpose and special-purpose
     * implementations.
     */
    public static Set<GameVariant> highlowGameVariants = EnumSet.of(HIGHLOW_1ROUND, HIGHLOW_3_IN_A_ROW_1SUIT,
            HIGHLOW_5_IN_A_ROW);

    String gameType;
    String rules;

    // Constructor, each argument to the constructor shadows one of the object's
    // fields
    GameVariant(GameType gameType, String rules) {
        this.gameType = gameType.getLabel();
        this.rules = rules;
    }
}
