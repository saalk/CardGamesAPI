package nl.knikit.cardgames.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import nl.knikit.cardgames.VO.CardGame;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardGameResponse {
	
	private CardGame cardGame;
	
	private Reason reason;
	
	public enum Reason {
		SUCCESS, FAILURE;
	}
	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private String errorCode;
	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private String errorMessage;
	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private String solution;
	
	@JsonIgnore
	public Reason getReason() {
		return reason;
	}
	
	@JsonIgnore
	public void setReason(Reason reason) {
		this.reason = reason;
	}
	
	
	@JsonIgnore
	public String getErrorCode() {
		return errorCode;
	}
	
	@JsonIgnore
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	@JsonIgnore
	public String getSolution() {
		return solution;
	}
	
	@JsonIgnore
	public void setSolution(String solution) {
		this.solution = solution;
	}
	
	@JsonIgnore
	public String getErrorMessage() {
		return errorMessage;
	}
	
	@JsonIgnore
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}