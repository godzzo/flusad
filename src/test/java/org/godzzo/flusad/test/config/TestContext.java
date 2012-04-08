package org.godzzo.flusad.test.config;

import java.sql.Date;
import java.util.Arrays;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.godzzo.flusad.config.Context;
import org.godzzo.flusad.config.Insert;
import org.godzzo.flusad.config.Insert.AfterPersist;
import org.godzzo.flusad.config.Insert.AfterSave;
import org.godzzo.flusad.config.Insert.BeforePersist;
import org.godzzo.flusad.config.Insert.BeforeSave;
import org.godzzo.flusad.config.MyBatisInsert;
import org.godzzo.flusad.source.DynamicQuerySource;
import org.godzzo.flusad.source.IncrementSource;
import org.godzzo.flusad.source.JoinedSource;
import org.godzzo.flusad.source.ListSource;
import org.godzzo.flusad.source.MapSource;
import org.godzzo.flusad.source.QuerySource;
import org.godzzo.flusad.source.ParentSource;
import org.godzzo.flusad.source.RangeSource;
import org.godzzo.flusad.source.ValueSource;
import org.godzzo.flusad.source.XmlSource;
import org.junit.Test;

public class TestContext {
	@Test
	public void invoke() throws Exception {
		new Context()
			.source(new ParentSource())
			.source(new ValueSource())
			
			.source(new QuerySource("Cities").setConfig("sakila"))
			
			.source(new ListSource("KindOf").setItems(Arrays.asList(new String[] {"NORMAL", "SOFT", "KIND"})))
			.source(new ListSource("Age").setItems(Arrays.asList(new Integer[] {23, 44, 75})))
			
			.source(new RangeSource<Double>("Price", 17.81, 99.99))
			
			.source(new IncrementSource("Counter", 1000L))
			
			.source(new DynamicQuerySource("RentalCustomer").setConfig("sakila"))
			
			.source(new XmlSource("HupRss").setUri("http://feeds.feedburner.com/HUP"))
			.source(new XmlSource("IndexRss").setUri("http://index.hu/24ora/rss/"))
			.source(new XmlSource("OrigoRss").setUri("http://origo.hu/contentpartner/rss/hircentrum/origo.xml"))
			.source(new JoinedSource("Rss").setNames("HupRss,IndexRss,OrigoRss"))
			
			.insert(new MyBatisInsert("Moragon").setCount(13)
				.field("place", "Cities", "city", "random")
				.field("location", "Cities", "country", "last")
				.field("kindof", "KindOf")
				.field("age", "Age")
				.field("length", "Value", "Moragon.age", "base")
				.field("price", "Price", RangeSource.MODE_RANDOM)
				.fieldStatic("created", new Date(System.currentTimeMillis()))
				.field("customer", "RentalCustomer", "customer_id", "random")
				.field("note", "Rss", "title", MapSource.MODE_RANDOM)
				.field("comment", "Rss", "description", MapSource.MODE_LAST)
				.fieldScript("code", 
					"values.get('age') + '_' + values.get('price')"
					+ "+'_'+sources.get('Age').pickOne(null, null)"
				)
				
				.setBeforeSave(headBeforeSave())
				.setBeforePersist(headBeforePersist())
				
				.insert(new MyBatisInsert("Burdang").setMin(1).setMax(9)
					.field("parent_id", "Parent", "id", "base")
					.field("place", "Cities", "city", "random")
					.field("blue_pos", "Counter")
					
					.setBeforeSave(itemBeforeSave())
				)
				
				.setAfterPersist(headAfterPersist())
				.setAfterSave(headAfterSave())
			)
			
			.execute();
	}

	protected BeforeSave itemBeforeSave() {
		return new BeforeSave() {
			@Override
			public boolean invoke(Context context, Insert insert,
					Map<String, Object> values) throws Exception {
				System.out.println("-- -- Item - Before Save called -- --");
				return true;
			}
		};
	}

	protected AfterPersist headAfterPersist() {
		return new AfterPersist() {
			@Override
			public void invoke(Context context, Insert insert,
					Map<String, Object> values, SqlSession session) throws Exception {
				System.out.println("-- After Persist called --");
			}
		};
	}
	protected BeforePersist headBeforePersist() {
		return new BeforePersist() {
			@Override
			public boolean invoke(Context context, Insert insert,
					Map<String, Object> values, SqlSession session) throws Exception {
				System.out.println("-- Before Persist called --");
				return true;
			}
		};
	}

	protected AfterSave headAfterSave() {
		return new AfterSave() {
			
			@Override
			public void invoke(Context context, Insert insert,
					Map<String, Object> values) throws Exception {
				System.out.println("-- After Save called --");
			}
		};
	}
	protected BeforeSave headBeforeSave() {
		return new BeforeSave() {
			@Override
			public boolean invoke(Context context, Insert insert, Map<String, Object> values) throws Exception {
				System.out.println("-- Before Save called --");
				return true;
			}
		};
	}
}
