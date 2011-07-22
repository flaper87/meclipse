package org.mongodb.meclipse.views.objects;

public class Filter extends CollectionBase {

	private String queryStr;

	public Filter(String name, String queryStr) {
		super(name);
		this.queryStr = queryStr;
	}

	public String getQueryStr() {
		return queryStr;
	}

}
