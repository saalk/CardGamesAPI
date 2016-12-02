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
public class GalacticCasinoStateMachine {
    
    public enum State {
        SELECT_GAME, SETUP_GAME, ITERATE_PLAYERS, ITERATE_TURNS, STOP_GAME, OFFER_FICHES
    }

    public enum Trigger {
        GAME_SELECTED, GAME_STOPPED, GAME_SETUP, TURN_STARTED, ANOTHER_TURN, TURN_ENDED, ROUNDS_ENDED, PLAYER_WINS, DECK_EMPTY, GAME_FINISHED, OFFER_ACCEPTED, QUIT
    }

    private StateMachine<State, Trigger> sm;

    public void initialize(final StateMachineConfig<State, Trigger> config, final State currentState) {
        sm = new StateMachine<>(currentState, config);
    }

    public void initialize(final StateMachineConfig<State, Trigger> config) {
        this.initialize(config, State.SELECT_GAME);
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
