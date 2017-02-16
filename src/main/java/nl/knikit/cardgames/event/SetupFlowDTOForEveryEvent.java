package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Hand;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.model.state.CardGameStateMachine;
import nl.knikit.cardgames.service.ICasinoService;
import nl.knikit.cardgames.service.IDeckService;
import nl.knikit.cardgames.service.IGameService;
import nl.knikit.cardgames.service.IHandService;
import nl.knikit.cardgames.service.IPlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SetupFlowDTOForEveryEvent extends AbstractEvent {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IGameService gameService;
	
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private ICasinoService casinoService;
//
//	@Autowired
//	private IHandService handService;
//
//	@Autowired
//	private IDeckService deckService;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		SetupFlowDTOForEveryEventDTO flowDTO = (SetupFlowDTOForEveryEventDTO) eventInput[0];
		EventOutput eventOutput;
		String message;
		
		message = String.format("SetupFlowDTOForEveryEvent getSuppliedGameId id : %s", flowDTO.getSuppliedGameId());
		log.info(message);
		
		// save the game and decks and casinos sorted in flow DTO if supplied
		if ((flowDTO.getSuppliedGameId() != null) && (Integer.parseInt(flowDTO.getSuppliedGameId()) > 0)) {
			try {
				
				// game
				flowDTO.setCurrentGame(gameService.findOne(Integer.parseInt(flowDTO.getSuppliedGameId())));
				
				message = String.format("SetupFlowDTOForEveryEvent game found: %s", flowDTO.getCurrentGame());
				log.info(message);
				
				if (flowDTO.getCurrentGame() == null) {
					eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
					return eventOutput;
				}
				
				// decks
				List<Deck> unsortedDecks = flowDTO.getCurrentGame().getDecks();
				
				message = String.format("SetupFlowDTOForEveryEvent decks unsortedDecks: %s", unsortedDecks);
				log.info(message);
				
				if (unsortedDecks != null && !unsortedDecks.isEmpty()) {
					Collections.sort(unsortedDecks, Comparator.comparing(Deck::getCardOrder).thenComparing(Deck::getCardOrder));
					flowDTO.setCurrentDecks(unsortedDecks);
					
					message = String.format("SetupFlowDTOForEveryEvent decks found: %s", flowDTO.getCurrentDecks());
					log.info(message);
				}
					
				// casinos
				List<Casino> unsortedCasinos = flowDTO.getCurrentGame().getCasinos();
				
				message = String.format("SetupFlowDTOForEveryEvent decks unsortedCasinos: %s", unsortedCasinos);
				log.info(message);
				
				if (unsortedCasinos != null && !unsortedCasinos.isEmpty()) {
					Collections.sort(unsortedCasinos, Comparator.comparing(Casino::getPlayingOrder).thenComparing(Casino::getPlayingOrder));
					flowDTO.setCurrentCasinos(unsortedCasinos);
					
					message = String.format("SetupFlowDTOForEveryEvent casinos found: %s", flowDTO.getCurrentCasinos());
					log.info(message);
				}
				
			} catch (Exception e) {
				message = String.format("SetupFlowDTOForEveryEvent error game decks or casinos: %s", e);
				log.info(message);
				
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		}
		
		// save the casino and hands sorted in flow DTO if supplied
		if ((flowDTO.getSuppliedCasinoId() != null) && (Integer.parseInt(flowDTO.getSuppliedCasinoId()) > 0)) {
			try {
				
				flowDTO.setCurrentCasino(casinoService.findOne(Integer.parseInt(flowDTO.getSuppliedCasinoId())));
				if (flowDTO.getCurrentCasino() == null) {
					eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
					return eventOutput;
				}
				
				// hands
				List<Hand> unsortedHands = flowDTO.getCurrentCasino().getHands();
				Collections.sort(unsortedHands, Comparator.comparing(Hand::getCardOrder).thenComparing(Hand::getCardOrder));
				flowDTO.setCurrentHands(unsortedHands);
				message = String.format("SetupFlowDTOForEveryEvent hands: %s", flowDTO.getCurrentHands());
				log.info(message);
				
			} catch (Exception e) {
				message = String.format("SetupFlowDTOForEveryEvent error casino or hands: %s", e);
				log.info(message);
				
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		}
		
		// save the player in flow DTO if supplied
		if ((flowDTO.getSuppliedPlayerId() != null) && (Integer.parseInt(flowDTO.getSuppliedPlayerId()) > 0)) {
			try {
				flowDTO.setCurrentPlayer(playerService.findOne(Integer.parseInt(flowDTO.getSuppliedPlayerId())));
				if (flowDTO.getCurrentPlayer() == null) {
					eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
					return eventOutput;
				}
				message = String.format("SetupFlowDTOForEveryEvent player: %s", flowDTO.getCurrentPlayer());
				log.info(message);
			} catch (Exception e) {
				message = String.format("SetupFlowDTOForEveryEvent error player: %s", e);
				log.info(message);
				
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		}
		eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
		message = String.format("SetupFlowDTOForEveryEvent success: %s", flowDTO.getSuppliedTrigger());
		log.info(message);
		return eventOutput;
	}
	
	public interface SetupFlowDTOForEveryEventDTO {
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
		// all supplied fields
		String getSuppliedGameId();
		
		String getSuppliedCasinoId();
		
		String getSuppliedPlayerId();
		
		// all the retrieved data
		Game getCurrentGame();
		
		void setCurrentGame(Game game);
		
		List<Deck> getCurrentDecks();
		
		void setCurrentDecks(List<Deck> decks);
		
		List<Casino> getCurrentCasinos();
		
		void setCurrentCasinos(List<Casino> casinos);
		
		Casino getCurrentCasino();
		
		void setCurrentCasino(Casino casino);
		
		List<Hand> getCurrentHands();
		
		void setCurrentHands(List<Hand> hands);
		
		Player getCurrentPlayer();
		
		void setCurrentPlayer(Player player);
		
	}
}
