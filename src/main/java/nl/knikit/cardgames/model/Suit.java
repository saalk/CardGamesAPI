package nl.knikit.cardgames.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * <H1>Suit</H1>
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */
@Embeddable
public enum Suit {

    /**
     * Because enum are constants, the names of an enum type's fields are in uppercase letters.
     * In this enum Suit the 4 constants are declared with the properties of the English and Dutch names
     * In addition the Joker is added
     */
    @Transient
    CLUBS("♣", "C", "Clubs", "Klaver"),
    DIAMONDS("♦", "D", "Diamonds", "Ruiten"),
    HEARTS("♥", "H", "Hearts", "Harten"),
    SPADES("♠", "S", "Spades", "Schoppen"),
    JOKERS("⋆", "J", "Joker", "Joker");
    @Transient
    String shortSymbol;
    @Column(name = "SUIT")
    String shortName;
    @Transient
    String englishName;
    @Transient
    String dutchName;

    // Constructor, each argument to the constructor shadows one of the object's fields
    Suit(String shortSymbol, String shortName, String englishName, String dutchName) {
        this.shortSymbol = shortSymbol;
        this.shortName = shortName;
        this.englishName = englishName;
        this.dutchName = dutchName;
    }

    // Getters, no setters needed


    String getShortSymbol() {
        return shortSymbol;
    }

    String getShortName() {
        return shortName;
    }

    String getEnglishName() {
        return englishName;
    }

    String getDutchName() {
        return dutchName;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Suit [value=").append(shortName).append("]");
        return builder.toString();
    }

};
