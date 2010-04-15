package org.mongo.meclipse.views.objects;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.*;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.mongo.meclipse.Images;
import org.mongo.meclipse.wizards.ConnectionWizard;

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
		//drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

}
