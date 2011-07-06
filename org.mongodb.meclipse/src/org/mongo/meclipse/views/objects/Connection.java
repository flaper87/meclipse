package org.mongo.meclipse.views.objects;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.*;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.views.properties.IPropertySource;
import org.mongo.meclipse.views.objects.properties.ConnectionPropertySource;

import com.mongodb.*;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public final class Connection extends TreeParent {
	private String host;
	private int port;
	private String username;
	private String password;
	private Mongo connection;
	
	private Action connect;
	private Action delete;
	
	private Connection(String name) {
		super(name);
	}
	
	private void makeActions() {
		final Connection conn = this;
		connect = new Action() {

			public void run() {
				loadDatabases();
				if (view != null) {
					view.refresh(false);
				}
			}
		};

		connect.setText("Connect");
		connect.setToolTipText("Start Connection");
		//connect.setImageDescriptor(Images.getDescriptor(Images.PageCommit));
		
		delete = new Action() {
			
			public void run() {
				if (view != null) {
					((ViewContentProvider)view.getViewer().getContentProvider()).getRoot().removeChild(conn);
					view.refresh(true);
				}
			}
		};
		
		delete.setText("Delete");
		delete.setToolTipText("Delete Connection");
	}
	
	public Connection(String name, String addr, int portN) {
		this(name);
		
		try {
			host = addr;
			port = portN;
			connection = new Mongo(host, port);
			makeActions();
		} catch (UnknownHostException exc) {
			System.out.println("Host not found");
		}
	}
	
	public Mongo getConnection() {
		return connection;
	}
	
	public String getHost() {
		return host;
	}
	public int getPort() {
		return port;
	}
	
	public boolean validate() {
		try {
			connection.getDatabaseNames();
			return true;
		} catch (Exception exc) {
			return false;
		}
	}
	
	public void loadDatabases() {
		List<String> dbs = connection.getDatabaseNames();
		Iterator<String> iterador = dbs.iterator();
		while (iterador.hasNext()) {
			Database newChild = new Database(iterador.next());
			newChild.setViewer(view);
			addChild(newChild);
			newChild.doubleClickAction(); // show us the expansion arrow immediately if the db has collections
		}
	}
	
	@Override
	public void doubleClickAction() {
		if (getChildren().length == 0) {
			loadDatabases();
		}
	}
	
	@Override
	public void fillContextMenu(IMenuManager manager) {
		manager.add(connect);
		manager.add(delete);
		manager.add(new Separator());
		super.fillContextMenu(manager);
	}

	public DBObject getServerStatus()
	{
		String firstDbName = connection.getDatabaseNames().get(0);
		DBObject status = connection.getDB(firstDbName).command("serverStatus");
		return status;
	}
	
    /**
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class adapter) {
		 if (adapter == IPropertySource.class) {
			return new ConnectionPropertySource(this);
		 }
       return null;
    }
}
