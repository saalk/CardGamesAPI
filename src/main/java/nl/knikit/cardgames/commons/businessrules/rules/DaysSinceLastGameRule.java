package nl.knikit.cardgames.commons.businessrules.rules;

import nl.knikit.cardgames.model.Game;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@Lazy
public class DaysSinceLastGameRule extends Rule<Game> {

	@Resource
	private DaysSinceRule daysSinceRule;

    @PostConstruct
	public void init() {
        this.load();
		errorCode = daysSinceRule.getErrorCode();
	}

	@Override
	public boolean evaluate(final Game input) {
		if (input == null) {
			return true;
		} else {
			//final Date datetime = input.getDatetime();
			final Date datetime = new Date();
			
			return daysSinceRule.evaluate(datetime);
		}
	}

}
