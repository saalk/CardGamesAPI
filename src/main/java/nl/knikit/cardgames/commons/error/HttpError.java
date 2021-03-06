package nl.knikit.cardgames.commons.error;

import nl.knikit.cardgames.model.enumlabel.LabeledEnum;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public enum HttpError implements LabeledEnum{
	
	BAD_REQUEST_GAMETYPE("400", "Please supply correct 'gameType' in your event to 'init' your cardgame."),
	BAD_REQUEST_ANTE("400", "Please supply a normal 'ante' in your event to 'init' your cardgame."),
	BAD_REQUEST_HUMAN_ALIAS("400", "Please supply a normal 'alias' in your event to 'setup' a human player in your cardgame."),
	BAD_REQUEST_HUMAN_AILEVEL("400", "Please supply the 'Human' aiLevel in your event to 'setup' a human player in your cardgame."),
	BAD_REQUEST_AI_AILEVEL("400", "Please do not supply the 'Human' aiLevel in your event to 'setup' a ai player in your cardgame."),
	
	UNAUTHORIZED("401", "Please supply correct credentials to play the cardgame."),
	FORBIDDEN("403", "Sorry, you don't have permission to play this cardgame."),
	
	NOT_FOUND_CARDGAME("404", "Please supply an existing id for the cardgame you want to 'init'"),
	NOT_FOUND_PLAYER("404", "Please supply an existing id for the player of your cardgame"),
	NOT_FOUND_CARD("404", "Please supply a valid card for the aciton you want to play"),
	
	METHOD_NOT_ALLOWED("405", "Please supply a correct event that is in sync with the state of your cardgame."),
	CODE_CONFLICT("409", "Please supply a correct action, this action is not possible at this moment."),
	PRECONDITION_FAILED("412", "Please check the rules for the cardgame, this event and actions are not possible."),
	
	INTERNAL_SERVER_ERROR("500", "Internal server error, please try again.");
	
	private String label;
	private String message;
	
	/**
	 * A static HashMap lookup with key + value is created to use in a getter
	 * to fromLabel the Enum based on the name eg. key "Low" -> value AiLevel.DUMB
	 */
	private static final Map<Integer, HttpError> lookup
			= new HashMap<>();
	
	static {
		for (HttpError httpError : EnumSet.allOf(HttpError.class))
			lookup.put(Integer.valueOf(httpError.getLabel()), httpError);
	}
	
	HttpError() {
	}
	
	HttpError(String label, String message) {
		this();
		this.label = label;
		this.message = message;
	}
	
	public static HttpError fromLabel(Integer label) {
		return lookup.get(label);
	}
	
	
	
}