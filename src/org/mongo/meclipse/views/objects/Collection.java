package org.mongo.meclipse.views.objects;

import com.mongodb.*;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySource;
import org.mongo.meclipse.views.objects.properties.CollectionPropertySource;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public final class Collection extends TreeObject
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
					"org.mongo.meclipse.editors.handlers.CallEditor", null);
		} catch (Exception ex) {
			System.out.println(ex.toString());
//			throw new RuntimeException(
//					"org.mongo.meclipse.editors.handlers.CallEditor not found");
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
