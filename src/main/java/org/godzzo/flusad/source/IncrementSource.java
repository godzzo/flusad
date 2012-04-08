package org.godzzo.flusad.source;

public class IncrementSource extends AbstractSource {
	private long value = 1L;
	
	public IncrementSource() {
		super();
	}
	public IncrementSource(String name) {
		this();
		setName(name);
	}
	public IncrementSource(String name, long start) {
		this(name);
		setValue(start);
	}
	
	@Override
	public void load() throws Exception {
	}
	
	@Override
	public Object pickOne(String field, String mode) throws Exception {
		value++;
		return value;
	}

	public long getValue() {
		return value;
	}
	public IncrementSource setValue(long value) {
		this.value = value;
		return this;
	}
}
