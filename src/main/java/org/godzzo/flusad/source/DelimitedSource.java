package org.godzzo.flusad.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DelimitedSource extends MapSource {
	private Log log = LogFactory.getLog(getClass());
	
	private String path;
	private String delimiter;
	private String names;
	
	public DelimitedSource() {
		super();
	}
	public DelimitedSource(String name) {
		this();
		setName(name);
	}
	
	@Override
	public void load() throws Exception {
		setItems(new ArrayList<Map<String, Object>>());
		
		FileInputStream stream = new FileInputStream(path);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line;
		String[] data;
		String[] names = getNames().split(",");
		
		while ((line = reader.readLine()) != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			
			log.debug("LINE: "+line);
			
			data = line.split(getDelimiter());
			
			for (int i=0; i<names.length; i++) {
				map.put(names[i], data[i]);
				log.debug(String.format("%d. %s = %s", i, names[i], data[i]));
			}
			
			log.debug("MAP: "+map.toString());
			
			getItems().add(map);
		}
	}

	public String getPath() {
		return path;
	}
	public DelimitedSource setPath(String path) {
		this.path = path;
		return this;
	}
	public String getDelimiter() {
		return delimiter;
	}
	public DelimitedSource setDelimiter(String delimiter) {
		this.delimiter = delimiter;
		return this;
	}
	public String getNames() {
		return names;
	}
	public DelimitedSource setNames(String names) {
		this.names = names;
		return this;
	}
	@Override
	public int getSize() {
		return getItems().size();
	}
}
