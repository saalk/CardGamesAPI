package nl.knikit.cardgames.commons.event;

import nl.knikit.cardgames.model.state.CardGameStateMachine;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractFlowDTO implements FlowEventCallback {

    private Queue<AbstractEvent> chain;
    private boolean running = false;
    
    @Setter
    @Getter
    private CardGameStateMachine stateMachine;

    public AbstractEvent getNextInFlow() {
        return chain.poll();
    }

    public AbstractFlowDTO() {
        this(new ConcurrentLinkedQueue<AbstractEvent>());
    }

    public AbstractFlowDTO(ConcurrentLinkedQueue<AbstractEvent> queue) {
        this.chain = queue;
    }

    public boolean addEvent(AbstractEvent event) {
        return !running && chain.add(event);
    }

    public void start() {
        running = true;
        final AbstractEvent first = this.getNextInFlow();
        if (first == null) {
            return;
        }
        first.fireChainedEvent(this);
    }

    public void transition(final CardGameStateMachine.Trigger trigger) {
        this.stateMachine.transition(trigger);
    }

    @Override
    public void startingEvent(final AbstractEvent event) {
        // stubbed for subclasses. Override if necessary
    }

    @Override
    public void endingEvent(final AbstractEvent event) {
        // stubbed for subclasses. Override if necessary
    }
}
