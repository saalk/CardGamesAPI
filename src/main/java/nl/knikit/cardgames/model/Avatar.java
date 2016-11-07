package nl.knikit.cardgames.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.ToString;

/**
 * <H1>Avatar</H1> What species is applicable
 * <p> There are 4 species to choose from
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */

// Getters, no setters needed
@Getter
@ToString
public enum Avatar {

    @Column(name = "AVATAR", length = 25)
    ELF("Elf"), MAGICIAN("Magician"), GOBLIN("Goblin"), ROMAN("Warrior");


    /**
     * A static HashMap lookup with key + value is created to use in a getter
     * to fromName the Enum based on the name eg. key "Elf" -> value Avatar.ELF
     */
    private static final Map<String,Avatar> lookup
            = new HashMap<>();
    static {
        for(Avatar avatar : EnumSet.allOf(Avatar.class))
            lookup.put(avatar.getLabel(), avatar);
    }
    private String label;

    Avatar(){
    }

    Avatar(String label) {
        this();
        this.label = label;
    }

    public static Avatar fromAvatarName(String label) {
        return lookup.get(label);
    }

};
