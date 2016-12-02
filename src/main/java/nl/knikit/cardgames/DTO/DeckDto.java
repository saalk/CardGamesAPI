package nl.knikit.cardgames.DTO;

import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Deck;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeckDto {
	
	@Setter(AccessLevel.NONE)
	// discard lombok setter for this field -> make your own
	private String name;
	// "(03) 10C  Ten of Clubs"
	// "       *  40 cards left
	// "---- ---  -------------
	// "(01)  AS+ Script Joe [ELF]-"
	// "(02)  RJ  Script Joe [ELF]"
	
	private int deckId;
	private GameDto gameDto;
	private CardDto cardDto;
	private int cardOrder;
	private PlayerDto dealtTo;
	
	public Deck getNameConverted(String name) {
		// "10C  Ten of Clubs"
		// " AS+ Script Joe [ELF]-"
		String[] splitName = StringUtils.split(StringUtils.replace(StringUtils.replace(name, " of ",";"), " ",";"), ";");
		
		if (splitName.length != 3 ||
				    splitName[0].isEmpty() || splitName[1].isEmpty() || splitName[2].isEmpty()) {
			Deck newDeck = new Deck();
			newDeck.setDeckId(0);
			newDeck.setCard(new Card("AS"));
			return newDeck;
		}
		Deck newDeck = new Deck();
		newDeck.setCard(new Card(splitName[0]));
		return newDeck;
	}
	
	public void setName() {
		// "10C  Ten of Clubs"
		// " AS+ Script Joe [Human]-"
		if (this.dealtTo == null) {
			this.name = this.cardDto.getCardId() + "  " +
					            this.cardDto.getRank() + " of " + this.cardDto.getSuit();
		} else {
			this.name = this.cardDto.getCardId() + "  " +
					            this.dealtTo.getAlias() + " [" + this.dealtTo.getAiLevel() + "]";
			
		}
	}
	
}
