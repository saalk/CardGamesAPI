package nl.knikit.cardgames.commons.validation;

import nl.knikit.cardgames.VO.CardGame;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public final class CardGameValidations implements Validator {
	
	private CardGameValidations() {
	}
	
		
	@Override
	public boolean supports(Class<?> aClass) {
		return CardGame.class.equals(aClass);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		CardGame cardGame = (CardGame) target;
		
		if(cardGame.getName() == null) {
			errors.rejectValue("name", "your_error_code");
		}
		
		// do "complex" validation here
		
	}
}
