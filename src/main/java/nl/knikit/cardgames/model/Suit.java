package nl.knikit.cardgames.model;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * <H1>Suit</H1>
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */

@Getter
public enum Suit implements Serializable {

    /**
     * Because enum are constants, the names of an enum type's fields are in uppercase letters.
     * Behind the enum is the code (int) or the name (String) of the enum.
     * Make a static lookup and use a private name int or String
     */
    CLUBS("C"),
    DIAMONDS("D"),
    HEARTS("H"),
    SPADES("S"),
    JOKERS("J");

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
    private static final Map<String,Suit> lookup
            = new HashMap<>();
    static {
        for(Suit suit : EnumSet.allOf(Suit.class))
            lookup.put(suit.getName(), suit);
    }
    private String name;

    Suit() {
        this.name = "";
    }
    Suit(String name) {
        this();
        this.name = name;
    }

    public static Suit get(String name) {
        return lookup.get(name);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Suit [value=").append(name).append("]");
        return builder.toString();
    }
}
