package nl.knikit.cardgames.DTO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Hand;

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
public class CasinoDto implements Serializable {
	
	public CasinoDto() {
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
	@Setter(AccessLevel.NONE)
	private String bet; // extra field
	@JsonIgnore
	private GameDto gameDto;
	
	@JsonIgnore
	//@JsonProperty(value = "visitor")
	private PlayerDto playerDto;
	
	@JsonIgnore
	private int playingOrder;
	
	@JsonIgnore
	@Setter(AccessLevel.NONE)
	//@JsonProperty(value = "cardsInHand")
	private List<HandDto> handDtos;
	@JsonIgnore
	@Setter(AccessLevel.NONE)
	private int cardCount; // extra field
	
	public void getNameConverted(String name) {
		//
	}
	
	public void setName() {
		// "Script Joe(Human|Smart) [Elf]"
		this.name = playingOrder + ": " + playerDto.getAlias() + "(" + WordUtils.capitalizeFully(playerDto.getAiLevel()) + ") [" + WordUtils.capitalizeFully(playerDto.getAvatar()) + "]";
	}
	
	public void setBet() {
		// "Script Joe(Human|Smart) [Elf]"
		if (this.playerDto != null) {
			if (this.gameDto != null) {
				this.bet = "Bet: " + this.gameDto.getAnte() + " x1 " + "(Cubits: " + playerDto.getCubits() + ") [Pawned ship: " + playerDto.getSecuredLoan() + "]";
			} else {
				this.bet = "(Cubits: " + playerDto.getCubits() + ") [Pawned ship: " + playerDto.getSecuredLoan() + "]";
			}
		} else {
			this.bet = "Bet: []";
		}
	}
	
	public void setHand() {
		if (this.handDtos != null) {
			StringBuilder sb = new StringBuilder(this.cardCount + " card(s) [");
			List<HandDto> hands;
			hands = this.handDtos;
			// sort on card order
			Collections.sort(hands, Comparator.comparing(HandDto::getCardOrder).thenComparing(HandDto::getCardOrder));
			boolean first = true;
			for (HandDto hand : hands) {
				if (!first) {
					sb.append(" ");
				}
				first = false;
				sb.append(hand.getCardDto().getCardId());
			}
			sb.append("]");
			
			this.hand = sb.toString();
		} else {
			this.hand = "0 cards []";
		}
	}
	
	public void setCardCount() {
		if (handDtos != null) {
			this.cardCount = handDtos.size();
		} else {
			this.cardCount = 0;
		}
		this.cardCount = 0;
	}
	
	public void setHandDtos(List<HandDto> handDtos) {
		this.handDtos = handDtos;
		this.cardCount = handDtos != null ? handDtos.size() : 0;
		setHand();
	}
	
	public void setGameDto(GameDto gameDto) {
		this.gameDto = gameDto;
		setBet();
	}
	
	public List<HandDto> getHandDtos() {
		return this.handDtos;
	}
	
}
	

