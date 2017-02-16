package nl.knikit.cardgames.VO;

import nl.knikit.cardgames.commons.event.AbstractFlowDTO;
import nl.knikit.cardgames.event.CreateCasinoForGameAndPlayerEvent;
import nl.knikit.cardgames.event.CreateDeckForGameEvent;
import nl.knikit.cardgames.event.CreateHandForCasinoForGameAndPlayerEvent;
import nl.knikit.cardgames.event.CreatePlayerEvent;
import nl.knikit.cardgames.event.DeleteCardGameEvent;
import nl.knikit.cardgames.event.DeleteCasinoForGameAndPlayerEvent;
import nl.knikit.cardgames.event.GetCardGameDetailsEvent;
import nl.knikit.cardgames.event.SetupFlowDTOForEveryEvent;
import nl.knikit.cardgames.event.UpdateCardGameDetailsEvent;
import nl.knikit.cardgames.event.UpdateCasinoForPlayingOrderEvent;
import nl.knikit.cardgames.event.UpdateCasinoForTurnAndBetEvent;
import nl.knikit.cardgames.event.UpdateDeckForGameAndCasinoEvent;
import nl.knikit.cardgames.event.UpdatePlayerCubitsAndSecuredLoanEvent;
import nl.knikit.cardgames.event.UpdatePlayerForCasinoDetailsEvent;
import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.CardAction;
import nl.knikit.cardgames.model.CardLocation;
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
				SetupFlowDTOForEveryEvent.SetupFlowDTOForEveryEventDTO,
				UpdateCardGameDetailsEvent.UpdateCardGameDetailsEventDTO,
				UpdateCasinoForPlayingOrderEvent.UpdateCasinoForPlayingOrderEventDTO,
				UpdateCasinoForTurnAndBetEvent.UpdateCasinoForTurnAndBetEventDTO,
				UpdateDeckForGameAndCasinoEvent.UpdateDeckForGameAndCasinoEventDTO,
				UpdatePlayerForCasinoDetailsEvent.UpdatePlayerForCasinoDetailsEventDTO,
				UpdatePlayerCubitsAndSecuredLoanEvent.UpdatePlayerCubitsAndSecuredLoanEventDTO {
	
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
	private String suppliedTest;
	private String suppliedPlayingOrder;
	
	private CardAction suppliedCardAction;
	private String suppliedTotal;
	private CardLocation suppliedCardLocation;
	
	// in game changes
	private int suppliedBet;
	private String suppliedCubits;
	private int suppliedCurrentRound;
	private int suppliedCurrentTurn;
	private int suppliedActiveCasino;
	
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
			if (pathAndQueryData.containsKey("casinoId")) {
				this.suppliedCasinoId = pathAndQueryData.get("casinoId");
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
			if (pathAndQueryData.containsKey("test")) {
				this.suppliedTest = pathAndQueryData.get("test");
			}
			if (pathAndQueryData.containsKey("playingOrder")) {
				this.suppliedPlayingOrder = pathAndQueryData.get("playingOrder");
			}
			if (pathAndQueryData.containsKey("cardAction")) {
				this.suppliedCardAction = CardAction.fromLabel(pathAndQueryData.get("cardAction"));
			}
			if (pathAndQueryData.containsKey("cardLocation")) {
				this.suppliedCardLocation = CardLocation.fromLabel(pathAndQueryData.get("cardLocation"));
			}
			if (pathAndQueryData.containsKey("total")) {
				this.suppliedTotal = pathAndQueryData.get("total");
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
	private List<Player> currentPlayers;
	private List<Deck> currentDecks;
	private List<Card> currentCards;
	private List<Hand> currentHands;
	private List<Casino> currentCasinos;
	
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
	public List<Deck> getCurrentDecks() {
		if (currentDecks == null) {
			currentDecks = new ArrayList<>();
		}
		return currentDecks;
	}
	
	@Override
	public List<Casino> getCurrentCasinos() {
		if (currentCasinos == null) {
			currentCasinos = new ArrayList<>();
		}
		return currentCasinos;
	}
	
	@Override
	public List<Hand> getCurrentHands() {
		if (currentHands == null) {
			currentHands = new ArrayList<>();
		}
		return currentHands;
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
	public void setSuppliedPlayerId(String playerId) {
		this.suppliedPlayerId = playerId;
	}
	
	@Override
	public void setSuppliedCasinoId(String casinoId) {
		this.suppliedCasinoId = casinoId;
	}
	
	@Override
	public String getSuppliedGameId() {
		return this.suppliedGameId;
	}
	
	
}