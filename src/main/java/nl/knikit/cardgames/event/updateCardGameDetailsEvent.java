package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.model.state.CardGameStateMachine;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class updateCardGameDetailsEvent extends AbstractEvent {

    
    @Override
    protected EventOutput execution(final Object... eventInput) {
    
        updateCardGameDetailsEventDTO flowDTO = (updateCardGameDetailsEventDTO) eventInput[0];

        // get the game and update the gametype and ante
        
	    // set a trigger for EventOutput to trigger a transition in the state machine
        EventOutput eventOutput = new EventOutput(EventOutput.Result.SUCCESS, flowDTO.getSuppliedTrigger());
        return eventOutput;
    }

    public interface updateCardGameDetailsEventDTO {
        
        void setCurrentGame(Game game);
        String getSuppliedGameId();
        String getSuppliedGameType();
        String getSuppliedAnte();
	
	    void setCurrentPlayer(Player player);
	    String getSuppliedAlias();
	    String getSuppliedAvatar();
	    String getSuppliedSecuredLoan();
	    String getSuppliedAiLevel();
	    
	    String getSuppliedPlayingOrder();
	    
	    CardGameStateMachine.Trigger getSuppliedTrigger();
	
    }
}
