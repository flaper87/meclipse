package org.mongodb.meclipse.views.objects;

import static org.mongodb.meclipse.MeclipsePlugin.getCaption;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.ui.views.properties.IPropertySource;
import org.mongodb.meclipse.util.RequiredInputValidator;
import org.mongodb.meclipse.util.UIUtils;
import org.mongodb.meclipse.views.objects.properties.DatabasePropertySource;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoException;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public final class Database extends TreeParent {
	private DB db;
	private IAction createCollection;
	private IAction setProfileLevel0;
	private IAction setProfileLevel1;
	private IAction setProfileLevel2;

	public Database(String name) {
		super(name);
		makeActions();
	}

	private void makeActions() {
		createCollection = new CreateCollectionAction();
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
		return (Connection) super.getParent();
	}

	public DB getDB() {
		return db;
	}

	// @Override
	// public void doubleClickAction() {
	// Set<String> cols = db.getCollectionNames();
	//
	// Iterator<String> iterador = cols.iterator();
	//
	// clearChildren();
	// while (iterador.hasNext()) {
	// Collection newChild = new Collection(iterador.next());
	// newChild.setViewer(view);
	// this.addChild(newChild);
	// view.refreshViewerIfNecessary();
	// }
	// }

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
		manager.add(createCollection);
		manager.add(new Separator());
		manager.add(setProfileLevel0);
		manager.add(setProfileLevel1);
		manager.add(setProfileLevel2);
		manager.add(new Separator());
		super.fillContextMenu(manager);
	}

	private class SetProfileLevelAction extends Action {

		private int level;

		public SetProfileLevelAction(int level) {
			super(getCaption("database.profileLevel") + level);
			this.level = level;
		}

		public void run() {
			db.command(new BasicDBObject("profile", level));
		}
	}

	private class CreateCollectionAction extends Action {

		public CreateCollectionAction() {
			super("Create Collection");
		}

		public void run() {
			InputDialog dialog = new InputDialog(view.getSite().getShell(),
					getCaption("database.newCollection.title"),
					getCaption("database.newCollection.msg"), "",
					new RequiredInputValidator(
							getCaption("database.newCollection.errorMsg")));
			if (dialog.open() == InputDialog.OK) {
				try {
					db.createCollection(dialog.getValue(), new BasicDBObject());
				} catch (MongoException ex) {
					UIUtils.openErrorDialog(view.getSite().getShell(),
							ex.toString());
				}
				view.getViewer().refresh(Database.this);
			}
		}

	}

	@Override
	public TreeObject[] getChildren() {
		List<Collection> children = new ArrayList<Collection>();
		for (String name : db.getCollectionNames()) {
			Collection collection = new Collection(name);
			collection.setParent(this);
			collection.setViewer(view);
			children.add(collection);
		}
		return children.toArray(new TreeObject[children.size()]);
	}

}