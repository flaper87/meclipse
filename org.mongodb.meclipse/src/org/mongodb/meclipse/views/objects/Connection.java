package org.mongodb.meclipse.views.objects;

import static org.mongodb.meclipse.MeclipsePlugin.getCaption;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.views.properties.IPropertySource;
import org.mongodb.meclipse.MeclipsePlugin;
import org.mongodb.meclipse.preferences.MongoInstance;
import org.mongodb.meclipse.views.objects.properties.ConnectionPropertySource;

import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public final class Connection extends TreeParent {
	private Action delete;
	private boolean isDown = false;

	public Connection(String name) {
		super(name);
		makeActions();
	}

	private void makeActions() {
		final Connection conn = this;
		delete = new Action() {
			public void run() {
				if (view != null) {
					MeclipsePlugin.getDefault().removeMongo(conn.getName());
					view.refreshMe();
				}
			}
		};

		delete.setText(getCaption("connection.delete"));
		delete.setToolTipText(getCaption("connection.tooltip.delete"));
	}

	// public TreeObject [] getChildren() {
	// loadDatabases();
	// return super.getChildren();
	// }

	/**
	 * In the style of lazy-loading, this is where we actually initiate the
	 * connection to a MongoDB instance - and this is where a user would 1st
	 * request to see data obtained via the connection.
	 * 
	 * @return
	 */
	public Mongo getMongo() {
		MongoInstance mongoInstance = MeclipsePlugin.getDefault()
				.getMongoInstance(this.getName());
		Exception ex;
		if (mongoInstance.getMongo() == null) {
			Mongo mongo;
			try {
				mongo = new Mongo(mongoInstance.getHost(),
						mongoInstance.getPort());
				// we've got some Node admin login credentials so authenticate
				// to gain access.
				if (null != mongoInstance.getUsername()) {
					if (!mongo.getDB("admin").authenticate(
							mongoInstance.getUsername(),
							mongoInstance.getPassword().toCharArray())) {
						this.showMessage(getCaption("connection.error.authAdmin"));
					}
				}
				mongo.getDatabaseNames();
				mongoInstance.setMongo(mongo); // add the active Mongo instance
												// to the plug-in's state
				isDown = false;
				return mongo;
				/* catch some possible exceptions */
			} catch (MongoException e) {
				ex = e;
			} catch (UnknownHostException e) {
				ex = e;
			}
			if (!isDown) {
				this.showMessage(String.format(
						getCaption("connection.error.conn"), this.getName(),
						mongoInstance.getHost(), ex));
				isDown = true;
			}
			return null;
		} else {
			return mongoInstance.getMongo();
		}
	}

	@Override
	public TreeObject[] getChildren() {
		List<Database> children = new ArrayList<Database>();
		Mongo mongo = getMongo();
		if (mongo != null) {
			try {
				for (String name : mongo.getDatabaseNames()) {
					Database database = new Database(name);
					database.setParent(this);
					database.setViewer(view);
					children.add(database);
				}
			} catch (Exception e) {
				MeclipsePlugin.getDefault().getMongoInstance(this.getName())
						.setMongo(null);
				e.printStackTrace();
			}
		}
		return children.toArray(new TreeObject[children.size()]);
	}

	/*
	 * @Override public void doubleClickAction() { if (getChildren().length ==
	 * 0) { loadDatabases(); } }
	 */

	@Override
	public void fillContextMenu(IMenuManager manager) {
		manager.add(delete);
		manager.add(new Separator());
		super.fillContextMenu(manager);
	}

	public DBObject getServerStatus() {
		String firstDbName = getMongo().getDatabaseNames().get(0);
		DBObject status = getMongo().getDB(firstDbName).command("serverStatus");
		return status;
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySource.class) {
			return new ConnectionPropertySource(this);
		}
		return null;
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(this.view.getViewer().getControl()
				.getShell(), getCaption("connection.title.view"), message);
	}
}
