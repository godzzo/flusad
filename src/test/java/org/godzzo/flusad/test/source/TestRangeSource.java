package org.godzzo.flusad.test.source;

import org.godzzo.flusad.config.Context;
import org.godzzo.flusad.source.RangeSource;
import org.junit.Test;

public class TestRangeSource {
	// STEPABLE
	
	@Test
	public void stepOne() throws Exception {
		Context context = new Context();
		RangeSource<Integer> source = new RangeSource<Integer>()
			.setMin(1)
			.setMax(10)
		;
		
		context.source(source);
		
		for (int i=0; i<25; i++) {
			System.out.println("STEP PickOne: "+source.pickOne(null, RangeSource.MODE_STEP));
		}
	}
	
	@Test
	public void randomOne() throws Exception {
		Context context = new Context();
		RangeSource<Integer> source = new RangeSource<Integer>()
			.setMin(1)
			.setMax(10)
		;
		
		context.source(source);
		
		for (int i=0; i<25; i++) {
			System.out.println("RANDOM PickOne: "+source.pickOne(null, RangeSource.MODE_RANDOM));
		}
	}
}
