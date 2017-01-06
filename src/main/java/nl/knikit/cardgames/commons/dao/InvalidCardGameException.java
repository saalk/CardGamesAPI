package nl.knikit.cardgames.commons.dao;

/**
 * Created by cl94wq on 22-02-16.
 */
public class InvalidCardGameException extends RuntimeException {
	public InvalidCardGameException(String message) {
		super(message);
	}

	public InvalidCardGameException(String message, Throwable cause) {
		super(message, cause);
	}
}
