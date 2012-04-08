package org.godzzo.flusad.test.source;

import java.util.Date;

import org.godzzo.flusad.config.Context;
import org.godzzo.flusad.source.DateIncrementSource;
import org.godzzo.flusad.source.DateIncrementSource.Interval;
import org.godzzo.flusad.source.DateIncrementSource.Position;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class TestDateIncrementSource {
	// STEPABLE
	
	@Test
	public void oneWeekStep() throws Exception {
		System.out.println("\n... oneWeekStep ...\n");
		
		Context context = new Context();
		DateIncrementSource source = new DateIncrementSource()
			.setNextInterval(new Interval(DurationFieldType.weeks(), 1))
			.setNextPosition(new Position(DateTimeFieldType.dayOfWeek(), DateTimeConstants.FRIDAY))
		;
		
		context.source(source);
		
		for (int i=0; i<10; i++) {
			Date date = (Date) source.pickOne(null, null);
			System.out.println("Date: "+date);
		}
	}
	
	@Test
	public void multipleWeekWithTo() throws Exception {
		System.out.println("\n... multipleWeekWithTo ...\n");
		
		Context context = new Context();
		DateIncrementSource source = new DateIncrementSource()
			.setNextInterval(new Interval(DurationFieldType.weeks(), 1, 3))
			.setToInterval(new Interval(DurationFieldType.days(), 3, 4, 2))
		;
		
		context.source(source);
		
		for (int i=0; i<10; i++) {
			Date date = (Date) source.pickOne(null, null);
			System.out.println("Date: "+date);
			date = (Date) source.pickOne("to_value", null);
			System.out.println("ToDate: "+date);
		}
	}
}
