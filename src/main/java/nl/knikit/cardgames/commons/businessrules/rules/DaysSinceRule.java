package nl.knikit.cardgames.commons.businessrules.rules;

import nl.knikit.cardgames.commons.businessrules.util.DutchBusinessDaysUtil;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

/**
 * rule to determine whether or not it's been x days since input (meaning last time a request has been made)
 */
@Component
@Lazy
@Slf4j
public class DaysSinceRule extends Rule<Date> {

	private static final int MAX_NON_WORKING_DAYS = 4;
	
	private DutchBusinessDaysUtil dutchBusinessDaysUtil = new DutchBusinessDaysUtil();
	private Integer thresholdValue;

	@PostConstruct
	public void init() {
		this.errorCode = 2; // sequential error code
		this.thresholdValue = Integer.valueOf(threshold.obtainThreshold("threshold/days/since"));
	}

	protected Calendar getCalendarWithCurrentTime() {
		return Calendar.getInstance();
	}

	@Override
	public boolean evaluate(final Date previousRequestDate) {

		if (previousRequestDate == null) {
			return false; //current behavior, should be "true" really
		}
		final Calendar currentTime = getCalendarWithCurrentTime();
		final DateTime previousRequestDateTime = new DateTime(previousRequestDate.getTime());
		final DateTime currentRequestDateTime = new DateTime(currentTime.getTime());
		if (Days.daysBetween(previousRequestDateTime, currentRequestDateTime).getDays() > (MAX_NON_WORKING_DAYS + thresholdValue)) {
			return true;
		}
		//with a weekend and holidays the max number of non-working days affecting the result is 4
		//if the current date differs less from the date of the previous request than the threshold + 4 days
		//the working days must be checked
		log.debug(String.format("input value = %s - border date = %s", previousRequestDate, currentTime.getTime()));
		return dutchBusinessDaysUtil.calculateNrOfBusinessDaysInInterval(previousRequestDate, currentTime.getTime()) > thresholdValue;
	}
}