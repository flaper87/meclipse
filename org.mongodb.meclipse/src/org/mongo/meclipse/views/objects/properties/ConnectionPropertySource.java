package org.mongo.meclipse.views.objects.properties;

import org.mongo.meclipse.views.objects.Connection;

public class ConnectionPropertySource extends DBObjectPropertySource {

	private Connection conn;

	public ConnectionPropertySource(Connection conn)
	{
		super(conn.getServerStatus());
	}
}
