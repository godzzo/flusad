package org.godzzo.flusad.source;

import org.godzzo.flusad.config.Context;

public abstract class AbstractSource implements Source {
	private Context context;
	private String name;
	
	
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setContext(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int getSize() {
		throw new RuntimeException("[getSize()] is unvalid call for this source!");
	}
}
