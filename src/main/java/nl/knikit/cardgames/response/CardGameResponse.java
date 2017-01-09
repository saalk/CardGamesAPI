package nl.knikit.cardgames.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import nl.knikit.cardgames.VO.CardGame;
import nl.knikit.cardgames.commons.controller.ControllerResponse;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Hand;
import nl.knikit.cardgames.model.Player;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardGameResponse {
	
	@JsonIgnore
	private Reason reason;
	
	public enum Reason {
		SUCCESS, FAILURE;
	}
	
	private CardGame cardGame;
		
	private List<Deck> cardsLeftInDeck;
	
	private Card topCardInDeck;
	private List<Hand> currentPlayersHand;
	
	private Player winner;
	private Player activePlayer;
	private List<Casino> allPlayers;
	
	private String errorCode;
	private String errorReason;
	
}
	
	

