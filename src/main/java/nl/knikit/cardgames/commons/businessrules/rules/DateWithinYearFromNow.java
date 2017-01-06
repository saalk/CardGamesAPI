package nl.knikit.cardgames.commons.businessrules.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

/**
 * Name: DateWithinYearFromNow
 * IF the expiration date is 12 month in the future or less
 * THEN the result is Green.
 * ELSE the result is Red; REASON‘ Early Reissue only allowed in last 12 month’
 */
@Component
@Lazy
public class DateWithinYearFromNow extends Rule<Date> {

	private static final Logger log = LoggerFactory.getLogger(DateWithinYearFromNow.class);

	public DateWithinYearFromNow() {
	}

	@PostConstruct
	public void init() {
		this.errorCode = 505;
	} // just an error code

	/**
	 *
	 * @param validUntilDate
	 * @return true if rule passes meaning the future date passed to the evaluate is within one year from now
	 */
	public boolean evaluate(@NotNull final Date validUntilDate) {
		Date nowPlusOneYear = getDatePlusOneYear();
		boolean expiresWithinYear = validUntilDate.before(nowPlusOneYear);
		log.debug("expiration date is " + validUntilDate + ", which is " + (expiresWithinYear ? "within " : " after ") + "one year");
		return expiresWithinYear;
	}


	private static Date getDatePlusOneYear() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
		return cal.getTime();
	}
}
