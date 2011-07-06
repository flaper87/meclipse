package org.mongodb.meclipse.preferences;

import java.net.URL;

/**
 * 
 * @author Joey Mink, ExoAnalyticSolutions
 *
 */
public class SavedServer {
	private String name;
	private String host;
	private Integer port;
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
	
}
