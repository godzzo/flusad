package org.godzzo.flusad.test.source;

import org.godzzo.flusad.config.Context;
import org.godzzo.flusad.source.DelimitedSource;
import org.godzzo.flusad.source.MapSource;
import org.junit.Assert;
import org.junit.Test;

public class TestDelimitedSource {
	// TODO: IT CAN BE STEPABLE!
	
	@Test
	public void checkSize() throws Exception {
		DelimitedSource source = new DelimitedSource("Test")
			.setDelimiter(";")
			.setNames("name,type")
			.setPath("src/test/resources/csv/simple.txt")
		;
		
		new Context().source(source);
		
		int size = source.getSize();
		print("SIZE: "+size);
		
		Assert.assertTrue(size == 7);
	}
	
	@Test
	public void randomPick() throws Exception {
		DelimitedSource source = new DelimitedSource("Test")
			.setDelimiter(";")
			.setNames("name,type")
			.setPath("src/test/resources/csv/simple.txt")
		;
		
		new Context().source(source);
		
		int size = source.getSize();
		
		for (int i=0; i<size; i++) {
			print(i+". ITEM");
			print("- "+source.pickOne("name", MapSource.MODE_RANDOM).toString());
			print("- "+source.pickOne("type", MapSource.MODE_LAST).toString());
		}
	}
	
	private void print(String text) {
		System.out.println(text);
	}
}
