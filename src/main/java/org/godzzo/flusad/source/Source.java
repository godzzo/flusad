package org.godzzo.flusad.source;

import org.godzzo.flusad.config.Context;

public interface Source {
	void load() throws Exception;
	Object pickOne(String field, String mode) throws Exception;
	String getName();
	void setContext(Context context);
	int getSize();
}
