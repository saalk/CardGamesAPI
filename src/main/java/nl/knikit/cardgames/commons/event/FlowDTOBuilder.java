package nl.knikit.cardgames.commons.event;


import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.state.CardGameStateMachine;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FlowDTOBuilder<T extends AbstractFlowDTO> implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Getter
    private final T flowDTO;

    public FlowDTOBuilder(T flowDTO) {
        this.flowDTO = flowDTO;
    }

    public FlowDTOBuilder<T> addEvent(Class<? extends AbstractEvent> eventClass) {
        final AbstractEvent eventBean = applicationContext.getBean(eventClass);
        // addEvent call in AbstractFlowDTO
        flowDTO.addEvent(eventBean);
        String message = String.format("FlowDTOBuilder in addEvent is: %s", eventBean);
        log.info(message);
        return this;
    }

    public FlowDTOBuilder<T> addStateMachine(CardGameStateMachine stateMachine) {
        flowDTO.setStateMachine(stateMachine);
        String message = String.format("FlowDTOBuilder in addStateMachine is: %s", stateMachine);
        log.info(message);
        return this;
    }
    
    public FlowDTOBuilder<T> addContext(Game context) {
        flowDTO.setGameContext(context);
        String message = String.format("FlowDTOBuilder in addContext is: %s", context);
        log.info(message);
        return this;
    }
    
    @SuppressWarnings("unchecked")
    public T build() {
	    String message = String.format("FlowDTOBuilder in build flowDTO is: %s", flowDTO);
	    log.info(message);
        return flowDTO;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}

