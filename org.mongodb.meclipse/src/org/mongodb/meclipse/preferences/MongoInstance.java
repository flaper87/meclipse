package org.mongodb.meclipse.preferences;

import com.mongodb.Mongo;

/**
 * 
 * @author Joey Mink, ExoAnalyticSolutions
 * 
 */
public class MongoInstance {
	private String name;
	private String host;
	private Integer port;
	private String username;
	private String password;
	private Mongo mongo;

	public MongoInstance(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Mongo getMongo() {
		return mongo;
	}
	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
