package nl.deknik.cardgames.model;

/**
 * <H1>Rank</H1> Progressive value for a card. <p> There are thirteen ranks of each of the four
 * French suits. The rank 'Joker' is the 14th one.
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */
public enum Rank {

    /**
     * Because enum are constants, the names of an enum type's fields are in uppercase letters. In
     * this enum Rank the 13 constants are declared with various properties, in addition the Joker
     * is added
     */
    ACE("A", "Ace", "Aas"), TWO("2", "Two", "Twee"), THREE("3", "Three", "Drie"), FOUR("4", "Four", "Vier"), FIVE("5",
            "Five", "Vijf"), SIX("6", "Six", "Zes"), SEVEN("7", "Seven", "Zeven"), EIGHT("8", "Eight",
            "Acht"), NINE("9", "Nine", "Negen"), TEN("10", "Ten", "Tien"), JACK("J", "Jack", "Boer"), QUEEN("Q",
            "Queen", "Vrouw"), KING("K", "King", "Koning"), JOKER("R", "Joker", "Joker");

    String shortName;
    String englishName;
    String dutchName;

    Rank(String shortName, String englishName, String dutchName) {
        this.shortName = shortName;
        this.englishName = englishName;
        this.dutchName = dutchName;
    }

    String getShortName() {
        return this.shortName;
    }

    String getEnglishName() {
        return this.englishName;
    }

    String getdutchName() {
        return this.dutchName;
    }

    /**
     * Usually the Face cards (K,Q,J) are worth 13,12,11 points, each Aces are worth 1. But the
     * selected card game determines the playing value.
     * <p>
     * Values for {@link CardGame}:
     * 1. Vote if equal cards are a loss or correct guess (usually loss since only high low counts).
     * 2. No jokers.
     * 3. Ace is worth 1
     *
     */
    public int getValue(CardGame inputCardGame) {
        int value = 0;
        switch (inputCardGame) {
            case HIGHLOW:
                switch (this) {
                    case JOKER:
                        value = 0;
                        break;
                    case ACE:
                        value = 1;
                        break;
                    case KING:
                        value = 13;
                        break;
                    case QUEEN:
                        value = 12;
                        break;
                    case JACK:
                        value = 11;
                        break;
                    default:
                        value = Integer.parseInt(shortName);
                }
            default:
                break;
        }
        return value;
    }

    @Override
    public String toString() {
        return "" + shortName;
    }

}
