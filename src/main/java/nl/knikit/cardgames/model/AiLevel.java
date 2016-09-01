package nl.knikit.cardgames.model;

import java.util.EnumSet;
import java.util.Set;

/**
 * <H1>AIlevel</H1> Artificial Intelligence level for simulation human-like intelligence to
 * {@link Player Player}s that are bots. AiLevel is an enum
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
public enum AiLevel {

    LOW("Dumb", "Dom"), MEDIUM("Avarage", "Gemiddeld"), HIGH("High", "Hoog"), HUMAN("Human",
            "Mens"), NONE("None","Geen");

    String englishName;
    String dutchName;

    /**
     * A list of all the Enums in the class. The list is created via Set implementation EnumSet.
     * <p>EnumSet is an abstract class so new() operator does not work. EnumSet has several static
     * factory methods for creating an instance like creating groups from enums.
     * Here it is used to group all enums.
     */
    public static Set<AiLevel> aiLevels = EnumSet.of(LOW, MEDIUM, HIGH, HUMAN);

    // Constructor, each argument to the constructor shadows one of the object's
    // fields
    AiLevel(String englishName, String dutchName) {
        this.englishName = englishName;
        this.dutchName = dutchName;
    }


    // Getters, no setters needed

    String getEnglishName() {
        return englishName;
    }

    String getDutchName() {
        return dutchName;
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
