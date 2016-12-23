package nl.knikit.cardgames.model.state;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Scope("prototype")
public class CardGameStateMachine {
    
    public enum State {
        IS_CONFIGURED, HAS_PLAYERS, IS_SETUP, PLAYING, GAME_WON, NO_WINNER, EMPTY_DECK
    }

    public enum Trigger {
        GAME_ADDED, GAME_CHANGED, HUMAN_ADDED, PLAYERS_CHANGED, HUMAN_DELETED, DECK_SHUFFLED, CARD_DEALT, TURN_PASSED, AI_TURNED, TURN_STARTED, SHOW_RESULTS, ROUNDS_ENDED, PLAYER_WINS, NO_CARDS_LEFT, GET_PRIZE
    }

    private StateMachine<State, Trigger> sm;

    public void initialize(final StateMachineConfig<State, Trigger> config, final State currentState) {
        sm = new StateMachine<>(currentState, config);
    }

    public void initialize(final StateMachineConfig<State, Trigger> config) {
        this.initialize(config, State.IS_SETUP);
    }

    public void check(final State state) {
        if (!sm.getState()
                .equals(state)) {
            throw new IllegalStateException("Unexpected state found: " + sm.getState() + " instead of: " + state);
        }
    }

    public String fireTrigger(final Trigger trigger) {
        if (!sm.canFire(trigger)) {
            return null;
        }

        sm.fire(trigger);
        return sm.getState()
                .toString();
    }

    public String getState() {
        return getStateEnum().toString();
    }

    public State getStateEnum() {
        return sm.getState();
    }

}
