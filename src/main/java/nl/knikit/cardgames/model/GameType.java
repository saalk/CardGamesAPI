package nl.knikit.cardgames.model;

import nl.knikit.cardgames.model.enumlabel.LabeledEnum;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
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
public enum GameType implements LabeledEnum {

    /**
     * HIGHLOW cardgame is a simple higher or lower guessing game. The dealer places one card
     * face-down in front of the player, then another card face-up for the players Hand. The player
     * guesses whether the value of the face-down card is higher or lower. <p> The player places his
     * initial bet. The house matches that bet into the pot. When the player guesses, he wins or
     * loses the pot depending on the outcome of his guess. After that round, the player can pass
     * the bet to another player, or go double or nothing on the next bet depending on the specific
     * variant of HIGHLOW.
     */
    @Column(name = "TYPE", length = 25, nullable = false)
    HIGHLOW("Hi-Lo"), BLACKJACK("Blackjack");

    /**
     * A static HashMap lookup with key + value is created to use in a getter
     * to fromLabel the Enum based on the name eg. key "Low" -> value AiLevel.LOW
     */
    private static final Map<String,GameType> lookup
            = new HashMap<>();
    static {
        for(GameType gameType : EnumSet.allOf(GameType.class))
            lookup.put(gameType.getLabel(), gameType);
    }
    @Transient
    private String label;

    GameType(){
    }

    GameType(String label) {
        this();
        this.label = label;
    }

    public static GameType fromGameTypeLabel(String label) {
        return lookup.get(label);
    }


    @Transient
    public static Set<GameType> cardGamesListGameType = EnumSet.of(HIGHLOW);

}
