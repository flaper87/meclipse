package org.mongo.meclipse.views.objects;

import java.util.Iterator;
import java.util.Set;

import com.mongodb.*;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public final class Database extends TreeParent {
	private DB db;
	
	public Database(String name) {
		super(name);
	}
	
	@Override
	public void setParent(TreeParent parent) {
		super.setParent(parent);
		db = this.getParent().getConnection().getDB(this.getName());
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

		while (iterador.hasNext()) {
			Collection newChild = new Collection(iterador.next());
			newChild.setViewer(view);
			this.addChild(newChild);
			view.refresh(false);
		}
	}

}