package org.godzzo.flusad.test.source;

import org.godzzo.flusad.config.Context;
import org.godzzo.flusad.source.MapSource;
import org.godzzo.flusad.source.XmlSource;
import org.junit.Test;

public class TestXmlSource {
	// TODO: IT CAN BE STEPABLE, BUT MapSource!
	
	@Test
	public void randomPicks() throws Exception {
		XmlSource source = new XmlSource("Test")
			.setUri("src/test/resources/xml/rss/hup/sample.xml")
		;
		
		new Context().source(source);
		
		for (int i=0; i<source.getSize(); i++) {
			print(i+". ITEM");
			print("- Title: "+source.pickOne("title", MapSource.MODE_RANDOM));
			print("- Description: "+source.pickOne("description", MapSource.MODE_LAST));
		}
	}
	
	private void print(String text) {
		System.out.println(text);
	}
}
