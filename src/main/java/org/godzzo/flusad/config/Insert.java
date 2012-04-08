package org.godzzo.flusad.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.godzzo.flusad.source.Source;

// TODO: May be a check for the count of table, if you have a limit 
public abstract class Insert {
	private static final String FIELD_TYPE_STATIC = "STATIC";
	private static final String FIELD_TYPE_SCRIPT = "SCRIPT";
	private static final String FIELD_TYPE_EXPRESSION = "EXPRESSION";

	private static final String MODE_BASE = "base";

	protected Log log = LogFactory.getLog(getClass());
	
	private List<Insert> inserts = new ArrayList<Insert>();
	private List<Loop> loops = new ArrayList<Loop>();

	private List<Info> fields = new ArrayList<Info>();
	private Context context;
	private Map<String, Object> values;
	private Set<String> keys = new HashSet<String>();

	private String name;
	private String uniqueNames;
	
	private int count;
	private int min=0;
	private int max;
	
	private Before before;
	private After after;
	private BeforeSave beforeSave;
	private AfterSave afterSave;
	
	private BeforePersist beforePersist;
	private AfterPersist afterPersist;
	
	public Insert() {
	}
	public Insert(String name) {
		this();
		setName(name);
	}
	
	public void execute() throws Exception {
		if (getBefore() == null || getBefore().invoke(getContext(), this)) {
			
			getContext().getStack().push(this);
			
			int count = calcCount();
			
			for (int i=0; i<count; i++) {
				makeValues(i);
				
				if (getBeforeSave() == null || getBeforeSave().invoke(getContext(), this, getValues())) {
					
					if (keyRegister()) {
						persist();
						
						executeSubElements();
						
						if (getAfterSave() != null) { 
							getAfterSave().invoke(getContext(), this, getValues());
						}
					} else {
						log.info("DUPLICATION: Make an another turn");
						
						count++; // Make an another turn.
					}
				}
			}
			
			getContext().getStack().pop();
			
			if (getAfter() != null) {
				getAfter().invoke(getContext(), this);
			}
		}
	}
	
	private boolean keyRegister() {
		if (getUniqueNames() != null) {
			String uniqueKey = makeKey();
			
			log.info("KEY: "+uniqueKey);
			
			if (keys.contains(uniqueKey)) {
				return false;
			} else {
				keys.add(uniqueKey);
			}
		}
		
		return true;
	}
	
	protected String makeKey() {
		String[] names = getUniqueNames().split(",");
		StringBuffer key = new StringBuffer();
		
		for (String name : names) {
			key.append("/:");
			key.append(getValues().get(name));
		}
		
		return key.toString();
	}
	
	private int calcCount() {
		int count = getCount();
		
		if (count < 0) {
			int size = max - min;
			
			if (size > 0) {
				int nextInt = getContext().getRandom().nextInt(size);
				count = nextInt+min;
			} else {
				return min;
			}
			
		}
		
		return count;
	}
	
	private void executeSubElements() throws Exception {
		for (Insert insert : inserts) {
			insert.execute();
		}
		for (Loop loop : loops) {
			loop.execute();
		}
	}
	
	private void makeValues(int i) throws Exception {
		setValues(new HashMap<String, Object>());
		
		getContext().getValues().put(getName(), getValues());
		getValues().put("_position", i+1);
		
		Source source;
		
		for (Info info : fields) {
			if (info.type.equals(FIELD_TYPE_STATIC)) {
				getValues().put(info.field, info.value);
			} else if (info.type.equals(FIELD_TYPE_EXPRESSION)) {
				getValues().put(info.field, info.valueExpression.invoke(getContext(), this));
			} else if (info.type.equals(FIELD_TYPE_SCRIPT)) {
				ScriptEngineManager factory = new ScriptEngineManager();
				ScriptEngine engine = factory.getEngineByName("JavaScript");
				
				engine.put("insert", this);
				engine.put("values", getValues());
				engine.put("context", getContext());
				engine.put("sources", getContext().getSources());
				
				Object eval = engine.eval(info.source);
				
				getValues().put(info.field, eval);
			} else {
				source = getContext().getSources().get(info.source);
				
				getValues().put(info.field, source.pickOne(info.name, info.mode));
			}
		}
	}
	
	public Insert fieldExpression(String field, Value valueExpression) {
		Info info = new Info();
		
		info.field = field;
		info.valueExpression = valueExpression;
		info.type = FIELD_TYPE_EXPRESSION;
		
		fields.add(info);
		
		return this;
	}
	public Insert fieldScript(String field, String script) {
		Info info = new Info();
		
		info.field = field;
		info.source = script;
		info.type = FIELD_TYPE_SCRIPT;
		
		fields.add(info);
		
		return this;
	}
	public Insert fieldStatic(String field, Object value) {
		fields.add(new Info(field, value));
		
		return this;
	}
	public Insert field(String field, String source) {
		fields.add(new Info(field, source, MODE_BASE));
		
		return this;
	}
	public Insert field(String field, String source, String mode) {
		fields.add(new Info(field, source, mode));
		
		return this;
	}
	public Insert field(String field, String source, String name, String mode) {
		fields.add(new Info(field, source, name, mode));
		
		return this;
	}
	
	public Insert loop(Loop loop) {
		loop.setContext(getContext());
		
		loops.add(loop);
		
		return this;
	}
	
	public Insert insert(Insert insert) {
		inserts.add(insert);
		
		return this;
	}
	
	public String generateInsertSql() {
		StringBuffer names = new StringBuffer();
		StringBuffer values = new StringBuffer();
		
		int i;
		for (i=0; i<fields.size(); i++) {
			names.append(fields.get(i).field);
			if (i<fields.size()-1) names.append(", ");
		}
		
		for (i=0; i<fields.size(); i++) {
			values.append("#{"+fields.get(i).field+"}");
			if (i<fields.size()-1) values.append(", ");
		}
		
		return String.format(
			"INSERT INTO %s (%s) VALUES (%s)", 
			getName(), 
			names.toString(), 
			values.toString()
		);
	}
	
	public class Info {
		private String field, source, name, mode, type="NORMAL";
		private Object value;
		private Value valueExpression;
		
		public Info() {
		}
		public Info(String field, Object value) {
			this.field = field;
			this.value = value;
			this.type = FIELD_TYPE_STATIC;
		}
		public Info(String field, String source, String mode) {
			this.field = field;
			this.source = source;
			this.name = field;
			this.mode = mode;
		}
		public Info(String field, String source, String name, String mode) {
			this.field = field;
			this.source = source;
			this.name = name;
			this.mode = mode;
		}
	}
	
	public static interface Value {
		Object invoke(Context context, Insert insert) throws Exception; 
	}
	
	public static interface Before {
		boolean invoke(Context context, Insert insert) throws Exception; 
	}
	public static interface After {
		void invoke(Context context, Insert insert) throws Exception; 
	}
	public static interface BeforeSave {
		boolean invoke(Context context, Insert insert, Map<String, Object> values) throws Exception; 
	}
	public static interface AfterSave {
		void invoke(Context context, Insert insert, Map<String, Object> values) throws Exception; 
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
	public Context getContext() {
		return context;
	}
	public String getName() {
		return name;
	}
	public int getCount() {
		return count;
	}
	public Map<String, Object> getValues() {
		return values;
	}
	public BeforeSave getBeforeSave() {
		return beforeSave;
	}
	public AfterSave getAfterSave() {
		return afterSave;
	}
	public String getUniqueNames() {
		return uniqueNames;
	}
	public Set<String> getKeys() {
		return keys;
	}
	public Before getBefore() {
		return before;
	}
	public After getAfter() {
		return after;
	}
	public int getMin() {
		return min;
	}
	public int getMax() {
		return max;
	}
	public List<Loop> getLoops() {
		return loops;
	}
	
	public Insert setName(String name) {
		this.name = name;
		return this;
	}
	public Insert setCount(int count) {
		this.count = count;
		return this;
	}
	public Insert setBefore(Before before) {
		this.before = before;
		return this;
	}
	public Insert setAfter(After after) {
		this.after = after;
		return this;
	}
	public Insert setMin(int min) {
		this.min = min;
		return this;
	}
	public Insert setMax(int max) {
		this.max = max;
		setCount(-1);
		return this;
	}
	public Insert setUniqueNames(String uniqueNames) {
		this.uniqueNames = uniqueNames;
		return this;
	}
	public Insert setBeforeSave(BeforeSave beforeSave) {
		this.beforeSave = beforeSave;
		return this;
	}
	public Insert setAfterSave(AfterSave afterSave) {
		this.afterSave = afterSave;
		return this;
	}
	public Insert setValues(Map<String, Object> values) {
		this.values = values;
		return this;
	}
	public Insert setLoops(List<Loop> loops) {
		this.loops = loops;
		return this;
	}
	
	public static interface BeforePersist {
		boolean invoke(Context context, Insert insert, Map<String, Object> values, SqlSession session) throws Exception; 
	}
	public static interface AfterPersist {
		void invoke(Context context, Insert insert, Map<String, Object> values, SqlSession session) throws Exception; 
	}
	
	public BeforePersist getBeforePersist() {
		return beforePersist;
	}
	public AfterPersist getAfterPersist() {
		return afterPersist;
	}
	public Insert setAfterPersist(AfterPersist afterPersist) {
		this.afterPersist = afterPersist;
		return this;
	}
	public Insert setBeforePersist(BeforePersist beforePersist) {
		this.beforePersist = beforePersist;
		return this;
	}
	
	protected abstract void persist() throws Exception;
	
	@Override
	public String toString() {
		return "Insert [name=" + name + ", count=" + count + ", min=" + min
				+ ", max=" + max + "]";
	}
}
