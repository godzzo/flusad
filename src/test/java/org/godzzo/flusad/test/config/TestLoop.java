package org.godzzo.flusad.test.config;

import org.godzzo.flusad.config.Context;
import org.godzzo.flusad.config.Loop;
import org.godzzo.flusad.config.Loop.Action;
import org.godzzo.flusad.source.DateIncrementSource;
import org.godzzo.flusad.source.DateIncrementSource.Interval;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.junit.Test;

public class TestLoop {
	// TODO: Need to solve: 
	// -- random count (other source?)
	// -- add position to values 
	@Test
	public void scanTen() throws Exception {
		new Context()
			.loop(new Loop("Main").setCount(10))
			.execute()
		;
	}
	
	@Test
	public void showDiffOfDates() throws Exception {
		LocalDate minValue = LocalDate.now().minusDays(3);
		LocalDate maxValue = LocalDate.now().plusDays(4);
		DateIncrementSource source = new DateIncrementSource("Date")
			.setMinValue(minValue)
			.setMaxValue(maxValue)
			.setNextInterval(new Interval(DurationFieldType.days(), 1))
		;
		
		new Context().source(source);
		
		int size = source.getSize();
		System.out.println("MIN: "+minValue);
		System.out.println("MAX: "+maxValue);
		System.out.println("SIZE: "+size);
		
		for (int i=0; i<size; ++i) {
 			System.out.println(i+". ITEM: "+source.pickOne("value", "base"));
		}
	}
	
	@Test
	public void scanDates() throws Exception {
		LocalDate minValue = LocalDate.now().minusDays(3);
		LocalDate maxValue = LocalDate.now().plusDays(4);
		
		new Context()
			.source(new DateIncrementSource("Date")
				.setMinValue(minValue)
				.setMaxValue(maxValue)
				.setNextInterval(new Interval(DurationFieldType.days(), 1))
			)
			
			.loop(new Loop("Main")
				.setSource("Date") // Source provide the count
				.setAction(loopAction()) // Print the living message
				
				.loop(new Loop("Inner")
					.setCount(3) // Inner loop with static count
					.setAction(loopAction()) // Print the living message
				)
			)
			
			.execute()
		;
	}

	private Action loopAction() {
		return new Action() {
			@Override
			public boolean invoke(Context context, Loop loop, int position) {
				print("Action Loop: "+loop.getName()+" : "+position);
				return false;
			}
		};
	}
	
	private static void print(String text) {
		System.out.println(text);
	}
}
