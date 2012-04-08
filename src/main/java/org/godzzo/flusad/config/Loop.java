package org.godzzo.flusad.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.godzzo.flusad.source.Source;

public class Loop {
	private Log log = LogFactory.getLog(getClass());
	
	private Context context;
	
	private Before before;
	private BeforeAction beforeAction;
	private Action action;
	private AfterAction afterAction;
	private After after;
	
	private int count;
	private String source;
	
	private String name;
	
	private List<Insert> inserts = new ArrayList<Insert>();
	private List<Loop> loops = new ArrayList<Loop>();
	
	public Loop() {
		super();
	}
	public Loop(String name) {
		this();
		setName(name);
	}
	
	public void execute() throws Exception {
		Source source = null;
		
		Map<String, Object> map = new HashMap<String, Object>();
		context.getValues().put(getName(), map);
		
		if (getBefore() == null || getBefore().invoke(getContext(), this)) {
			if (getSource() != null) {
				source = getContext().getSources().get(getSource());
				
				source.load();
				
				setCount(source.getSize());
			}
			
			map.put("count", getCount());
			
			for (int i=0; i<getCount(); i++) {
				action(source, map, i);
			}
			
			if (getAfter() != null) {
				getAfter().invoke(getContext(), this);
			}
		}
	}
	
	protected void action(Source source, Map<String, Object> map, int i)
			throws Exception {
		log.debug(String.format("Loop [%s] step %d.", getName(), i));
		
		map.put("position", i);
		
		if (source != null) {
			map.put("value", source.pickOne("value", "base"));
		}
		
		if (getBeforeAction() == null || getBeforeAction().invoke(getContext(), this, i)) {
			for (Insert insert : getInserts()) {
				log.debug(String.format("Loop [%s] step %d, insert %s", getName(), i, insert.getName()));
				insert.execute();
			}
			
			for (Loop loop : getLoops()) {
				log.debug(String.format("Loop [%s] step %d, loop %s", getName(), i, loop.getName()));
				loop.execute();
			}
			
			if (getAction() != null) {
				getAction().invoke(getContext(), this, i);
			}
			
			if (getAfterAction() != null) {
				getAfterAction().invoke(getContext(), this, i);
			}
		}
	}
	
	public Loop insert(Insert insert) {
		insert.setContext(getContext());
		inserts.add(insert);
		return this;
	}
	public Loop loop(Loop loop) {
		loop.setContext(getContext());
		loops.add(loop);
		return this;
	}
	
	public static interface Action {
		boolean invoke(Context context, Loop loop, int position) throws Exception;
	}
	public static interface BeforeAction {
		boolean invoke(Context context, Loop loop, int position) throws Exception;
	}
	public static interface AfterAction {
		boolean invoke(Context context, Loop loop, int position) throws Exception;
	}
	public static interface Before {
		boolean invoke(Context context, Loop loop) throws Exception;
	}
	public static interface After {
		void invoke(Context context, Loop loop) throws Exception;
	}
	
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
		
		for (Insert insert : inserts) {
			insert.setContext(context);
		}
		for (Loop loop : loops) {
			loop.setContext(context);
		}
	}
	public List<Insert> getInserts() {
		return inserts;
	}
	public Loop setInserts(List<Insert> inserts) {
		this.inserts = inserts;
		return this;
	}
	public List<Loop> getLoops() {
		return loops;
	}
	public Loop setLoops(List<Loop> loops) {
		this.loops = loops;
		return this;
	}
	public Before getBefore() {
		return before;
	}
	public After getAfter() {
		return after;
	}
	public Loop setBefore(Before before) {
		this.before = before;
		return this;
	}
	public Loop setAfter(After after) {
		this.after = after;
		return this;
	}
	public int getCount() {
		return count;
	}
	public Loop setCount(int count) {
		this.count = count;
		return this;
	}
	public String getSource() {
		return source;
	}
	public Loop setSource(String source) {
		this.source = source;
		return this;
	}
	public String getName() {
		return name;
	}
	public Loop setName(String name) {
		this.name = name;
		return this;
	}
	public Action getAction() {
		return action;
	}
	public Loop setAction(Action action) {
		this.action = action;
		return this;
	}
	public BeforeAction getBeforeAction() {
		return beforeAction;
	}
	public AfterAction getAfterAction() {
		return afterAction;
	}
	public Loop setBeforeAction(BeforeAction beforeAction) {
		this.beforeAction = beforeAction;
		return this;
	}
	public Loop setAfterAction(AfterAction afterAction) {
		this.afterAction = afterAction;
		return this;
	}
	
	@Override
	public String toString() {
		return String.format("Loop [name: %s, count: %d]", getName(), getCount());
	}
}
