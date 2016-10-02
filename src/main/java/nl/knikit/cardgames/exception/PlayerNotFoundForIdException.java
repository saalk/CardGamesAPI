package nl.knikit.cardgames.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Key Not Found") //404 for general error page
public class PlayerNotFoundForIdException extends Exception {

    private static final long serialVersionUID = -3332292346834265371L;

    public PlayerNotFoundForIdException(int id) {
        super(String.format("No data found for /id in path: %d\n", id));
    }
}