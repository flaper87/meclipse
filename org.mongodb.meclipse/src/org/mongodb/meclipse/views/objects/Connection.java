package org.mongodb.meclipse.views.objects;

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
	
	public Connection(String name) {
		super(name);
		makeActions();
	}
	
	private void makeActions() {
		final Connection conn = this;
		delete = new Action() {	
			public void run() {
				if (view != null) {
					MeclipsePlugin.getDefault().markMongoDeleted(conn.getName());
					MeclipsePlugin.getDefault().removeMongo( conn.getName() );
					view.refreshMe();
				}
			}
		};
		
		delete.setText("Delete");
		delete.setToolTipText("Delete Connection");
	}
	
//	public TreeObject [] getChildren() {
//		loadDatabases();
//		return super.getChildren();
//	}
	
	/**
	 * In the style of lazy-loading, this is where we actually initiate the connection
	 * to a MongoDB instance - and this is where a user would 1st request to see data
	 * obtained via the connection.
	 * @return
	 */
	public Mongo getMongo() {
		MongoInstance mongoInstance = MeclipsePlugin.getDefault().getMongoInstance(this.getName());
		if (mongoInstance.getMongo() == null)
		{
			Mongo mongo;
			try {
				mongo = new Mongo(mongoInstance.getHost(), mongoInstance.getPort());
				mongoInstance.setMongo(mongo); // add the active Mongo instance to the plug-in's state
				return mongo;
			} catch (UnknownHostException e) {
				this.showMessage(e.getMessage());
			} catch (MongoException e) {
				this.showMessage(e.getMessage());				
			}
			return null;
		}
		else return mongoInstance.getMongo();
	}
	
	@Override
	public TreeObject[] getChildren(){
		List<Database> children = new ArrayList<Database>();
		for(String name: getMongo().getDatabaseNames()){
			Database database = new Database(name);
			database.setParent(this);
			database.setViewer(view);
			children.add(database);
		}
		return children.toArray(new TreeObject[children.size()]);
	}
	
	/*
	@Override
	public void doubleClickAction() {
		if (getChildren().length == 0) {
			loadDatabases();
		}
	}
	*/
	
	@Override
	public void fillContextMenu(IMenuManager manager) {
		manager.add(delete);
		manager.add(new Separator());
		super.fillContextMenu(manager);
	}

	public DBObject getServerStatus()
	{
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
		MessageDialog.openInformation(this.view.getViewer().getControl().getShell(),
				"Meclipse View", message);
	}
}
