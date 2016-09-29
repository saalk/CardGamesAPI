package nl.knikit.cardgames.model;

import java.util.EnumSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import lombok.Getter;

/**
 * <H1>CardGame</H1> A selection of card games that can be selected to play. <p> More games will be
 * added in future.
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */
@Getter
public enum CardGame {

    /**
     * HIGHLOW cardgame is a simple higher or lower guessing game. The dealer places one card
     * face-down in front of the player, then another card face-up for the players hand. The player
     * guesses whether the value of the face-down card is higher or lower. <p> The player places his
     * initial bet. The house matches that bet into the pot. When the player guesses, he wins or
     * loses the pot depending on the outcome of his guess. After that round, the player can pass
     * the bet to another player, or go double or nothing on the next bet depending on the specific
     * variant of HIGHLOW.
     */
    @Column(name = "CARDGAME")
    HIGHLOW("Hi-Lo Card Game", "Hoog Laag Kaartspel");
    // BLACKJACK("Blackjack (twenty-one)", "Blackjack (Eenentwintigen)");

    @Transient
    String englishName;
    @Transient
    String dutchName;
    @Transient
    public static Set<CardGame> cardGamesList = EnumSet.of(HIGHLOW);

    // Constructor, each argument to the constructor shadows one of the object's
    // fields
    CardGame(String englishName, String dutchName) {
        this.englishName = englishName;
        this.dutchName = dutchName;
    }

    /*
     * Using @Override annotation while overriding method in Java is one of the
     * best practice in Java.
     */
    @Override
    public String toString() {
        return "" + englishName;
    }

};
