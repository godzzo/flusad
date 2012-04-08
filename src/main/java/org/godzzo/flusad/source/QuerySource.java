package org.godzzo.flusad.source;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.godzzo.flusad.mybatis.Session;

public class QuerySource extends MapSource {
	@SuppressWarnings("unused")
	private Log log = LogFactory.getLog(QuerySource.class);
	
	private boolean load = true;
	private String config;

	public QuerySource() {
		super();
	}
	public QuerySource(String name) {
		this();
		
		setName(name);
	}
	
	@Override
	public void load() throws Exception {
		if (isLoad()) query();
	}
	
	public void query() throws Exception {
		query(null);
	}
	public void query(Map<String, Map<String, Object>> values) throws Exception {
		SqlSession session = Session.configureByXml(
			"mybatis/"+getConfig()+"/Configuration.xml"
		).openSession();
		
		try {
			List<Map<String, Object>> items;
			
			if (values == null) {
				items = session.selectList("Mapper.select" + getName());
			} else {
				items = session.selectList("Mapper.select" + getName(), values);
			}
			
			setItems(items);
		} finally {
			session.close();
		}
	}
	
	public String getConfig() {
		return config;
	}
	public QuerySource setConfig(String config) {
		this.config = config;
		return this;
	}
	public boolean isLoad() {
		return load;
	}
	public QuerySource setLoad(boolean load) {
		this.load = load;
		return this;
	}
	@Override
	public int getSize() {
		return getItems().size();
	}

	@Override
	public String toString() {
		return "MyBatisSource [configName=" + config + ", name=" + getName() + "]";
	}
}
