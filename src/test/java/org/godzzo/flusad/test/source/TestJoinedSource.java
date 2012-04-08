package org.godzzo.flusad.test.source;

import org.godzzo.flusad.config.Context;
import org.godzzo.flusad.source.JoinedSource;
import org.godzzo.flusad.source.MapSource;
import org.godzzo.flusad.source.XmlSource;
import org.junit.Test;

public class TestJoinedSource {
	// TODO: IT CAN BE STEPABLE, BUT MapSource!
	
	@Test
	public void randomPicks() throws Exception {
		JoinedSource source = new JoinedSource("Joined").setNames("Hup,Origo"); 
		
		new Context()
			.source(new XmlSource("Hup").setUri("src/test/resources/xml/rss/hup/sample.xml"))
			.source(new XmlSource("Origo").setUri("src/test/resources/xml/rss/origo/sample.xml"))
			.source(source)
		;
		
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
