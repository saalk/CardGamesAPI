/**
 *
 */
package nl.knikit.cardgames.model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;

/**
 * <H1>Player2</H1>
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */

@Getter
@Setter
public class Player2 {

    /**
     * startId static maken ivm onthouden ophogen in constructor
     */
    private static int startId = 0;


    /**
     * id is final, initialization in constructor and no setter
     */
    private int id;
    private Origin origin;
    private String alias;
    private boolean isHuman;
    private AiLevel aiLevel;
    private int cubits;
    private int securedLoan;


    public Player2(int id, Origin origin, String alias, boolean isHuman, AiLevel aiLevel, int
            cubits, int securedLoan) {
        this.id = id;
        this.origin = origin;
        this.alias = alias;
        this.isHuman = isHuman;
        this.aiLevel = aiLevel;
        this.cubits = cubits;
        this.securedLoan = securedLoan;

    }

    public int getId() {
        return id;
    }

    public void setId() {
        this.id = startId++;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public void setHuman(boolean human) {
        isHuman = human;
    }

    public AiLevel getAiLevel() {
        return aiLevel;
    }

    public void setAiLevel(AiLevel aiLevel) {
        this.aiLevel = aiLevel;
    }

    public int getCubits() {
        return cubits;
    }

    public void setCubits(int cubits) {
        this.cubits = cubits;
    }

    public int getSecuredLoan() {
        return securedLoan;
    }

    public void setSecuredLoan(int securedLoan) {
        this.securedLoan = securedLoan;
    }

}
