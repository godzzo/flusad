package org.godzzo.flusad.test.xml;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xpath.XPathAPI;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestParseUri {
	@Test
	public void parseRss() throws Exception {
		DOMParser parser = new org.apache.xerces.parsers.DOMParser();
		
		// specifying validation
		parser.setFeature("http://xml.org/sax/features/validation", false);

		// installing error handler
		// parser.setErrorHandler(handler);
		
		parser.parse("http://origo.hu/contentpartner/rss/hircentrum/origo.xml");
		
		Document document = parser.getDocument();
		
		NodeList list = XPathAPI.selectNodeList(document, "/rss/channel/item");
		
		System.out.println("SIZE: "+list.getLength());
		
		for (int i=0; i<list.getLength(); i++) {
			Node item = list.item(i);
			
			NodeList nodes = XPathAPI.selectNodeList(item, "./*");
			
			System.out.println("Row: "+i);
			
			for (int j=0; j<nodes.getLength(); j++) {
				System.out.println(String.format(
					"- %s = [%s]",
					nodes.item(j).getNodeName(),
					nodes.item(j).getTextContent()
				));
			}
		}
	}
}

