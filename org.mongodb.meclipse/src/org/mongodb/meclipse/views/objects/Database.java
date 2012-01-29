package org.mongodb.meclipse.views.objects;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.views.properties.IPropertySource;
import org.mongodb.meclipse.views.objects.properties.DatabasePropertySource;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public final class Database extends TreeParent {
	private DB db;
	private IAction setProfileLevel0;
	private IAction setProfileLevel1;
	private IAction setProfileLevel2;
	
	public Database(String name) {
		super(name);
		makeActions();
	}
	
	private void makeActions() {
		setProfileLevel0 = new SetProfileLevelAction(0);
		setProfileLevel1 = new SetProfileLevelAction(1);
		setProfileLevel2 = new SetProfileLevelAction(2);
	}
	
	@Override
	public void setParent(TreeParent parent) {
		super.setParent(parent);
		db = this.getParent().getMongo().getDB(this.getName());
	}
	
	@Override
	public Connection getParent() {
		return (Connection)super.getParent();
	}
	
	public DB getDB() {
		return db;
	}
	
	@Override
	public void doubleClickAction() {
		Set<String> cols = db.getCollectionNames();
		
		Iterator<String> iterador = cols.iterator();

		clearChildren();
		while (iterador.hasNext()) {
			Collection newChild = new Collection(iterador.next());
			newChild.setViewer(view);
			this.addChild(newChild);
			view.refreshViewerIfNecessary();
		}
	}

    /**
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
	@Override
	@SuppressWarnings("rawtypes")
    public Object getAdapter(Class adapter) {
		 if (adapter == IPropertySource.class) {
			return new DatabasePropertySource(this);
		 }
       return null;
    }
    
	@Override
	public void fillContextMenu(IMenuManager manager) {
		manager.add(new Separator());
		manager.add(setProfileLevel0);
		manager.add(setProfileLevel1);
		manager.add(setProfileLevel2);
		manager.add(new Separator());
		super.fillContextMenu(manager);
	}
	
	private class SetProfileLevelAction extends Action {
		
		private int level;
		
		public SetProfileLevelAction(int level){
			super("Set Profile Level : " + level);
			this.level = level;
		}
		
		public void run() {
			db.command( new BasicDBObject( "profile" , level ));
		}
	}
}