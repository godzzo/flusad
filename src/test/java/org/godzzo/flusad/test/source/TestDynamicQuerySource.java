package org.godzzo.flusad.test.source;

import java.util.HashMap;
import java.util.Map;

import org.godzzo.flusad.config.Context;
import org.godzzo.flusad.source.DynamicQuerySource;
import org.godzzo.flusad.source.MapSource;
import org.junit.Test;

public class TestDynamicQuerySource {
	// TODO: IT CAN BE STEPABLE BY THE QuerySource
	
	@Test
	public void randomPicks() throws Exception {
		DynamicQuerySource source = new DynamicQuerySource("RentalCustomer")
			.setConfig("sakila")
		;
		
		Context context = new Context().source(source);
		
		Map<String, Object> map = new HashMap<String, Object>();
		context.getValues().put("Moragon", map);
		
		for (int i=0; i<10; i++) {
			print(i+". ITEM");
			map.put("_position", i);
			print("- Value: "+source.pickOne("customer_id", MapSource.MODE_RANDOM));
			print("- Value: "+source.pickOne("staff_id", MapSource.MODE_LAST));
		}
	}
	
	private void print(String text) {
		System.out.println(text);
	}
}
