package nl.knikit.cardgames.commons.event;


import nl.knikit.cardgames.model.state.CardGameStateMachine;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import lombok.Getter;

public class FlowDTOBuilder<T extends AbstractFlowDTO> implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Getter
    private final T flowDTO;

    public FlowDTOBuilder(T flowDTO) {
        this.flowDTO = flowDTO;
    }

    public FlowDTOBuilder<T> addEvent(Class<? extends AbstractEvent> eventClass) {
        final AbstractEvent eventBean = applicationContext.getBean(eventClass);
        flowDTO.addEvent(eventBean);
        return this;
    }

    public FlowDTOBuilder<T> addStateMachine(CardGameStateMachine stateMachine) {
        flowDTO.setStateMachine(stateMachine);
        return this;
    }

    @SuppressWarnings("unchecked")
    public T build() {
        return flowDTO;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}

