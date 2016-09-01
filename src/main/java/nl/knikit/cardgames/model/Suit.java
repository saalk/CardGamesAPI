package nl.knikit.cardgames.model;

/**
 * <H1>Suit</H1>
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */
public enum Suit {
	
	/** Because enum are constants, the names of an enum type's fields are in uppercase letters.
	 * In this enum Suit the 4 constants are declared with the properties of the English and Dutch names 
	 * In addition the Joker is added
	*/
	CLUBS("♣","C","Clubs", "Klaver"),
	DIAMONDS("♦","D","Diamonds", "Ruiten"), 
	HEARTS("♥","H","Hearts", "Harten"), 
	SPADES("♠","S","Spades", "Schoppen"),
	JOKERS("⋆","J","Joker", "Joker");

	String shortSymbol;
	String shortName;
	String englishName;
	String dutchName;

	// Constructor, each argument to the constructor shadows one of the object's fields
	Suit(String shortSymbol, String shortName, String englishName, String dutchName) {
		this.shortSymbol = shortSymbol;
		this.shortName = shortName;
		this.englishName = englishName;
		this.dutchName = dutchName;
	}
	
	// Getters, no setters needed
	

	String getShortSymbol() {
		return shortSymbol; 
	}
	
	String getShortName() {
		return shortName; 
	}
	
	String getEnglishName() {
		return englishName; 
	}
	
	String getDutchName() {
		return dutchName; 
	}
	
	@Override
	public String toString() {
		return ""+shortName;
	}

};
