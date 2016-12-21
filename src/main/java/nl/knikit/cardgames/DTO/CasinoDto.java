package nl.knikit.cardgames.DTO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

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

//	@Autowired
//	private ModelMapperUtil mapUtil;
	
	public CasinoDto() {
	}
	
	// Casino has 5 fields, CasinoDto has 1 more
	// discard lombok setter for this field -> make your own
	@Setter(AccessLevel.NONE)
	private String name; // extra field
	private int casinoId;
	//@JsonManagedReference(value="gameDto")
	private GameDto gameDto;
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
	
	public void setName() throws Exception {
		this.name = "Name of the game";
	}
	
	public void setCardCount() {
		if (handDtos !=null) {
			this.cardCount = handDtos.size();
		} else {
			this.cardCount = 0;
		}
	}
	
	public void setHandDtos(List<HandDto> handDtos) {
		this.handDtos = handDtos;
	}
	
	public List<HandDto> getHandDtos() {
		return this.handDtos;
	}
	
}
	
