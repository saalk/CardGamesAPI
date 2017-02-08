package nl.knikit.cardgames.VO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Relation(value = "casino", collectionRelation = "casinos")
//@JsonIdentityInfo(generator = JSOGGenerator.class)
// - this annotation adds @Id to prevent chain loop
// - you could also use @JsonManagedReference and @JsonBackReference
public class CardGameCasino implements Serializable {
	
	public CardGameCasino() {
	}
	
	// Casino has 5 fields, CasinoDto has 1 more
	// discard lombok setter for this field -> make your own
	@Setter(AccessLevel.NONE)
	private String name; // extra field
	@JsonProperty(value = "hand")
	@Setter(AccessLevel.NONE)
	private String hand; // extra field
	@JsonProperty(value = "playerId")
	private int casinoId;
	// private String created; to prevent setting, this is generated
	//@JsonManagedReference(value="gameDto")
	
	@JsonIgnore
	private CardGame cardGame;
	
	//@JsonIgnore
	@JsonProperty(value = "visitor")
	private CardGamePlayer cardGamePlayer;
	
	private int playingOrder;
	
	@Setter(AccessLevel.NONE)
	private String balance; // extra field
	
	private int bet; // extra field
	
	private int activeTurn;
	
	@JsonIgnore
	@Setter(AccessLevel.NONE)
	//@JsonProperty(value = "cardsInHand")
	private List<CardGameHand> cardGameHands;
	
	@JsonIgnore
	@Setter(AccessLevel.NONE)
	private int cardCount; // extra field
	
	public void setName() {
		// "Script Joe(Human|Smart) [Elf]"
		//this.name = playingOrder + ": " + cardGamePlayer.getAlias() + "(" + WordUtils.capitalizeFully(cardGamePlayer.getAiLevel()) + ") [" + WordUtils.capitalizeFully(cardGamePlayer.getAvatar()) + "]";
		this.name = cardGamePlayer.getAlias() + " [" + WordUtils.capitalizeFully(cardGamePlayer.getAiLevel()) + "]";
	}
	
	public void setBalance() {
		// "Script Joe(Human|Smart) [Elf]"
		int raise = 0;
		if (this.activeTurn > 2) {
			raise = (int) Math.pow(2, activeTurn - 1);
		} else {
			raise = this.activeTurn;
		}
		if (this.cardGamePlayer != null) {
			if (this.cardGame != null) {
				this.balance = "Bet: " + this.cardGame.getAnte() + "x" + raise + " (Cubits: " + cardGamePlayer.getCubits() + ") [Pawned ship: " + cardGamePlayer.getSecuredLoan() + "]";
			} else {
				this.balance = "(Cubits: " + cardGamePlayer.getCubits() + ") [Pawned ship: " + cardGamePlayer.getSecuredLoan() + "]";
			}
		} else {
			this.balance = "Bet: []";
		}
	}
	
	public void setHand() {
		if (this.cardGameHands != null) {
			StringBuilder sb = new StringBuilder(this.cardCount + " card(s) [");
			List<CardGameHand> hands;
			hands = this.cardGameHands;
			// sort on card order
			Collections.sort(hands, Comparator.comparing(CardGameHand::getCardOrder).thenComparing(CardGameHand::getCardOrder));
			boolean first = true;
			int round = 1;
			for (CardGameHand hand : hands) {
				if (round != hand.getRound()) {
					sb.append("] ");
					sb.append(hand.getRound());
					sb.append("[");
					round++;
				}
				if (!first) {
					sb.append(" ");
				}
				first = false;
				sb.append(hand.getCardGameCard().getCardId());
			}
			sb.append("]");
			
			this.hand = sb.toString();
		} else {
			this.hand = "0 cards []";
		}
	}
	
	public void setCardCount() {
		if (cardGameHands != null) {
			this.cardCount = cardGameHands.size();
		} else {
			this.cardCount = 0;
		}
		this.cardCount = 0;
	}
	
	public void setCardGameHands(List<CardGameHand> handDtos) {
		this.cardGameHands = handDtos;
		this.cardCount = handDtos != null ? handDtos.size() : 0;
		setHand();
	}
	
	public void setCardGame(CardGame gameDto) {
		this.cardGame = gameDto;
		setBalance();
	}
	
	public List<CardGameHand> getCardGameHands() {
		return this.cardGameHands;
	}
	
}
	

