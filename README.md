FluSad
======

You can create sample/test data with fluent like API.
Define sources and inserts, and the flusad will random pick from sources, and persist test data.
There are a lots of project out there for making test data, but i dicided to make it my own way.
"My way" is to make the config almost completely in java (except SQL's which is sit in MyBatis xml config files).
Pojects which are the same purpose: DTM Data Generator, Benerator.
I am using Eclipse Indigo SR2 JEE (M2E, DTP).

**Tiny Sample**

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
			
			.insert(new MyBatisInsert("Moragon")
				.setCount(13)
				
				.field("place", "Cities", "city", MapSource.MODE_RANDOM)
				.field("location", "Cities", "country", MapSource.MODE_LAST)
				.field("kindof", "KindOf")
				.field("age", "Age")
				.field("length", "Value", "Moragon.age", "base")
				.field("price", "Price", RangeSource.MODE_RANDOM)
				.fieldStatic("created", new Date(System.currentTimeMillis()))
				.field("customer", "RentalCustomer", "customer_id", "MapSource.MODE_RANDOM)
				.field("note", "Rss", "title", MapSource.MODE_RANDOM)
				.field("comment", "Rss", "description", MapSource.MODE_LAST)
				
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

**Links**

* [Complex sample code](https://github.com/godzzo/flusad/blob/master/src/test/java/org/godzzo/flusad/test/config/TestContext.java)
* [SQL Queries from Sakila](https://github.com/godzzo/flusad/blob/master/src/main/resources/mybatis/sakila/Mapper.xml)
* [SQL Inserts to Test Database](https://github.com/godzzo/flusad/blob/master/src/main/resources/mybatis/data/Mapper.xml)
* [Create Tables for Test Database](https://github.com/godzzo/flusad/blob/master/src/test/resources/sql/data/clip.sql)
* Config files which are need to be set [data](https://github.com/godzzo/flusad/blob/master/src/main/resources/mybatis/data/config.properties), [sakila](https://github.com/godzzo/flusad/blob/master/src/main/resources/mybatis/sakila/config.properties)

* [Home page](http://godzzo.github.com/flusad) - (empty yet)

**Sources**

* Jdbc (MyBatis)
* Xml (Rss)
* Csv
* Collection (List)
* Range (Integer/Double)

**Persist**

* Jdbc (MyBatis)
