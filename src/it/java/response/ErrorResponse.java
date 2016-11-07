package response;

import lombok.Getter;

@Getter
public class ErrorResponse {


    private Integer applicationId;
    private Integer errorId;
    private Long timestamp;

    private Integer httpStatusCode;

}
