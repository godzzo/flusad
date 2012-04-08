package org.godzzo.flusad.test.common;

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.Test;

public class TestScripting {
	@Test
	public void hello() throws Exception {
		// create a script engine manager
		ScriptEngineManager factory = new ScriptEngineManager();
		// create a JavaScript engine
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		// evaluate JavaScript code from String
		engine.eval("print('Hello, World')");
	}
	
	@Test
	public void concat() throws Exception {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		
		engine.put("name", "Joe");
		engine.put("age", 22);
		
		Map<String, Object> values = new HashMap<String, Object>();
		
		values.put("type", "NORMAL");
		values.put("find", 223);
		
		engine.put("values", values);
		
		Object eval = engine.eval("name+' '+age+' / '+values.get('type')+':'+values.get('find')");
		
		System.out.println(eval);
	}
}
