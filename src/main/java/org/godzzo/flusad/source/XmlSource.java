package org.godzzo.flusad.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlSource extends MapSource {
	private String uri = "http://origo.hu/contentpartner/rss/hircentrum/origo.xml";
	private String path = "/rss/channel/item";
	
	public XmlSource() {
		super();
	}
	public XmlSource(String name) {
		this();
		setName(name);
	}
	
	@Override
	public void load() throws Exception {
		setItems(new ArrayList<Map<String, Object>>());
		
		DOMParser parser = new org.apache.xerces.parsers.DOMParser();
		
		parser.setFeature("http://xml.org/sax/features/validation", false);

		parser.parse(getUri());
		
		Document document = parser.getDocument();
		
		NodeList list = XPathAPI.selectNodeList(document, getPath());
		
		for (int i=0; i<list.getLength(); i++) {
			Node item = list.item(i);
			
			parseItem(i, item);
		}
	}

	protected void parseItem(int i, Node item) throws TransformerException {
		NodeList nodes = XPathAPI.selectNodeList(item, "./*");
		Map<String, Object> value = new HashMap<String, Object>();
		
		for (int j=0; j<nodes.getLength(); j++) {
			value.put(nodes.item(j).getNodeName(), nodes.item(j).getTextContent());
		}
		
		getItems().add(value);
	}

	public String getUri() {
		return uri;
	}
	public String getPath() {
		return path;
	}
	public XmlSource setUri(String uri) {
		this.uri = uri;
		return this;
	}
	public XmlSource setPath(String path) {
		this.path = path;
		return this;
	}
	@Override
	public int getSize() {
		return getItems().size();
	}
}
