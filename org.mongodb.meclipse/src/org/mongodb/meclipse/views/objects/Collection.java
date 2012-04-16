package org.mongodb.meclipse.views.objects;

import java.io.File;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.handlers.IHandlerService;
import org.json.JSONObject;
import org.mongodb.meclipse.util.IOUtils;
import org.mongodb.meclipse.util.JSONUtils;
import org.mongodb.meclipse.util.RequiredInputValidator;
import org.mongodb.meclipse.util.UIUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public final class Collection extends CollectionBase
implements IAdaptable {
	private DBCollection col;
	private IAction rename;
	private IAction delete;
	private IAction insert;
	private CollectionType type=null;
	
	
    public enum CollectionType {
        SYSINDEX,
        COLLECTION
    }
    
	public Collection(String name) {
		super(name);
		makeActions();
	}
	
	private void makeActions() {
		insert = new Action("Insert Document"){
			@Override
			public void run(){
				FileDialog dialog = new FileDialog(view.getSite().getShell(), SWT.OPEN);
				dialog.setFilterExtensions(new String[]{"*.json"});
				String result = dialog.open();
				if(result != null){
					try {
						String jsonText = IOUtils.readFile(new File(result));
						JSONObject jsonObj = new JSONObject(jsonText);
						col.insert(JSONUtils.toDBObject(jsonObj));
					} catch(Exception ex){
						UIUtils.openErrorDialog(view.getSite().getShell(), ex.toString());
					}
				}
			}
		};
		
		rename = new Action("Rename Collection"){
			@Override
			public void run() {
				InputDialog dialog = new InputDialog(view.getSite().getShell(), 
						"Rename Collection", "Collection Name:", col.getName(), 
						new RequiredInputValidator("Please input the collection name."));
				if(dialog.open() == InputDialog.OK){
					try {
						col.rename(dialog.getValue());
					} catch(MongoException ex){
						UIUtils.openErrorDialog(view.getSite().getShell(), ex.toString());
					}
					view.getViewer().refresh(getParent());
				}
			}
		};
		
		delete = new Action("Delete Collection"){
			@Override
			public void run() {
				if(MessageDialog.openConfirm(view.getSite().getShell(), "Confirm", 
						String.format("Are you sure you want to delete collection '%s'?", col.getName()))){
					col.drop();
					view.getViewer().refresh(getParent());
				}
			}
		};
	}
	
	@Override
	public void fillContextMenu(IMenuManager manager) {
		manager.add(insert);
		manager.add(rename);
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
	
	public CollectionType getType() {
	    if (type == null) {
	        if (col.getName().equalsIgnoreCase( "system.indexes" ) ) {
	            type = CollectionType.SYSINDEX;
	        } else  {
	            type = CollectionType.COLLECTION;
	        }
	    }
	    return type;
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
