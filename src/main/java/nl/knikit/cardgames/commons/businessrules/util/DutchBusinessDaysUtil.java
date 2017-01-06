package nl.knikit.cardgames.commons.businessrules.util;

// import de.jollyday.util.CalendarUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DutchBusinessDaysUtil {


	private static final String LIBERATION_DAY = "05-05";
	//calculate easter etc for current year and next year
	//add permanent holidays
	private static String[] REGULAR_HOLIDAYS = new String[]{
		"01-01", "25-12", "26-12"
	};

	private String kingsDay = "27-04";

	private static final String DATE_FORMAT = "dd-MM-yyyy";

	private Map<Integer, Set<Integer>> holidays = new HashMap<>();

	private Set<Integer> getRegularHolidays(final int year) {
		final Set<Integer> holidays = new HashSet<>();
		for (String regularHoliday : REGULAR_HOLIDAYS) {
			Calendar calendar = getCalendar(year, regularHoliday);
			holidays.add(calendar.get(Calendar.DAY_OF_YEAR));
		}
		return holidays;
	}

	private int getKingsDay(final int year) {
		final Calendar calendar = getCalendar(year, kingsDay);
		final int retval = calendar.get(Calendar.DAY_OF_YEAR);
		return (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ? (retval - 1) : retval);
	}


	private Set<Integer> getLiberationDay(final int year) {
		final Set<Integer> holidays = new HashSet<>();
		if (year % 5 == 0) {
			Calendar calendar = getCalendar(year, LIBERATION_DAY);
			holidays.add(calendar.get(Calendar.DAY_OF_YEAR));
		}
		return holidays;
	}

//	private Set<Integer> getEasterRelatedHolidays(final int year) {
//		final Set<Integer> holidays = new HashSet<>();
//		final Calendar easterSunday = new GregorianCalendar();
//		easterSunday.setTime(CalendarUtil.getEasterSunday(year).toDate());
//		final int easterSundayDayOfYear = easterSunday.get(Calendar.DAY_OF_YEAR);
//		holidays.add(easterSundayDayOfYear + 1); // easter monday
//		holidays.add(easterSundayDayOfYear + 39); // ascension day
//		holidays.add(easterSundayDayOfYear + 49 + 1); // pentecost monday
//		return holidays;
//	}

	private Calendar getCalendar(final int year, final String dateString) {
		final Date date = getDate(year, dateString);
		final Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}

	public static Date getDate(final String dateString) {
		try {
			return new SimpleDateFormat(DATE_FORMAT).parse(dateString);
		} catch (ParseException e) {
			log.error(e.getMessage());
			throw new RuntimeException("cannot parse date " + dateString);
		}
	}

	private static Date getDate(final int year, final String regularHoliday) {
		return getDate(regularHoliday + "-" + year);
	}

	private void calculateHolidays(final int year) {
		final Set<Integer> holidaysInYear = new HashSet<>();
		holidays.put(year, holidaysInYear);
		holidaysInYear.addAll(getRegularHolidays(year));
		holidaysInYear.add(getKingsDay(year));
		holidaysInYear.addAll(getLiberationDay(year));
//		holidaysInYear.addAll(getEasterRelatedHolidays(year));
	}

	protected void initHolidaysIfNecessery(final int year) {
		if (!holidays.containsKey(year)) {
			calculateHolidays(year);
		}
	}

	protected boolean isHolidayDay(final Calendar cal) {
		return holidays.get(cal.get(Calendar.YEAR)).contains(cal.get(Calendar.DAY_OF_YEAR));
	}

	protected boolean isBusinessDay(final Calendar cal) {
		//return !CalendarUtil.isWeekend(new LocalDate(cal.getTime().getTime())) && !isHolidayDay(cal);
		return false;
	}


	public int calculateNrOfBusinessDaysInInterval(final Date startDate, final Date endDate) {

		if (endDate.before(startDate)) {
			return 0;
		}
		final Calendar calendar1 = new GregorianCalendar();
		final int hourOfDay1 = prepareCalendar(startDate, calendar1);

		final Calendar calendar2 = new GregorianCalendar();
		final int hourOfDay2 = prepareCalendar(endDate, calendar2);

		int nrOfBusinessDays = 0;
		while (calendar1.before(calendar2)) {
			if (isBusinessDay(calendar1)) {
				nrOfBusinessDays++;
			}
			calendar1.add(Calendar.DAY_OF_MONTH, 1);
		}
		if (hourOfDay1 < hourOfDay2) {
			nrOfBusinessDays++;
		}
		if (isBusinessDay(calendar2)) {
			nrOfBusinessDays++;
		}
		return nrOfBusinessDays;
	}

	private int prepareCalendar(final Date date, final Calendar calendar) {
		calendar.setTime(date);
		final int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		initHolidaysIfNecessery(calendar.get(Calendar.YEAR));
		return hourOfDay;
	}
}
