package org.godzzo.flusad.test.source;

import java.util.Arrays;

import org.godzzo.flusad.config.Context;
import org.godzzo.flusad.source.ListSource;
import org.junit.Test;

public class TestListSource {
	// TODO: IT CAN BE STEPABLE!
	
	@Test
	public void randomPicks() throws Exception {
		ListSource source = new ListSource("Test")
			.setItems(Arrays.asList(new String[] {"ALPHA", "BETA", "GAMMA", "DELTA", "ZETA"}))
		;
		
		new Context().source(source);
		
		for (int i=0; i<source.getSize(); i++) {
			print(i+". ITEM");
			print("- Value: "+source.pickOne(null, null));
		}
	}
	
	private void print(String text) {
		System.out.println(text);
	}
}
