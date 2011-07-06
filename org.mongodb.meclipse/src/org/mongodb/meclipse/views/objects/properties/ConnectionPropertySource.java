package org.mongodb.meclipse.views.objects.properties;

import org.mongodb.meclipse.views.objects.Connection;

public class ConnectionPropertySource extends DBObjectPropertySource {

	private Connection conn;

	public ConnectionPropertySource(Connection conn)
	{
		super(conn.getServerStatus());
	}
}
