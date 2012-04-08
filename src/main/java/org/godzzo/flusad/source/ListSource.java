package org.godzzo.flusad.source;

import java.util.List;

public class ListSource extends AbstractSource {
	private List<?> items;
	
	public ListSource() {
		super();
	}
	public ListSource(String name) {
		this();
		
		setName(name);
	}
	
	@Override
	public void load() throws Exception {
	}
	
	@Override
	public Object pickOne(String field, String mode) throws Exception {
		int nextInt = getContext().getRandom().nextInt(getItems().size());
		
		return getItems().get(nextInt);
	}
	
	public List<?> getItems() {
		return items;
	}

	public ListSource setItems(List<?> items) {
		this.items = items;
		return this;
	}
	@Override
	public int getSize() {
		return getItems().size();
	}
}
