package nl.knikit.cardgames.commons.controller;

import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.state.CardGameStateMachine;

public interface Controller<T extends Game> {
	
	T init(T context, CardGameStateMachine.State currentState);
	
	T init(T context);
	
	T reinstate(final int id);
	
	T getContext();
	
	void setContext(T context);
	
	void transition(final CardGameStateMachine.Trigger trigger);
	
	ControllerResponse reset();
}
