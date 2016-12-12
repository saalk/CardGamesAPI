package nl.knikit.cardgames.DTO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Deck;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Relation(value = "deck", collectionRelation = "decks")
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class DeckDto implements Serializable {
	
	// Game has 5 fields, GameDto has 1 more
	
	// discard lombok setter for this field -> make your own
	@Setter(AccessLevel.NONE)
	// discard lombok setter for this field -> make your own
	private String name; // eztra field
	// "(03) 10C  Ten of Clubs"
	// "       *  40 cards left
	// "---- ---  -------------
	// "(01)  AS+ Script Joe [ELF]-"
	// "(02)  RJ  Script Joe [ELF]"
	private int deckId;
	//@JsonManagedReference(value="gameDto")
	private GameDto gameDto;
	private CardDto cardDto;
	private int cardOrder;
	private PlayerDto dealtToDto;
	
	public DeckDto() {
	}
	
	public Deck getNameConverted(String name) {
		// "10C  Ten of Clubs"
		// " AS+ Script Joe [ELF]-"
		String[] splitName = StringUtils.split(StringUtils.replace(StringUtils.replace(name, " of ", ";"), " ", ";"), ";");
		
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
	
	public void setName() throws Exception {
		// "10C  Ten of Clubs"
		// " AS+ Script Joe [Human]-"
		
		StringBuilder sb = new StringBuilder();
		if (cardDto ==null) {
			new Exception("CardId cannot be null in DeckDto") ;
		}
		if (this.cardOrder < 10) {
			sb.append("(0" + this.cardOrder + ") ");
		} else {
			sb.append("(" + this.cardOrder + ") ");
		}
		if (this.cardDto.getCardId().length() == 2) {
			sb.append(" ");
		}
		sb.append(this.cardDto.getCardId());
		if (this.dealtToDto == null || this.dealtToDto.getPlayerId() == 0) {
			sb.append("  " +
					          WordUtils.capitalizeFully(this.cardDto.getRank()) + " of " +
					          WordUtils.capitalizeFully(this.cardDto.getSuit()));
		} else {
			sb.append(
					"  " +
							WordUtils.capitalizeFully(this.dealtToDto.getAlias()) + " [" +
							WordUtils.capitalizeFully(this.dealtToDto.getAiLevel()) + "]");
		}
		this.name = String.valueOf(sb);
	}
}
	

