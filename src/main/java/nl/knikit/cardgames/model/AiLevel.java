package nl.knikit.cardgames.model;

import nl.knikit.cardgames.model.enumlabel.LabeledEnum;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.ToString;

/**
 * <H1>AIlevel</H1> Artificial Intelligence level for simulation human-like intelligence to
 * {@link Player}s that are bots. AiLevel is an enum
 * since the levels are a predefined list of values.
 * AIlevels that can be used are
 * <ul>
 * <li> {@link #HIGH}
 * <li> {@link #MEDIUM}
 * <li> {@link #LOW}
 * <li> {@link #HUMAN}
 * <li> {@link #NONE}
 * </ul>
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */

@Getter
@ToString
public enum AiLevel implements LabeledEnum {

    @Column(name = "AI_LEVEL", length = 10, nullable = false)
    LOW("Low"), MEDIUM("Medium"), HIGH("High"), HUMAN("Human"), NONE("None");

    /**
     * A static HashMap lookup with key + value is created to use in a getter
     * to fromLabel the Enum based on the name eg. key "Low" -> value AiLevel.LOW
     */
    private static final Map<String,AiLevel> lookup
            = new HashMap<>();
    static {
        for(AiLevel aiLevel : EnumSet.allOf(AiLevel.class))
            lookup.put(aiLevel.getLabel(), aiLevel);
    }
    private String label;

    AiLevel(){

    }
    AiLevel(String label) {
        this();
        this.label = label;
    }

    public static AiLevel fromAiLevelName(String label) {
        return lookup.get(label);
    }

    /**
     * A list of all the Enums in the class. The list is created via Set implementation EnumSet.
     * <p>EnumSet is an abstract class so new() operator does not work. EnumSet has several static
     * factory methods for creating an instance like creating groups from enums.
     * Here it is used to group all enums.
     */
    public static Set<AiLevel> aiLevels = EnumSet.of(LOW, MEDIUM, HIGH, HUMAN, NONE);

    /*
     * Using @Override annotation while overriding method in Java is one of the
	 * best practice in Java.
     */
    @Override
    public String toString() {
        return "" + label;
    }
}
