package nl.knikit.cardgames.commons.controller;

import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.state.CardGameStateMachine;

public interface Controller<T extends Game> {
	
	void init(T context, CardGameStateMachine.State currentState);
	
	void init(T context);
	
	T getContext();
	
	void setContext(T context);
	
	void transition(final CardGameStateMachine.Trigger trigger);
	
	ControllerResponse reset();
}
