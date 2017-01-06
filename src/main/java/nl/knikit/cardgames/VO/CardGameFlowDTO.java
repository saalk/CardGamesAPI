package nl.knikit.cardgames.VO;

import nl.knikit.cardgames.commons.businessrules.rules.Rule;
import nl.knikit.cardgames.commons.event.AbstractFlowDTO;
import nl.knikit.cardgames.event.GetCardGameEvent;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.response.CardGameResponse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardGameFlowDTO extends AbstractFlowDTO implements
		GetCardGameEvent.GetCardGameEventDTO {
	
	private CardGameResponse response;
	
	/**
	 * Builds the API response
	 *
	 * @return the built response
	 */
	public CardGameResponse getResponse() {
		
		this.response.setCardGameId(gameId);
		
		if (!getCasinos().isEmpty()) {
			this.response.setCasinos(null);
		}
		
		return this.response;
	}
	
	
	@Setter(AccessLevel.NONE)
	private String applicationId = "001";
	
	private String gameId;
	
	private String playerId;
	private String casinoId;
	
	private List<Casino> casinos;
	
	private MultivaluedMap<String, String> cardGameFilter;
	
	// business rules related
	private CardGame lastCardGame;
	@Setter(AccessLevel.NONE)
	private List<Game> pendingGames;
	@Setter(AccessLevel.NONE)
	private Map<Rule, Object> rulesMap;
	
	// return fields
	private String verificationStatus;
	@Setter(AccessLevel.NONE)
	private Integer rulesCode;
	
	public List<Game> getPendingGames() {
		if (pendingGames == null) {
			pendingGames = new ArrayList<>();
		}
		return pendingGames;
	}
	
	public Map<Rule, Object> getRulesMap() {
		if (rulesMap == null) {
			rulesMap = new LinkedHashMap<>();
		}
		return rulesMap;
	}
	
	public MultivaluedMap<String, String> getCardGameFilter() {
		if (cardGameFilter == null) {
			cardGameFilter = new MultivaluedHashMap<>();
		}
		return cardGameFilter;
	}
	
	public List<Casino> getCasinos() {
		if (casinos == null) {
			casinos = new ArrayList<>();
		}
		return casinos;
	}
}