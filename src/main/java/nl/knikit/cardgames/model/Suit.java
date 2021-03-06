package nl.knikit.cardgames.model;

import nl.knikit.cardgames.model.enumlabel.LabeledEnum;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <H1>Suit</H1>
 *
 * Suit.ENUM("String")
 * - Suit.CLUBS.name() or
 * - Suit.CLUBS.label or
 * - Suit.CLUBS.getLabel()
 * -> gets the "C"
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */
@Getter
public enum Suit implements LabeledEnum {

    /**
     * Because enum are constants, the names of an enum type's fields are in uppercase letters.
     * Behind the enum is the code (int) or the name (String) of the enum.
     * Make a static lookup and use a private name int or String
     */
    @Column(name = "SUIT", length = 10, nullable = false)
    CLUBS("C"),
    DIAMONDS("D"),
    HEARTS("H"),
    SPADES("S"),
    JOKERS("J");

    /**
     * Make a :
     * - a static HashMap lookup with key value pairs -> key= code/name, value= the ENUM
     * - a private field code/name and a method getCode/Name()
     * - a static fromLabel(code/name) that returns the ENUM based on the lookup key
     * -> the static fromLabel could better be called byLetter, byValue to distinguish from @Getter
     *
     * Now you can us a method fromLabel() that return with the ENUM based on a int/name
     * eg. "A" -> RANK.ACE
     *
     * HashMap:
     * - static hashMap.put(key, value)
     * - value = hashMap.fromLabel(key)
     */
    private static final Map<String,Suit> lookup
            = new HashMap<>();
    static {
        for(Suit suit : EnumSet.allOf(Suit.class))
            lookup.put(suit.getLabel(), suit);
    }

    @Transient
    private String label;

    Suit() {
    }

    Suit(String label) {
        this();
        this.label = label;
    }

    public static Suit fromLabel(String label) {
        return lookup.get(label);
    }

}
