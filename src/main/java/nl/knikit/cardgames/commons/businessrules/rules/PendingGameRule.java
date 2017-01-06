package nl.knikit.cardgames.commons.businessrules.rules;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

@Component
@Lazy
@Slf4j
public class PendingGameRule extends Rule<List> {

	@PostConstruct
	public void init() {
        this.load();
		this.errorCode = 3;
	}

	@Override
	public boolean evaluate(List input) {
		log.debug(String.format("input value = %s - allowed size = 0", input));
		return input != null && input.size() == 0;
	}
	
}
