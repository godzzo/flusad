package org.godzzo.flusad.mybatis;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class Session {
	public static SqlSessionFactory configureByXml(String resource) throws Exception {
		Reader reader = Resources.getResourceAsReader(resource);
		return new SqlSessionFactoryBuilder().build(reader);
	}
}
