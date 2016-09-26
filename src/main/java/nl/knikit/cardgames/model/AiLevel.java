package nl.knikit.cardgames.model;

import java.util.EnumSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.ToString;

/**
 * <H1>AIlevel</H1> Artificial Intelligence level for simulation human-like intelligence to
 * {@link PlayerOld PlayerOld}s that are bots. AiLevel is an enum
 * since the levels are a predefined list of values.
 * AIlevels that can be used are
 * <ul>
 * <li> {@link #HIGH}
 * <li> {@link #MEDIUM}
 * <li> {@link #LOW}
 * <li> {@link #HUMAN}
 * </ul>
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */
@Embeddable
@Getter
@ToString
public enum AiLevel {

    @Column(name="AI_LEVEL")
    LOW("Dumb", "Dom"), MEDIUM("Average", "Gemiddeld"), HIGH("High", "Hoog"), HUMAN("Human",
            "Mens"), NONE("None","Geen");

    @Transient
    String englishName;
    @Transient
    String dutchName;

    /**
     * A list of all the Enums in the class. The list is created via Set implementation EnumSet.
     * <p>EnumSet is an abstract class so new() operator does not work. EnumSet has several static
     * factory methods for creating an instance like creating groups from enums.
     * Here it is used to group all enums.
     */
    @Transient
    public static Set<AiLevel> aiLevels = EnumSet.of(LOW, MEDIUM, HIGH, HUMAN);

    // Constructor, each argument to the constructor shadows one of the object's
    // fields
    AiLevel(String englishName, String dutchName) {
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
}
