flusad
======

You can create sample/test data with fluent like API.
Define sources and inserts, and the flusad will random pick from sources, and persist test data.

Complex sample code: [TestContext](https://github.com/godzzo/flusad/blob/master/src/test/java/org/godzzo/flusad/test/config/TestContext.java)

**Sources:**

* Jdbc (MyBatis)
* Xml (Rss)
* Csv
* Collection (List)
* Range

**Persist:**

* Jdbc (MyBatis)
