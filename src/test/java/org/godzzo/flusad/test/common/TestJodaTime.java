package org.godzzo.flusad.test.common;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.junit.Test;

public class TestJodaTime {
	private static Log log = LogFactory.getLog(TestJodaTime.class);
	
	@Test
	public void simple() {
		LocalDate date = new LocalDate();
		
		dateInfo("Now", date);
		
		LocalDate nextWeek = date.withFieldAdded(DurationFieldType.weeks(), 1);
		
		dateInfo("NextWeek", nextWeek);
		
		LocalDate nextMonday = nextWeek.withField(DateTimeFieldType.dayOfWeek(), DateTimeConstants.MONDAY);
		
		dateInfo("NextMonday", nextMonday);
		
		Date utilDate = date.toDate();
		LocalDate fromDateFields = LocalDate.fromDateFields(utilDate);
		dateInfo("FromDateFields", fromDateFields);
	}

	public static void dateInfo(String name, LocalDate date) {
		log.info(name+": "+date.toString());
		
		log.info("- WeekOfWeekyear: "+date.getWeekOfWeekyear());
		log.info("- Weekyear: "+date.getWeekyear());
		
		log.info("- DayOfWeek: "+date.getDayOfWeek());
		log.info("- DayOfMonth: "+date.getDayOfMonth());
		log.info("- DayOfYear: "+date.getDayOfYear());
		log.info("- MonthOfYear: "+date.getMonthOfYear());
	}
}
