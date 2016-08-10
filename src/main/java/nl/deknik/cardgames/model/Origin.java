package nl.deknik.cardgames.model;

/**
 * <H1>Origin</H1> What species is applicable
 * <p> There are 4 species to choose from
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */
public enum Origin {

	ELF("Elf", "Elf"), MAGICIAN("Magician", "Magier"), GOBLIN("Goblin", "Goblin"), ROMAN("Warrior", "Krijger");

	String englishName;
	String dutchName;

	// Constructor, each argument to the constructor shadows one of the object's
	// fields
	Origin(String englishName, String dutchName) {
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
