package nl.knikit.cardgames.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import nl.knikit.cardgames.commons.controller.ControllerResponse;
import nl.knikit.cardgames.model.Casino;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardGameResponse {
	
	private Reason reason;
	public enum Reason {
		SUCCESS, FAILURE
	}
	public boolean isSuccess() {
		return ControllerResponse.Reason.SUCCESS.equals(reason);
	}
	public boolean isFailure() {
		return ControllerResponse.Reason.FAILURE.equals(reason);
	}
	
	private String cardGameId;
	private String playerId;
	private List<Casino> casinos;
	private String errorCode;
	private String errorReason;
	
}
	
	

