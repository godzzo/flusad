package org.godzzo.flusad.test.source;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.godzzo.flusad.config.Context;
import org.godzzo.flusad.source.MapSource;
import org.godzzo.flusad.source.QuerySource;
import org.junit.Test;

public class TestQuerySource {
	// TODO: IT CAN BE STEPABLE!
	
	private Log log = LogFactory.getLog(getClass());
	
	@Test
	public void base() throws Exception {
		Context context = new Context();
		QuerySource source = new QuerySource("Cities");
		
		source.setConfig("sakila");
		source.setStrict(true);
		
		context.source(source);
		
		source.load();

		// city_id, city, country_id
		for (int i=0; i<10; i++) {
			log.info("city, random: "+source.pickOne("city", MapSource.MODE_RANDOM));
			log.info("city_id, last: "+source.pickOne("city_id", MapSource.MODE_LAST));
			log.info("country_id, last: "+source.pickOne("country_id", MapSource.MODE_LAST));
		}
	}
}
