package org.godzzo.flusad.source;

import java.util.List;
import java.util.Map;

public abstract class MapSource extends AbstractSource {
	public static final String MODE_LAST = "last";
	public static final String MODE_RANDOM = "random";
	
	private List<Map<String, Object>> items;
	private Map<String, Object> last;
	
	private boolean strict = true;
	
	@Override
	public Object pickOne(String field, String mode) throws Exception {
		if (mode.equals(MODE_RANDOM)) {
			return randomPick(field, mode);
		} else if (mode.equals(MODE_LAST)) {
			return lastPick(field, mode);
		} else {
			throw new RuntimeException(String.format("Pick mode [%s] not found in [%s]!", mode, toString()));
		}
	}
	
	public Map<String, Object> find(String field, Object value) {
		for (Map<String, Object> item : getItems()) {
			if (item.get(field).equals(value)) {
				return item;
			}
		}
		
		return null;
	}
	
	protected Object lastPick(String field, String mode) throws Exception {
		if (getLast() == null) {
			throw new RuntimeException(String.format("Last item not found [%s]!", toString()));
		}
		
		return getFieldValue(getLast(), field, mode);
	}
	
	protected Object randomPick(String field, String mode) throws Exception {
		int size = getItems().size();
		
		if (size < 1) {
			throw new RuntimeException("Source ["+getName()+"] not has any elements!");
		}
		
		int nextInt = getContext().getRandom().nextInt(size);
		
		setLast(getItems().get(nextInt));
		
		return getFieldValue(getLast(), field, mode);
	}
	
	protected Object getFieldValue(Map<String, ?> item, String field, String mode) throws Exception {
		Object value = item.get(field);
		
		if (!item.containsKey(field)) {
			throw new RuntimeException(String.format("Field [%s] not found in [%s]!", field, toString()));
		}
		
		if (value == null && isStrict()) {
			throw new RuntimeException(String.format("Field [%s] is null in [%s]!", field, toString()));
		}
		
		return value;
	}
	
	public boolean isStrict() {
		return strict;
	}
	public void setStrict(boolean strict) {
		this.strict = strict;
	}
	
	public Map<String, Object> getLast() {
		return last;
	}
	public void setLast(Map<String, Object> last) {
		this.last = last;
	}
	
	public List<Map<String, Object>> getItems() {
		return items;
	}
	public void setItems(List<Map<String, Object>> items) {
		this.items = items;
	}
}
