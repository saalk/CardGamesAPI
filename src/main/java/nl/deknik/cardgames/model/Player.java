/**
 *
 */
package nl.deknik.cardgames.model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

import nl.deknik.cardgames.utils.NameGenerator;

/**
 * <H1>Player</H1>
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */
public class Player {

    /**
     * startId static maken ivm onthouden ophogen in constructor
     */
    private static int startId = 1;

    /**
     * id is final, initialization in constructor and no setter
     */
    private int id;
    private String alias;
    private AiLevel aiLevel;
    private Origin origin;
    private Hand hand;
    private boolean isHuman;
    private int fichesValue;

    /**
     * @param inputOrigin human or bot
     * @param inputAiLevel dumb or smart
     * @param humanOrNot human or not
     * @throws URISyntaxException
     * @throws IOException
     */
    public Player(Origin inputOrigin, AiLevel inputAiLevel, boolean humanOrNot) throws IOException, URISyntaxException {

        // increment startId at the same time with one after the assingment has been made!
        this.id = startId++;

        // to do something with origin via the name generator

        if (humanOrNot == true) {
            this.alias = generateName(inputOrigin) + ("(You)");
        } else {
            this.alias = generateName(inputOrigin) + ("(" + inputAiLevel + ")");
            ;
        }
        this.aiLevel = inputAiLevel;
        this.origin = inputOrigin;
        // bugfix this was this.hand = null !
        this.hand = new Hand();
        this.isHuman = humanOrNot;
        this.fichesValue = 0;
        // startkapitaal elke player
    }

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

    public Hand emptyHand() {
        return hand = new Hand();
    }

    public Hand getHand() {
        return hand;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public int setFichesValue(int fichesValueChange) {
        return fichesValue = fichesValue + fichesValueChange;
    }

    public int getFichesValue() {
        return fichesValue;
    }

    public String displayFiches() {

		/*
         * some math basics: PEMDAS (Please Excuse My Dear Aunt Sally) order P =
		 * parenthesis, E = Exponents
		 * 
		 * modulus = 22 % 6 = 4 because 22 / 6 = 3 with a remainder of 4 When
		 * dividing an integer by an integer, the answer will be an integer (not
		 * rounded)
		 * 
		 * When dividing an integer by an integer, the answer will be an integer
		 * (not rounded) Integer division 8 / 5 = 1
		 * 
		 * be aware int a, b; double c; b = 21; a = 5; c = b/a => gives double
		 * 4.0 corrections: c = ( double) b/a but do not: c = ( double) (b/a)
		 * since integer division is done first.
		 */

        StringBuilder displayFiches = new StringBuilder();
        // displayPlayer.append("#"+id+"-");
        displayFiches.append("  " + alias);
        displayFiches.append(System.lineSeparator());
        displayFiches.append("  > Fiches: [");
        int remainder = fichesValue;

        if (fichesValue >= 100) {
            displayFiches.append((remainder / 100) + "x[100]");
            remainder = fichesValue % 100;
        } else {
            // displayFiches.append(", 100[-]");
        }
        // displayFiches.append(System.lineSeparator());
        if (remainder >= 50) {
            displayFiches.append("" + (remainder / 50) + "x[50]");
            remainder = remainder % 50;
        } else {
            // displayFiches.append(", 50[-]");
        }
        // displayFiches.append(System.lineSeparator());
        if (remainder >= 20) {
            displayFiches.append("" + (remainder / 20) + "x[20]");
            remainder = remainder % 20;
        } else {
            // displayFiches.append(", 20[-]");
        }
        // displayFiches.append(System.lineSeparator());
        if (remainder >= 10) {
            displayFiches.append("" + (remainder / 10) + "x[10]");
            remainder = remainder % 10;
        } else {
            // displayFiches.append(", 10[-]");
        }
	
		/*
		 * if (remainder >= 5) { displayFiches.append("    5 - " + (remainder /
		 * 5) + "x"); remainder = remainder % 5; } else { displayFiches.append(
		 * "    5 - " + "none"); } displayFiches.append(System.lineSeparator());
		 * if (remainder >= 2) { displayFiches.append("    2 - " + (remainder /
		 * 2) + "x"); remainder = remainder % 2; } else { displayFiches.append(
		 * "    2 - " + "none"); } displayFiches.append(System.lineSeparator());
		 * if (remainder >= 1) { displayFiches.append("    1 - " + (remainder /
		 * 1) + "x"); } else { displayFiches.append("    1 - " + "none"); }
		 */
        displayFiches.append("]");
        // displayFiches.append(System.lineSeparator());
        return displayFiches.toString();
    }

    public Predict addCardToHand(Card newCard) {

        Predict prediction = null;

        if (!(this.hand.getCards().size() == 0)) {
            prediction = predictHighOrLowOrEqual(this.hand.getLastCard(), newCard);
        }

        this.hand.setCard(newCard);
        return prediction;
    }

    public enum Predict {
        HIGH, LOW, INDECISIVE, EQUAL
    }

    ;

    public Predict predictNextCard(boolean forceHighLow) {

        Predict prediction = null;
        int value = 0;
        Random rnd = new Random();
        value = this.hand.getLastCard().getRank().getValue(CardGame.HIGHLOW);

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

        String inputOriginFile = "external/" + inputOrigin.getEnglishName() + "Names.txt";

        NameGenerator namegenerator;
        namegenerator = new NameGenerator(inputOriginFile);
        int syls;
        Random random = new Random();

        syls = random.nextInt(2) + 2;
        namegenerator.compose(syls);
        String generatedName = namegenerator.compose(syls);

        return generatedName;
    }

    public String toString() {

        StringBuilder displayPlayer = new StringBuilder();
        displayPlayer.append("" + alias);

        return displayPlayer.toString();
    }

    public String toStringPlayers() {
        // auto display toString first
        StringBuilder displayPlayer = new StringBuilder();
        displayPlayer.append(displayFiches());
        displayPlayer.append(System.lineSeparator());
        displayPlayer.append("  > Cards : " + hand);

        return displayPlayer.toString();
    }

}
