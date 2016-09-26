package nl.knikit.cardgames.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Player Not Found For Sequence param") //404 for general error page
public class PlayerNotFoundForSequenceException extends Exception {

    private static final long serialVersionUID = -3332292346834265371L;

    public PlayerNotFoundForSequenceException(int sequence){
        super(String.format("No Player found for param sequence: %d\n", sequence));
    }
}