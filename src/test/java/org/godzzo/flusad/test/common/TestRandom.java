package org.godzzo.flusad.test.common;

import java.util.Random;

import org.junit.Test;

public class TestRandom {
	
	@Test
	public void simple() {
		Random random = new Random();
		
		for (int i=0; i<100; i++) {
			System.out.println(""+(random.nextInt(3)+1)); // 4 never reach!
		}
	}
}
