package nl.knikit.cardgames.DTO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import nl.knikit.cardgames.model.Rank;
import nl.knikit.cardgames.model.Suit;

import org.springframework.hateoas.core.Relation;

import java.io.Serializable;
import java.text.ParseException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Relation(value = "card", collectionRelation = "cards")
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class CardDto implements Serializable{
	
	private String cardId;
	private String rank;
	private String suit;
	private int value;
	
	public CardDto() {
	}
	
	public Suit getSuitConvertedFromLabel(String suitLabel) throws Exception {
		Suit converted = Suit.fromLabel(suitLabel);
		if (converted==null) {
			throw new Exception("SuitParseLabelException");
		}
		return converted;
	}
	
	public void setSuit(Suit suit) {
		// static Eum methods:
		// - valueOf() - returns enum instance taking a String
		// - values()  - returns all enum instances
		// instance Enum method:
		// - name()    - returns name of enum constant
		// -> better use toString() to get the user-friendly name
		
		this.suit = (String.valueOf(suit));
	}
	
	public Rank getRankConvertedFromLabel(String rankLabel) throws Exception {
		Rank converted = Rank.fromLabel(rankLabel);
		if (converted==null) {
			throw new Exception("RankParseLabelException");
		}
		return converted;
	}
	
	public void setRank(Rank rank) {
		// static Eum methods:
		// - valueOf() - returns enum instance taking a String
		// - values()  - returns all enum instances
		// instance Enum method:
		// - name()    - returns name of enum constant
		// -> better use toString() to get the user-friendly name
		
		this.rank = (String.valueOf(rank));
	}
	
}
