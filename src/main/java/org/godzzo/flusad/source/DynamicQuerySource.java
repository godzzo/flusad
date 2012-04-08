package org.godzzo.flusad.source;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DynamicQuerySource extends QuerySource {
	private Log log = LogFactory.getLog(DynamicQuerySource.class);
	
	public DynamicQuerySource() {
		super();
	}
	public DynamicQuerySource(String name) {
		super(name);
	}
	
	@Override
	public void load() throws Exception {
		log.debug("Load is empty!");
	}
	
	@Override
	public Object pickOne(String field, String mode) throws Exception {
		if (mode.equals(MODE_RANDOM)) {
			query(getContext().getValues());
			
			log.debug("Items is queried!");
		}
		
		return super.pickOne(field, mode);
	}
	
	@Override
	public DynamicQuerySource setConfig(String config) {
		super.setConfig(config);
		return this;
	}
	
	@Override
	public int getSize() {
		throw new RuntimeException("[getSize()] is unvalid call for this source!");
	}
}
