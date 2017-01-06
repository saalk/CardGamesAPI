package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.model.Casino;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetCardGameEvent extends AbstractEvent {

    @Override
    protected EventOutput execution(final Object... input) {
        
        //     EventOutput eventOutput = listCardsEvent.fireEvent(flowDTO);
             EventOutput eventOutput = new EventOutput(null);
        
        if(eventOutput.isSuccess()) {
            //Casino casino = flowDTO.getCasinos().get(0);
        } else {
            return EventOutput.failure();
        }

        return EventOutput.success();
    }

    public interface GetCardGameEventDTO {
        List<Casino> getCasinos();
    }
}
