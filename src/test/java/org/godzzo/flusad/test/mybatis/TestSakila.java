package org.godzzo.flusad.test.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.godzzo.flusad.mybatis.Session;
import org.junit.Test;

public class TestSakila {
	@Test
	public void selectCities() throws Exception {
		SqlSession session = Session.configureByXml("mybatis/sakila/Configuration.xml").openSession();

		try {
			List<Map<String, ?>> items = session.selectList("Mapper.selectCities");
			
			System.out.println("Cities: "+items.toString());
		} finally {
			session.close();
		}
	}
	
	@Test
	public void selectCountries() throws Exception {
		SqlSession session = Session.configureByXml("mybatis/sakila/Configuration.xml").openSession();

		try {
			List<Map<String, ?>> items = session.selectList("Mapper.selectCountries");
			
			System.out.println("Countries: "+items.toString());
		} finally {
			session.close();
		}
	}
}
