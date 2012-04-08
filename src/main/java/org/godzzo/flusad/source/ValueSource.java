package org.godzzo.flusad.source;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ValueSource extends AbstractSource {
	@SuppressWarnings("unused")
	private Log log = LogFactory.getLog(ValueSource.class);
	
	public ValueSource() {
		setName("Value");
	}
	
	@Override
	public void load() throws Exception {
	}

	@Override
	public Object pickOne(String field, String mode) throws Exception {
		if (!field.contains(".")) {
			throw new RuntimeException("It is not a valid name ["+field+"] (proper: Source.field)!");
		}
		
		String[] data = field.split("\\.");
		
		String source = data[0];
		String name = data[1];
		
		Map<String, ?> sourceValues = getContext().getValues().get(source);
		
		if (sourceValues == null) {
			throw new RuntimeException("Source not found ["+source+"] !");
		}
		
		if (!sourceValues.containsKey(name)) {
			throw new RuntimeException("Source value not found for ["+name+"] field!");
		}
		
		return sourceValues.get(name);
	}
}
