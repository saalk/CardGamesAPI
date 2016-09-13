/**
 *
 */
package nl.knikit.cardgames.model;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

import nl.knikit.cardgames.utils.NameGenerator;

/**
 * <H1>PlayerOld</H1>
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */

public class PlayerOld {

    /**
     * startId static maken ivm onthouden ophogen in constructor
     */
    private static int startId = 0;

    /**
     * id is final, initialization in constructor and no setter
     */
    private int id;
    private String alias;
    private boolean isHuman;
    private AiLevel aiLevel;
    private int cubits;
    private int securedLoan;
    private Origin origin;
    private HandOld handOld;

    /**
     * @param inputOrigin human or bot
     * @param inputAiLevel dumb or smart
     * @param humanOrNot human or not
     * @throws URISyntaxException
     * @throws IOException
     */
    public PlayerOld(Origin inputOrigin, AiLevel inputAiLevel, boolean humanOrNot) throws IOException, URISyntaxException {

        // increment startId at the same time with one after the assignment has been made!
        this.id = startId++;
        this.origin = inputOrigin;
        this.alias = generateName(inputOrigin);
        this.isHuman = humanOrNot;
        this.aiLevel = inputAiLevel;
        this.cubits = 0;
        this.securedLoan = 0;
        this.handOld = new HandOld();
    }
    public PlayerOld() {};

    public int getId() {
        return id;
    }

    public String getAlias() {
        return alias;
    }

    public AiLevel getAiLevel() {
        return aiLevel;
    }

    public Origin getOrigin() {
        return origin;
    }

    public HandOld emptyHand() {
        return handOld = new HandOld();
    }

    public HandOld getHandOld() {
        return handOld;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public int setCubits(int cubits) {
        return this.cubits = this.cubits + cubits;
    }

    public int getCubits() {
        return cubits;
    }

    public String displayCubits() {

		/*
         * some math basics: PEMDAS (Please Excuse My Dear Aunt Sally) order P =
		 * parenthesis, E = Exponents
		 * 
		 * modulus = 22 % 6 = 4 because 22 / 6 = 3 with a remainder of 4
		 * 
		 * When dividing an integer by an integer, the answer will be an integer
		 * (not rounded) Integer division 8 / 5 = 1
		 * 
		 * be aware int a, b; double c; b = 21; a = 5; c = b/a => gives double
		 * 4.0 corrections: c = (double) b/a but do not: c = (double) (b/a)
		 * since integer division is done first.
		 */

        StringBuilder displayCubits = new StringBuilder();

        displayCubits.append("");
        int remainder = cubits;

        if (cubits >= 10000) {
            displayCubits.append("" + (remainder / 10000) + "x[10.000]");}
        remainder = remainder % 10000;

        if (cubits >= 1000) {
            displayCubits.append("" + (remainder / 1000) + "x[1.000]");}
        remainder = remainder % 1000;

        if (cubits >= 500) {
            displayCubits.append("" + (remainder / 500) + "x[500]");}
        remainder = remainder % 500;

        if (cubits >= 100) {
            displayCubits.append("" + (remainder / 100) + "x[100]");}
        remainder = remainder % 100;

        if (remainder >= 50) {
            displayCubits.append("" + (remainder / 50) + "x[50]");}
        remainder = remainder % 50;

        if (remainder >= 20) {
            displayCubits.append("" + (remainder / 20) + "x[20]");}
        remainder = remainder % 20;

        if (remainder >= 10) {
            displayCubits.append("" + (remainder / 10) + "x[10]");}
        remainder = remainder % 10;

        if (remainder >= 5) {
            displayCubits.append("" + (remainder / 5) + "x[5]");}
        remainder = remainder % 5;

        if (remainder >= 2) {
            displayCubits.append("" + (remainder / 2) + "x[2]");}
        remainder = remainder % 2;

        if (remainder >= 1) {
            displayCubits.append("" + (remainder / 1) + "x[1]");}

        return displayCubits.toString();
    }

    public Predict addCardToHand(Card newCard) {

        Predict prediction = null;

        if (!(this.handOld.getCards().size() == 0)) {
            prediction = predictHighOrLowOrEqual(this.handOld.getLastCard(), newCard);
        }

        this.handOld.setCard(newCard);
        return prediction;
    }

    public int getSecuredLoan() {
        return securedLoan;
    }

    public void setSecuredLoan(int securedLoan) {
        this.securedLoan = securedLoan;
    }

    public enum Predict {
        HIGH, LOW, INDECISIVE, EQUAL
    }

    public Predict predictNextCard(boolean forceHighLow) {

        Predict prediction = null;
        int value = 0;
        Random rnd = new Random();
        value = this.handOld.getLastCard().getRank().getValue(CardGame.HIGHLOW);

        switch (this.aiLevel) {
            case HIGH:
                prediction = (value < 7) ? Predict.HIGH : Predict.LOW;
                prediction = (value == 7) ? Predict.HIGH : prediction;
                break;
            case MEDIUM:
                prediction = (value < 7) ? Predict.HIGH : Predict.LOW;
                // set 'dumb' behavior on 5, 6 and 8, 9
                prediction = (value == 6 || value == 5) ? Predict.LOW : prediction;
                prediction = (value == 7 || value == 8 || value == 9) ? Predict.HIGH : prediction;
                break;
            default:
                // set even 'dumber' behavior on LOW : make a random decision
                prediction = (rnd.nextBoolean() == true) ? Predict.HIGH : Predict.HIGH;
                break;
        }

        if (!forceHighLow) {
            // stop 1 out of 5 times
            boolean pass = false;
            pass = (rnd.nextInt(5) == 0) ? true : false;
            prediction = pass ? Predict.INDECISIVE : prediction;
        }

        return prediction;
    }

    private Predict predictHighOrLowOrEqual(Card lastCard, Card inputCard) {
        if (lastCard.getRank().getValue(CardGame.HIGHLOW) < inputCard.getRank().getValue(CardGame.HIGHLOW)) {
            return Predict.HIGH; // new card is higher
        } else {
            if (lastCard.getRank().getValue(CardGame.HIGHLOW) > inputCard.getRank().getValue(CardGame.HIGHLOW)) {
                return Predict.LOW; // new card is lower
            } else {
                return Predict.EQUAL; // new card is equal
            }
        }
    }

    // Generate a name
    public String generateName(Origin inputOrigin) throws IOException, URISyntaxException {

        // Don't fiddle with relative paths in java.io.File. They are dependent
        // on the current working directory over which you have totally no
        // control from inside the Java code.

        /*
        String inputOriginFile = "" + inputOrigin
                .getEnglishName() + "Names" +
                ".txt";

        NameGenerator namegenerator;
        namegenerator = new NameGenerator(inputOriginFile);
        int syls;
        Random random = new Random();

        syls = random.nextInt(2) + 2;
        namegenerator.compose(syls);
        String generatedName = namegenerator.compose(syls);
        /*

         */
        return "Alias"+startId;
    }

    public String toString() {

        StringBuilder displayPlayer = new StringBuilder();
        displayPlayer.append("" + alias);

        return displayPlayer.toString();
    }

    public String toStringPlayers() {
        // auto display toString first
        StringBuilder displayPlayer = new StringBuilder();
        displayPlayer.append(displayCubits());
        displayPlayer.append(System.lineSeparator());
        displayPlayer.append("  > Cards : " + handOld);

        return displayPlayer.toString();
    }

}
