package org.mongodb.meclipse.views.objects;

import com.mongodb.*;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.views.properties.IPropertySource;
import org.mongodb.meclipse.views.objects.properties.CollectionPropertySource;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public final class Collection extends CollectionBase
implements IAdaptable {

	private DBCollection col;
	
	public Collection(String name) {
		super(name);
		// TODO Auto-generated method stub
	}
	
	@Override
	public void setParent(TreeParent parent) {
		super.setParent(parent);
		col = ((Database)this.getParent()).getDB().getCollection(this.getName());
	}
	
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
	
    /**
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class adapter) {
		 if (adapter == IPropertySource.class) {
			return new CollectionPropertySource(this);
		 }
       return null;
    }
}
