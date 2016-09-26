package nl.knikit.cardgames.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionJSONInfo {

    // custom error class
    private int status;
    private String url;
    private String message;

}
