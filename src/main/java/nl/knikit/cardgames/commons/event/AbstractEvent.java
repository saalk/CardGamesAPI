package nl.knikit.cardgames.commons.event;

import nl.knikit.cardgames.model.state.CardGameStateMachine.Trigger;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractEvent {

    @Getter
    protected List<AbstractValidation> validations = new ArrayList<>();

    protected abstract EventOutput execution(Object... eventOutput);

    public EventOutput fireEvent(Object... eventOutput) {
        checkValidation(eventOutput);
        return execution(eventOutput);
    }

    public void fireChainedEvent(AbstractFlowDTO dto) {
        checkValidation(dto);
        final EventOutput result = execution(dto);
        
        String message = String.format("AbstractEvent fireChainedEvent is dto is: %s", dto);
        log.info(message);
        
        AbstractEvent next = dto.getNextInFlow();
        if (result.isSuccess()) {
            if(result.getTrigger() != null) {
                dto.transition(result.getTrigger());
            } else {
                //dto.transition(Trigger.OK);
            }
            if (next != null) {
                message = String.format("AbstractEvent fireChainedEvent is next is: %s", next);
                log.info(message);
                next.fireChainedEvent(dto);
            }
        } else {
            //dto.transition(Trigger.NOT_OK);
        }
    }

    protected void checkValidation(Object... inputValue) {
        for (AbstractValidation validation : this.validations) {
            validation.validate(inputValue);
        }
    }

}
