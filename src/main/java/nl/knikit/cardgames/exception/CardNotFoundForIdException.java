package nl.knikit.cardgames.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Key Not Found") //404 for general error page
public class CardNotFoundForIdException extends Exception {
    
    private static final long serialVersionUID = -3332292346834265371L;

    public CardNotFoundForIdException(String cardId) {
        super(String.format("No data found for /card in path: %s\n", cardId));
    }
}