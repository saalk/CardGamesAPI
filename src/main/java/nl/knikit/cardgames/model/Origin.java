package nl.knikit.cardgames.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.ToString;

/**
 * <H1>Origin</H1> What species is applicable
 * <p> There are 4 species to choose from
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */

// Getters, no setters needed
@Getter
public enum Origin {

    @Column(name = "ORIGIN")
    ELF("Elf", "Elf"), MAGICIAN("Magician", "Magier"), GOBLIN("Goblin", "Goblin"), ROMAN("Warrior", "Krijger");

    @Transient
    String englishName;
    @Transient
    String dutchName;

    // Constructor, each argument to the constructor shadows one of the object's
    // fields
    Origin(String englishName, String dutchName) {

        this.englishName = englishName;
        this.dutchName = dutchName;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Origin [name=").append(englishName).append("]");
        return builder.toString();
    }

};
