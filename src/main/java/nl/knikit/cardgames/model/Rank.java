package nl.knikit.cardgames.model;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * <H1>Rank</H1> Progressive value for a card. <p> There are thirteen ranks of each of the four
 * French suits. The rank 'Joker' is the 14th one.
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */

@Getter
public enum Rank implements Serializable {

    /**
     * Because enum are constants, the names of an enum type's fields are in uppercase letters.
     */

    ACE("A"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"),
    NINE("9"), TEN("10"), JACK("J"), QUEEN("Q"), KING("K"), JOKER("R");

    /**
     * Make a :
     * - a static HashMap lookup with key value pairs -> key= code/name, value= the ENUM
     * - a private field code/name and a method getCode/Name()
     * - a static get(code/name) that returns the ENUM based on the lookup key
     * -> the static get could better be called byLetter, byValue to distinguish from @Getter
     *
     * Now you can us a method get() that return with the ENUM based on a int/name
     * eg. "A" -> RANK.ACE
     *
     * HashMap:
     * - static hashMap.put(key, value)
     * - value = hashMap.get(key)
     */

    private static final Map<String,Rank> lookup
            = new HashMap<>();
    static {
        for(Rank rank : EnumSet.allOf(Rank.class))
            lookup.put(rank.getName(), rank);
    }

    private String name;

    Rank() {
        this.name = "";
    }

    Rank(String name) {
        this();
        this.name = name;
    }


    public static Rank get(String name) {
        return lookup.get(name);
    }

    /**
     * Usually the Face cards (K,Q,J) are worth 13,12,11 points, each Aces are worth 1. But the
     * selected card game determines the playing value.
     * <p>
     * Values for {@link CardGameType}:
     * 1. Vote if equal cards are a loss or correct guess (usually loss since only high low counts).
     * 2. No jokers.
     * 3. Ace is worth 1
     */
    public int getValue(CardGameType inputCardGameType) {
        int value = 0;
        switch (inputCardGameType) {
            case HIGHLOW:
                switch (this) {
                    case JOKER:
                        value = 0;
                        break;
                    case ACE:
                        value = 1;
                        break;
                    case KING:
                        value = 13;
                        break;
                    case QUEEN:
                        value = 12;
                        break;
                    case JACK:
                        value = 11;
                        break;
                    default:
                        value = Integer.parseInt(name);
                }
            default:
                break;
        }
        return value;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Rank [value=").append(name).append("]");
        return builder.toString();
    }

}
