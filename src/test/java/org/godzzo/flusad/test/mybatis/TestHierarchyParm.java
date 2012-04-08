package org.godzzo.flusad.test.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.godzzo.flusad.mybatis.Session;
import org.junit.Test;

public class TestHierarchyParm {
	@Test
	public void invoke() throws Exception {
		Map<String, Map<String, Object>> map = new HashMap<String, Map<String,Object>>();
		
		map.put("main", new HashMap<String, Object>());
		
		map.get("main").put("name", "Joe");
		map.get("main").put("age", 22);
		map.get("main").put("male", true);
		map.get("main").put("max_id", 99);
		
		SqlSession session = Session.configureByXml("mybatis/sakila/Configuration.xml").openSession();

		try {
			List<Map<String, ?>> items = session.selectList("Mapper.testHierarchyParm", map);
			
			System.out.println("Test Cities: "+items.toString());
			System.out.println("Test Cities Size(): "+items.size());
			
			map.get("main").put("max_id", 10);
			
			items = session.selectList("Mapper.testHierarchyParm", map);
			
			System.out.println("Test Cities: "+items.toString());
			System.out.println("Test Cities Size(): "+items.size());
		} finally {
			session.close();
		}
		
	}
}
