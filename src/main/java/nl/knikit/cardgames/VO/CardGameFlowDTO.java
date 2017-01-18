package nl.knikit.cardgames.VO;

import nl.knikit.cardgames.commons.event.AbstractFlowDTO;
import nl.knikit.cardgames.event.CreateCasinoForGameAndPlayerEvent;
import nl.knikit.cardgames.event.CreateDeckForGameEvent;
import nl.knikit.cardgames.event.CreateHandForCasinoForGameAndPlayerEvent;
import nl.knikit.cardgames.event.CreatePlayerEvent;
import nl.knikit.cardgames.event.DeleteCardGameEvent;
import nl.knikit.cardgames.event.DeleteCasinoForGameAndPlayerEvent;
import nl.knikit.cardgames.event.GetCardGameDetailsEvent;
import nl.knikit.cardgames.event.UpdateCardGameDetailsEvent;
import nl.knikit.cardgames.event.UpdateCasinoForGameAndPlayerEvent;
import nl.knikit.cardgames.event.UpdateDeckForGameAndPlayerEvent;
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
				CreateHandForCasinoForGameAndPlayerEvent.CreateHandForCasinoForGameAndPlayerEventDTO,
				CreatePlayerEvent.CreatePlayerEventDTO,
				DeleteCardGameEvent.DeleteCardGameEventDTO,
				DeleteCasinoForGameAndPlayerEvent.DeleteCasinoForGameAndPlayerEventDTO,
				GetCardGameDetailsEvent.GetCardGameDetailsEventDTO,
				UpdateCardGameDetailsEvent.UpdateCardGameDetailsEventDTO,
				UpdateCasinoForGameAndPlayerEvent.UpdateCasinoForGameAndPlayerEventDTO,
				UpdateDeckForGameAndPlayerEvent.UpdateDeckForGameAndPlayerEventDTO,
				UpdatePlayerDetailsEvent.UpdatePlayerDetailsEventDTO {
	
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
	
	private String suppliedResource;
	private String suppliedResourceId;
	private String suppliedExtraResource;
	
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
	
	// process all frontend data to the fields above
	
	public void processPathAndQueryParamsAndTrigger(Map<String, String> pathAndQueryData, CardGameStateMachine.Trigger trigger) {
		if (pathAndQueryData != null || !pathAndQueryData.isEmpty()) {
			
			String message = String.format("CardGameFlowDTO processPathAndQueryParamsAndTrigger is: %s", pathAndQueryData);
			log.info(message);
			
			// pass pathid's to the Flow
			if (pathAndQueryData.containsKey("gameId")) {
				this.suppliedGameId = pathAndQueryData.get("gameId");
			}
			if (pathAndQueryData.containsKey("playerId")) {
				this.suppliedPlayerId = pathAndQueryData.get("playerId");
			}
			if (pathAndQueryData.containsKey("humanOrAi")) {
				this.suppliedHumanOrAi = pathAndQueryData.get("humanOrAi");
			}
			
			// pass queryParam's to the Flow
			if (pathAndQueryData.containsKey("gameType")) {
				this.suppliedGameType = GameType.fromLabel(pathAndQueryData.get("gameType"));
				
				message = String.format("CardGameFlowDTO gameType in path is: %s", pathAndQueryData.get("gameType"));
				log.info(message);
				
				message = String.format("CardGameFlowDTO gameType from Label is: %s", GameType.fromLabel(pathAndQueryData.get("gameType")));
				log.info(message);
				
				message = String.format("CardGameFlowDTO gameType stored is: %s", this.suppliedGameType);
				log.info(message);
			}
			if (pathAndQueryData.containsKey("ante")) {
				this.suppliedAnte = pathAndQueryData.get("ante");
			}
			if (pathAndQueryData.containsKey("alias")) {
				this.suppliedAlias = pathAndQueryData.get("alias");
			}
			if (pathAndQueryData.containsKey("avatar")) {
				this.suppliedAvatar = Avatar.fromLabel(pathAndQueryData.get("avatar"));
			}
			if (pathAndQueryData.containsKey("aiLevel")) {
				this.suppliedAiLevel = AiLevel.fromLabel(pathAndQueryData.get("aiLevel"));
			}
			if (pathAndQueryData.containsKey("securedLoan")) {
				this.suppliedSecuredLoan = pathAndQueryData.get("securedLoan");
			}
			
			
			if (pathAndQueryData.containsKey("jokers")) {
				this.suppliedJokers = pathAndQueryData.get("jokers");
			}
			if (pathAndQueryData.containsKey("playingOrder")) {
				this.suppliedPlayingOrder = pathAndQueryData.get("playingOrder");
			}
			if (pathAndQueryData.containsKey("action")) {
				this.suppliedAction = pathAndQueryData.get("action");
			}
			if (pathAndQueryData.containsKey("resource")) {
				this.suppliedResource = pathAndQueryData.get("resource");
			}
			if (pathAndQueryData.containsKey("resourceId")) {
				this.suppliedResourceId = pathAndQueryData.get("resourceId");
			}
			if (pathAndQueryData.containsKey("extraResource")) {
				this.suppliedExtraResource = pathAndQueryData.get("extraResource");
			}
		}
		if (trigger != null) {
			this.suppliedTrigger = trigger;
		}
	}
	
	// processing related
	private Game currentGame;
	private Player currentPlayer;
	private Deck currentDeck;
	private Card currentCard;
	private Hand currentHand;
	private Casino currentCasino;
	
	// lists of all the 6 entities, no setter?: that is done in the EventDTO's
	private List<Game> games;
	private List<Player> players;
	private List<Deck> decks;
	private List<Card> cards;
	private List<Hand> hands;
	private List<Casino> casinos;
	
	// business rules related
	private List<Game> abandonedGames;
	
	// return fields
	private Integer rulesCode;
	
	public void setContextByGame() {
		setGameContext(currentGame);
	}
	
	public void setGameByContext() {
		setCurrentGame(getGameContext());
		setSuppliedGameId(String.valueOf(currentGame.getGameId()));
	}
	
	@Override
	public List<Deck> getDecks() {
		if (decks == null) {
			decks = new ArrayList<>();
		}
		return decks;
	}
	
	@Override
	public List<Casino> getCasinos() {
		if (casinos == null) {
			casinos = new ArrayList<>();
		}
		return casinos;
	}
	
	@Override
	public List<Hand> getHands() {
		if (hands == null) {
			hands = new ArrayList<>();
		}
		return hands;
	}
	
	@Override
	public void setCurrentGame(Game game) {
		this.currentGame = game;
		setContextByGame();
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
	public void setCurrentCard(Card card) {
		this.currentCard = card;
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
		public void setSuppliedCardId(String cardId) {
		this.suppliedCardId = cardId;
	}
	
	@Override
	public void setSuppliedDeckId(String deckId) {
		this.suppliedDeckId = deckId;
	}
	
	@Override
	public String getSuppliedGameId() {
		return this.suppliedGameId;
	}
	
	
}