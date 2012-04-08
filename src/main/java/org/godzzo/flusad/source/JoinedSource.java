package org.godzzo.flusad.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JoinedSource extends MapSource {
	private String names;
	
	public JoinedSource() {
		super();
	}
	public JoinedSource(String name) {
		this();
		setName(name);
	}
	
	@Override
	public void load() throws Exception {
		setItems(new ArrayList<Map<String, Object>>());
		
		String[] names = getNames().split(",");
		
		for (String name : names) {
			Source source = getContext().getSources().get(name);
			
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("name", name);
			map.put("source", source);
			
			getItems().add(map);
		}
	}
	
	@Override
	protected Object getFieldValue(Map<String, ?> item, String field, String mode) throws Exception {
		Source source = (Source) item.get("source");
		
		return source.pickOne(field, mode);
	}
	
	public String getNames() {
		return names;
	}
	public JoinedSource setNames(String names) {
		this.names = names;
		return this;
	}
	@Override
	public int getSize() {
		String[] names = getNames().split(",");
		int size = 0;
		
		for (String name : names) {
			Source source = getContext().getSources().get(name);
			size += source.getSize();
		}
		
		return size;
	}
}
