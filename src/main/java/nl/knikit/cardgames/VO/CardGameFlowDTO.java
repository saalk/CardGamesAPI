package nl.knikit.cardgames.VO;

import nl.knikit.cardgames.commons.event.AbstractFlowDTO;
import nl.knikit.cardgames.event.CreateCasinoForGameAndPlayerEvent;
import nl.knikit.cardgames.event.CreateDeckForGameEvent;
import nl.knikit.cardgames.event.CreatePlayerEvent;
import nl.knikit.cardgames.event.UpdateCardGameDetailsEvent;
import nl.knikit.cardgames.event.UpdatePlayerDetailsEvent;
import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.GameType;
import nl.knikit.cardgames.model.Hand;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.model.state.CardGameStateMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class CardGameFlowDTO extends AbstractFlowDTO implements
		CreateCasinoForGameAndPlayerEvent.CreateCasinoForGameAndPlayerEventDTO,
		CreateDeckForGameEvent.CreateDeckForGameEventDTO,
		CreatePlayerEvent.CreatePlayerEventDTO,
		UpdateCardGameDetailsEvent.UpdateCardGameDetailsEventDTO,
		UpdatePlayerDetailsEvent.UpdatePlayerDetailsEventDTO
{
	
	// suppress lombok setter for these fixed values
	@Setter(AccessLevel.NONE)
	private String applicationId = "001";
	
	// frontend path ids
	private String suppliedGameId;
	private String suppliedPlayerId;
	private String suppliedCasinoId;
	private String suppliedDeckId;
	private String suppliedHandId;
	private String suppliedCardId;
	
	// frontend query params
	private String suppliedHumanOrAi;
	private GameType suppliedGameType;
	private String suppliedAnte;
	private String suppliedAlias;
	private Avatar suppliedAvatar;
	private String suppliedSecuredLoan;
	private AiLevel suppliedAiLevel;
	private String suppliedJokers;
	private String suppliedPlayingOrder;
	private String suppliedAction;
	
	// frontend trigger
	private CardGameStateMachine.Trigger suppliedTrigger;
	
	
	// lists of all the 6 entities, no setter: that is done in the EventDTO's
	@Setter(AccessLevel.NONE)
	private List<Game> games;
	@Setter(AccessLevel.NONE)
	private List<Player> players;
	@Setter(AccessLevel.NONE)
	private List<Deck> decks;
	@Setter(AccessLevel.NONE)
	private List<Card> cards;
	@Setter(AccessLevel.NONE)
	private List<Hand> hands;
	@Setter(AccessLevel.NONE)
	private List<Casino> casinos;
	
	// input fields
	private Map<String, String> pathAndQueryParams;
	
	public void processPathAndQueryParams() {
		if (this.pathAndQueryParams == null) {
			return;
		}
		
		String message = String.format("CardGameFlowDTO processPathAndQueryParams is: %s", pathAndQueryParams);
		log.info(message);
				
		// pass pathid's to the Flow
		if (pathAndQueryParams.containsKey("gameId")) {
			this.suppliedGameId = pathAndQueryParams.get("gameId");
		}
		if (pathAndQueryParams.containsKey("playerId")) {
			this.suppliedPlayerId = pathAndQueryParams.get("playerId");
		}
		if (pathAndQueryParams.containsKey("humanOrAi")) {
			this.suppliedHumanOrAi = pathAndQueryParams.get("humanOrAi");
		}
		
		// pass queryParam's to the Flow
		if (pathAndQueryParams.containsKey("gameType")) {
			this.suppliedGameType = GameType.fromLabel(pathAndQueryParams.get("gameType"));
		}
		if (pathAndQueryParams.containsKey("ante")) {
			this.suppliedAnte = pathAndQueryParams.get("ante");
		}
		if (pathAndQueryParams.containsKey("alias")) {
			this.suppliedAlias = pathAndQueryParams.get("alias");
		}
		if (pathAndQueryParams.containsKey("avatar")) {
			this.suppliedAvatar = Avatar.fromLabel(pathAndQueryParams.get("avatar"));
		}
		if (pathAndQueryParams.containsKey("aiLevel")) {
			this.suppliedAiLevel = AiLevel.fromLabel(pathAndQueryParams.get("aiLevel"));
		}
		if (pathAndQueryParams.containsKey("securedLoan")) {
			this.suppliedSecuredLoan = pathAndQueryParams.get("securedLoan");
		}
		
		
		if (pathAndQueryParams.containsKey("jokers")) {
			this.suppliedJokers = pathAndQueryParams.get("jokers");
		}
		if (pathAndQueryParams.containsKey("playingOrder")) {
			this.suppliedPlayingOrder = pathAndQueryParams.get("playingOrder");
		}
		if (pathAndQueryParams.containsKey("action")) {
			this.suppliedAction = pathAndQueryParams.get("action");
		}
		
	}
	
	// processing related
	private Game currentGame;
	private Player currentPlayer;
	private Deck currentDeck;
	private Card cardToDeal;
	private Card cardDealt;
	private Hand currentHand;
	private Casino currentCasino;
	
	// business rules related
	@Setter(AccessLevel.NONE)
	private List<Game> abandonedGames;
	
	// return fields
	private Integer rulesCode;
	
	public Game getGame() {
		return currentGame;
	}
	
	public void setContextByGame() {
		getGameContext().setGameId(currentGame.getGameId());
		getGameContext().setCreated(currentGame.getCreated());
		getGameContext().setGameType(currentGame.getGameType());
		getGameContext().setAnte(currentGame.getAnte());
	}
	
	public List<Deck> getDecks() {
		if (decks == null) {
			decks = new ArrayList<>();
		}
		return decks;
	}
	
	public List<Casino> getCasinos() {
		if (casinos == null) {
			casinos = new ArrayList<>();
		}
		return casinos;
	}
	
	public List<Hand> getHands() {
		if (hands == null) {
			hands = new ArrayList<>();
		}
		return hands;
	}
	
	@Override
	public void setCurrentGame(Game game) {
		this.currentGame = game;
	}
	
	@Override
	public void setCurrentPlayer(Player player) {
		this.currentPlayer = player;
	}
	
	@Override
	public void setCurrentCasino(Casino casino) {
		this.currentCasino = casino;
	}
	
	@Override
	public void setSuppliedPlayerId(String playerId) {
		this.suppliedPlayerId = playerId;
	}
	
	@Override
	public void setSuppliedCasinoId(String casinoId) {
		this.suppliedCasinoId = casinoId;
	}
	
	@Override
	public String getSuppliedGameId() {
		return this.suppliedGameId ;
	}
	
	
}