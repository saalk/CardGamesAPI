package nl.knikit.cardgames.exception;

import com.google.common.base.Objects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
// A class which annotated with @ControllerAdvice will be registered as the global exception handler.
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ExceptionJSONInfo> handleSQLException(Exception ex) {

        log.error("SQL Exception occurred: " + ex);

        ExceptionJSONInfo error = new ExceptionJSONInfo();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setUrl("/api/*");
        error.setMessage("Database exception, please contact the administrator. " + ex);
        return new ResponseEntity<ExceptionJSONInfo>(error, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ExceptionJSONInfo> handleIOException(Exception ex) {

        log.error("File not found (IO) exception: " + ex);

        ExceptionJSONInfo error = new ExceptionJSONInfo();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setUrl("/api/*");
        error.setMessage("Missing file exception, Please contact the administrator. " + ex);
        return new ResponseEntity<ExceptionJSONInfo>(error, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(PlayerNotFoundForIdException.class)
    public ResponseEntity<ExceptionJSONInfo> handlePlayerNotFoundForIdException(Exception ex) {

        log.error("PlayerNotFoundForIdException Occurred: " + ex);

        ExceptionJSONInfo error = new ExceptionJSONInfo();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setUrl("/api/players/(id)");
        error.setMessage("Missing player, supply a valid id. " + ex);
        return new ResponseEntity<ExceptionJSONInfo>(error, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(GameNotFoundForIdException.class)
    public ResponseEntity<ExceptionJSONInfo> handleGameNotFoundForIdException(Exception ex) {

        log.error("GameNotFoundForIdException Occurred: " + ex);

        ExceptionJSONInfo error = new ExceptionJSONInfo();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setUrl("/api/games/(id)");
        error.setMessage("Missing game for reference, please contact the administrator. " + ex);
        return new ResponseEntity<ExceptionJSONInfo>(error, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionJSONInfo> handleException(Exception ex) {

        log.error("Exception Occurred. Please investigate: "
                + ex + "\n"
                + Arrays.asList(ex.getStackTrace())
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n")) + "\n"
        );

        ExceptionJSONInfo error = new ExceptionJSONInfo();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setUrl("/api/*");
        error.setMessage("Something went wrong, please contact the administrator. " + ex);
        return new ResponseEntity<ExceptionJSONInfo>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
