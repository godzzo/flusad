package org.godzzo.flusad.config;

import org.apache.ibatis.session.SqlSession;
import org.godzzo.flusad.mybatis.Session;

public class MyBatisInsert extends Insert {
	
	public MyBatisInsert() {
		super();
	}
	public MyBatisInsert(String name) {
		this();
		setName(name);
	}
	
	@Override
	protected void persist() throws Exception {
		SqlSession session = Session.configureByXml(
			"mybatis/"+getContext().getTarget()+"/Configuration.xml"
		).openSession();
		
		try {
			if (getBeforePersist() == null || getBeforePersist().invoke(getContext(), this, getValues(), session)) {
				try {
					session.insert("Mapper.insert" + getName(), getValues());
				} catch (Exception e) {
					log.error("Mapper.insert" + getName() + ", Values: "+getValues().toString(), e);
					throw e;
				}
				
				session.commit();
				
				log.info("PERSISTED: "+getName()+" / "+getValues());
				
				if (getAfterPersist() != null) {
					getAfterPersist().invoke(getContext(), this, getValues(), session);
				}
			}
		} finally {
			session.close();
		}
	}
}
