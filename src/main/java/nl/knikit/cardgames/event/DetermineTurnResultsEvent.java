package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.CardAction;
import nl.knikit.cardgames.model.CardLocation;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Hand;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.model.state.CardGameStateMachine;

import org.springframework.stereotype.Component;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DetermineTurnResultsEvent extends AbstractEvent {
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		DetermineTurnResultsEventDTO flowDTO = (DetermineTurnResultsEventDTO) eventInput[0];
		EventOutput eventOutput = null;
		
		String message = String.format("DetermineTurnResultsEvent setCurrentGame: %s", flowDTO.getCurrentGame());
		log.info(message);
		message = String.format("DetermineTurnResultsEvent CardAction: %s", flowDTO.getSuppliedCardAction());
		log.info(message);
		message = String.format("DetermineTurnResultsEvent CardLocation: %s", flowDTO.getSuppliedCardLocation());
		log.info(message);
		
		// init all the object and lists
		Game gameToCheck = flowDTO.getCurrentGame();
		Casino casinoToCheck = flowDTO.getCurrentCasino();
		
		List<Casino> casinosToCheck = flowDTO.getCurrentCasinos();
		List<Hand> handsToCheck = flowDTO.getCurrentHands();
		
		message = String.format("DetermineTurnResultsEvent handsToCheck: %s", handsToCheck);
		log.info(message);
		
		// determine raise and set bet
		try {
			
			// UPDATE CURRENT TURN
			if (flowDTO.getSuppliedCardAction() == CardAction.DEAL) {
				flowDTO.setNewCurrentTurn(1);
			} else if (flowDTO.getSuppliedCardAction() == CardAction.HIGHER ||
					    flowDTO.getSuppliedCardAction() == CardAction.LOWER) {
				flowDTO.setNewCurrentTurn(casinoToCheck.getActiveTurn() + 1);
			} else {
				// PASS
				flowDTO.setNewCurrentTurn(casinoToCheck.getActiveTurn());
			}
			
			message = String.format("DetermineTurnResultsEvent setNewCurrentTurn is: %s", flowDTO.getNewCurrentTurn());
			log.info(message);
			
			// DOUBLE OR NOTHING
			int raiseFactor;
			if (flowDTO.getNewCurrentTurn() > 2) {
				raiseFactor = (int) Math.pow(2, flowDTO.getNewCurrentTurn() - 1);
			} else {
				raiseFactor = flowDTO.getNewCurrentTurn();
			}
			int raise = raiseFactor * flowDTO.getCurrentGame().getAnte();
			
			if (flowDTO.getSuppliedCardAction() == CardAction.DEAL) {
				flowDTO.setNewBet(raise);
				
				message = String.format("DetermineTurnResultsEvent deal so only new bet and turn and raise: %s", raise);
				log.info(message);
				
				eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
				return eventOutput;
			}
			
			
			// IS 2nd CARD HIGHER? -> NOT FOR PASS
			boolean won = true;
			if (flowDTO.getSuppliedCardAction() == CardAction.HIGHER ||
					    flowDTO.getSuppliedCardAction() == CardAction.LOWER) {
				
				boolean higher = false;
				Card lastCard = handsToCheck.get(handsToCheck.size() - 1).getCard();
				
				message = String.format("DetermineTurnResultsEvent lastCard: %s", lastCard);
				log.info(message);
				
				Card previousCard = handsToCheck.get(handsToCheck.size() - 2).getCard();
				
				message = String.format("DetermineTurnResultsEvent previousCard: %s", previousCard);
				log.info(message);
				
				if (lastCard.getRank().getValue(gameToCheck.getGameType()) < previousCard.getRank().getValue(gameToCheck.getGameType())) {
					higher = false; // HIGHER
				} else {
					if (lastCard.getRank().getValue(gameToCheck.getGameType()) > previousCard.getRank().getValue(gameToCheck.getGameType())) {
						higher = true; // LOWER
					} else {
						higher = true; // EQUAL
						// TODO add gamevariant logic to this
					}
				}
				// UPDATE BET
				if ((higher) && (flowDTO.getSuppliedCardAction() == CardAction.HIGHER)) {
					message = String.format("DetermineTurnResultsEvent won with raise: %s", raise);
					log.info(message);
					flowDTO.setNewBet(raise);
					won = true;
				} else {
					message = String.format("DetermineTurnResultsEvent lost with raise: %s", raise);
					log.info(message);
					flowDTO.setNewBet(-1 * raise);
					won = false;
				}
			}
			
			
			// UPDATE CUBITS WITH BET
			if (((flowDTO.getSuppliedCardAction() == CardAction.PASS) && won) || !won) {
				
				message = String.format("DetermineTurnResultsEvent update cubits: %s", flowDTO.getNewBet());
				log.info(message);
				flowDTO.setNewCubits(flowDTO.getNewBet());
				flowDTO.setCurrentPlayer(casinoToCheck.getPlayer());
				
				won = true;
			}
			
			// WHEN PASS OR LOST -> UPDATE NEW ACTIVE CASINO
			if ((flowDTO.getSuppliedCardAction() == CardAction.PASS) || !won) {
				
				boolean found2 = false;
				for (Casino casino : casinosToCheck) {
					if (found2) {
						flowDTO.setNewActiveCasino(Integer.parseInt(String.valueOf(casino.getCasinoId())));
						found2 = false;
						break;
					}
					if (casino.getCasinoId() == Integer.parseInt(flowDTO.getSuppliedCasinoId())) {
						found2 = true;
						message = String.format("DetermineTurnResultsEvent getSuppliedCasinoId found: %s", flowDTO.getSuppliedCasinoId());
						log.info(message);
					}
				}
				if (found2) {
					flowDTO.setNewActiveCasino(casinosToCheck.get(0).getCasinoId());
					message = String.format("DetermineTurnResultsEvent setNewActiveCasino : %s", flowDTO.getNewActiveCasino());
					log.info(message);
				}
			}
			
			message = String.format("DetermineTurnResultsEvent setNewBet: %s", flowDTO.getNewBet());
			log.info(message);
			
			
		} catch (Exception e) {
			message = String.format("DetermineTurnResultsEvent crash : %s", e);
			log.info(message);
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, CardGameStateMachine.Trigger.ERROR);
			return eventOutput;
		}
		
		eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
		message = String.format("DetermineTurnResultsEvent do no transition");
		log.info(message);
		
		return eventOutput;
	}
	
	public interface DetermineTurnResultsEventDTO {
		
		// all game and trigger fields
		String getSuppliedGameId();
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
		Game getCurrentGame();
		
		List<Casino> getCurrentCasinos();
		
		List<Hand> getCurrentHands();
		
		Casino getCurrentCasino();
		
		void setCurrentGame(Game game);
		
		// the rest of the supplied fields
		String getSuppliedCasinoId();
		
		CardAction getSuppliedCardAction();
		
		CardLocation getSuppliedCardLocation();
		
		// get the data created by other events
		
		void setNewCurrentTurn(int currentTurn);
		
		void setCurrentPlayer(Player player);
		
		int getNewCurrentTurn();
		
		void setNewBet(int bet);
		
		int getNewBet();
		
		void setNewActiveCasino(int activeCasino);
		
		int getNewActiveCasino();
		
		void setNewCubits(int cubits);
	}
}
