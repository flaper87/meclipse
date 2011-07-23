package org.mongodb.meclipse.views.objects;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class Filter extends CollectionBase {

	private String queryStr;

	public Filter(String name, String queryStr) {
		super(name);
		this.queryStr = queryStr;
	}

	@Override
	public DBCollection getCollection() {
		TreeParent treeObj = getParent();
		while (!(treeObj instanceof Collection))
			treeObj = treeObj.getParent();
			
		Collection coll = (Collection)treeObj;
		return coll.getCollection();
	}

	private DBObject mergeJson(JSONObject json, DBObject dbObj) throws JSONException
	{
		Iterator jsonKeyIter = json.keys();
		while (jsonKeyIter.hasNext())
		{
			String jsonKey = (String)jsonKeyIter.next();
			Object jsonValue = json.get(jsonKey);
			if (jsonValue instanceof JSONObject)
				dbObj.put(jsonKey, mergeJson((JSONObject)jsonValue, new BasicDBObject()));
			dbObj.put(jsonKey, jsonValue);
		}
		
		return dbObj;
	}
	
	@Override
	public DBObject getQuery() {
		DBObject dbObj = ((CollectionBase)getParent()).getQuery();

		try {
			dbObj = mergeJson(new JSONObject(new JSONTokener(queryStr)), dbObj);
			return dbObj;
		} catch (JSONException e) {
			// TODO: handle this better!
			System.out.println("Failed to parse query str.");
		}
		return null;
	}

}
