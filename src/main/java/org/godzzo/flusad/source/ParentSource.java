package org.godzzo.flusad.source;

import org.godzzo.flusad.config.Insert;

public class ParentSource extends AbstractSource {
	public ParentSource() {
		setName("Parent");
	}
	
	@Override
	public void load() throws Exception {
	}

	@Override
	public Object pickOne(String field, String mode) throws Exception {
		Insert parent = getContext().getStack().get(getContext().getStack().size()-2);
		
		if (!parent.getValues().containsKey(field)) {
			throw new RuntimeException(String.format("Field [%s] not found in [%s]!", field, toString()));
		}
		
		Object value = parent.getValues().get(field);
		
		return value;
	}
}
