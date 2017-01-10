package nl.knikit.cardgames.model.state;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Scope("prototype")
public class CardGameStateMachine {
	
	// @formatter:off
	public enum State {
		IS_CONFIGURED,
		HAS_PLAYERS,
		IS_SETUP,
		PLAYING,
		GAME_WON,
		NO_WINNER,
		
		ERROR,
		CANCELLED
	}
	
	public enum Trigger {
		POST_INIT,
		PUT_INIT,
		POST_INIT_HUMAN,
		POST_SETUP_HUMAN,
		DELETE_SETUP_HUMAN,
		
		DELETE_SETUP_AI,
		POST_SETUP_AI,
		PUT_SETUP_PLAYER,
		POST_SHUFFLE,
		
		PUT_TURN,
		PLAYER_WINS,
		ROUNDS_ENDED,
		NO_CARDS_LEFT,
		
		RESET,
		OK,
		NOT_OK
	}
	
	// @formatter:on
	private StateMachine<State, Trigger> sm;
	
	public void initialize(final StateMachineConfig<State, Trigger> config, final State currentState) {
		sm = new StateMachine<>(currentState, config);
	}
	
	public void initialize(final StateMachineConfig<State, Trigger> config) {
		this.initialize(config, State.IS_CONFIGURED);
	}
	
	public void check(final State state) {
		if (!sm.getState()
				     .equals(state)) {
			throw new IllegalStateException("Unexpected state found: " + sm.getState() + " instead of: " + state);
		}
	}
	
	public void checkAll(final List<State> states) {
		
		boolean found = false;
		for (State state : states) {
			if (sm.getState().equals(state))
				found = true;
		}
		if (!found) throw new IllegalStateException("Unexpected state found: " + sm.getState() + " instead of: " + states.toString());
	}
	
	public String transition(final Trigger trigger) {
		if (!sm.canFire(trigger)) {
			return null;
		}
		
		sm.fire(trigger);
		return sm.getState()
				       .toString();
	}
	
	public String getCurrentState() {
		return getCurrentStateEnum().toString();
	}
	
	public State getCurrentStateEnum() {
		return sm.getState();
	}
	
}
