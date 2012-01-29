package org.mongodb.meclipse.views.objects;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.handlers.IHandlerService;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public final class Collection extends CollectionBase
implements IAdaptable {
	private DBCollection col;
	private IAction delete;
	
	public Collection(String name) {
		super(name);
		makeActions();
	}
	
	private void makeActions() {
		delete = new Action("Delete Collection"){
			@Override
			public void run() {
				col.drop();
				view.getViewer().refresh(getParent());
			}
		};
	}
	
	@Override
	public void fillContextMenu(IMenuManager manager) {
		manager.add(delete);
		manager.add(new Separator());
		super.fillContextMenu(manager);
	}



	@Override
	public void setParent(TreeParent parent) {
		super.setParent(parent);
		col = ((Database)this.getParent()).getDB().getCollection(this.getName());
	}
	
	@Override
	public DBCollection getCollection() {
		return col;
	}

	@Override
	public void doubleClickAction() {
		IHandlerService handlerService = (IHandlerService) view.getSite().getService(IHandlerService.class);
		try {
			handlerService.executeCommand(
					"org.mongodb.meclipse.editors.handlers.CallEditor", null);
		} catch (Exception ex) {
			System.out.println(ex.toString());
//			throw new RuntimeException(
//					"org.mongodb.meclipse.editors.handlers.CallEditor not found");
		}
	}

	@Override
	public DBObject getQuery() {
		return new BasicDBObject();
	}
}
