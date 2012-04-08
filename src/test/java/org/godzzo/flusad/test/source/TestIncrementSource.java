package org.godzzo.flusad.test.source;

import org.godzzo.flusad.config.Context;
import org.godzzo.flusad.source.IncrementSource;
import org.junit.Assert;
import org.junit.Test;

public class TestIncrementSource {
	// TODO: I DON'T KNOWN, MAY BE CAN STEP ABLE, BUT IT NEED MAX FOR THAT (size support)?
	
	@Test
	public void steps() throws Exception {
		IncrementSource source = new IncrementSource("Test")
			.setValue(0)
		;
		
		new Context().source(source);
		
		Long pickOne = null;
		
		for (int i=0; i<10; i++) {
			pickOne = (Long) source.pickOne(null, null);
			print(i+".VAL: "+pickOne);
		}
		
		Assert.assertTrue(pickOne == 10L);
	}
	
	private void print(String text) {
		System.out.println(text);
	}
}
