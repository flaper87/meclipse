package org.mongodb.meclipse.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;

/**
 * 
 * @author Naoki Takezoe
 */
public class JSONUtils {
	
	public static BasicDBObject toDBObject(JSONObject jsonObj) throws JSONException {
		BasicDBObject dbObj = new BasicDBObject();
		@SuppressWarnings("unchecked")
		Iterator<String> keys = jsonObj.keys();
		while(keys.hasNext()){
			String key = keys.next();
			Object value = jsonObj.get(key);
			if(value instanceof JSONObject){
				dbObj.put(key, toDBObject((JSONObject) value));
			} else if(value instanceof JSONArray){
				dbObj.put(key, toArray((JSONArray) value));
			} else {
				dbObj.put(key, jsonObj.get(key));
			}
		}
		return dbObj;
	}
	
	public static Object[] toArray(JSONArray jsonArray) throws JSONException {
		List<Object> list = new ArrayList<Object>();
		for(int i=0; i < jsonArray.length(); i++){
			Object value = jsonArray.get(i);
			if(value instanceof JSONObject){
				list.add(toDBObject((JSONObject) value));
			} else if(value instanceof JSONArray){
				list.add(toArray((JSONArray) value));
			} else {
				list.add(value);
			}
		}
		return list.toArray();
	}
	
}
