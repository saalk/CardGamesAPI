package nl.knikit.cardgames.commons.event;

import nl.knikit.cardgames.model.state.CardGameStateMachine.Trigger;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

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
        AbstractEvent next = dto.getNextInFlow();
        if (result.isSuccess()) {
            if(result.getTrigger() != null) {
                dto.transition(result.getTrigger());
            } else {
                dto.transition(Trigger.OK);
            }
            if (next != null) {
                next.fireChainedEvent(dto);
            }
        } else {
            dto.transition(Trigger.NOT_OK);
        }
    }

    protected void checkValidation(Object... inputValue) {
        for (AbstractValidation validation : this.validations) {
            validation.validate(inputValue);
        }
    }

}
