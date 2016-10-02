/**
 * @author KvdM
 */
package nl.knikit.cardgames.model;

import java.util.EnumSet;
import java.util.Set;


/**
 * <H1>CardGameVariant</H1> A selection of variants to a specific card games that can be selected to
 * play. <p> More variants will be added in future.
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */
public enum CardGameVariant {

    /**
     * verwijs in deze enum van Regels naar de enum van CardGame voor 'nested enums', anders zullen
     * 2 enums naar eenzelfde interface moeten verwijzen en die is nu niet nodig
     */
    HILOW_1ROUND(CardGameType.HIGHLOW, "One round double or nothing "),
    HILOW_3_IN_A_ROW_1SUIT(CardGameType.HIGHLOW, "'3-in-a-row' one suit"),
    HILOW_5_IN_A_ROW(CardGameType.HIGHLOW, "'5-in-a-row'");


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
    public static Set<CardGameVariant> highlowCardGameVariants = EnumSet.of(HILOW_1ROUND, HILOW_3_IN_A_ROW_1SUIT,
            HILOW_5_IN_A_ROW);

    String cardName;
    String ruleName;

    // Constructor, each argument to the constructor shadows one of the object's
    // fields
    CardGameVariant(CardGameType cardName, String ruleName) {
        this.cardName = cardName.getEnglishName();
        this.ruleName = ruleName;
    }

    // Getters, no setters needed
    String getCardName() {
        return cardName;
    }

    String getRuleName() {
        return ruleName;
    }

    @Override
    public String toString() {
        return ruleName;
    }

};
