package org.godzzo.flusad.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import org.godzzo.flusad.source.Source;

public class Context {
	private Map<String, Map<String, Object>> values = new HashMap<String, Map<String, Object>>();
	private List<Insert> inserts = new ArrayList<Insert>();
	private List<Loop> loops = new ArrayList<Loop>();
	private Map<String, Source> sources = new HashMap<String, Source>();
	private Stack<Insert> stack = new Stack<Insert>();
	private Random random = new Random();
	private String target = "data";
	
	public Context() {
		super();
	}
	public Context(String target) {
		this();
		setTarget(target);
	}
	
	public Context source(Source source) throws Exception {
		source.setContext(this);
		
		sources.put(source.getName(), source);
		
		source.load();
		
		return this;
	}
	
	public Context insert(Insert insert) {
		insert.setContext(this);
		
		inserts.add(insert);
		
		return this;
	}
	
	public Context loop(Loop loop) {
		loop.setContext(this);
		
		loops.add(loop);
		
		return this;
	}
	
	public Context execute() throws Exception {
		for (Insert insert : inserts) {
			insert.execute();
		}
		
		for (Loop loop : loops) {
			loop.execute();
		}
		
		return this;
	}
	
	public int roll(int min, int max) {
		return getRandom().nextInt(max-min+1)+min;
	}
	
	public List<Insert> getInserts() {
		return inserts;
	}
	public Map<String, Source> getSources() {
		return sources;
	}
	public void setInserts(List<Insert> inserts) {
		this.inserts = inserts;
	}
	public void setSources(Map<String, Source> sources) {
		this.sources = sources;
	}
	public Stack<Insert> getStack() {
		return stack;
	}
	public void setStack(Stack<Insert> stack) {
		this.stack = stack;
	}
	public Random getRandom() {
		return random;
	}
	public Map<String, Map<String, Object>> getValues() {
		return values;
	}
	public void setTarget(String target) {
		this.target  = target;
	}
	public String getTarget() {
		return target;
	}
}
