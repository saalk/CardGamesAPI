package nl.knikit.cardgames.DTO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Relation(value = "casino", collectionRelation = "casinos")
@JsonIdentityInfo(generator = JSOGGenerator.class)
// - this annotation adds @Id to prevent chain loop
// - you could also use @JsonManagedReference and @JsonBackReference
public class CasinoDto implements Serializable {

	public CasinoDto() {
	}
	
	// Casino has 5 fields, CasinoDto has 1 more
	// discard lombok setter for this field -> make your own
	@Setter(AccessLevel.NONE)
	private String name; // extra field
	@JsonProperty(value = "playerId")
	private int casinoId;
	// private String created; to prevent setting, this is generated
	//@JsonManagedReference(value="gameDto")
	@JsonIgnore
	private GameDto gameDto;
	@JsonProperty(value = "player")
	private PlayerDto playerDto;
	private int playingOrder;
	
	@Setter(AccessLevel.NONE)
	@JsonIgnore
	private List<HandDto> handDtos;
	@Setter(AccessLevel.NONE)
	private int cardCount; // extra field
	
	public void getNameConverted(String name) {
		//
	}
	
	public void setName() {
		// "Script Joe(Human|Smart) [Elf]"
		this.name = playerDto.getAlias() + "(" + WordUtils.capitalizeFully(playerDto.getAiLevel()) + ") [" + WordUtils.capitalizeFully(playerDto.getAvatar()) + "]";
	}
	
	public void setCardCount() {
//		if (handDtos !=null) {
//			this.cardCount = handDtos.size();
//		} else {
//			this.cardCount = 0;
//		}
			this.cardCount = 0;
	}
	
	public void setHandDtos(List<HandDto> handDtos) {
		this.handDtos = handDtos;
		this.cardCount = handDtos !=null ? handDtos.size() : 0;
	}
	
	public List<HandDto> getHandDtos() {
		return this.handDtos;
	}
	
}
	

