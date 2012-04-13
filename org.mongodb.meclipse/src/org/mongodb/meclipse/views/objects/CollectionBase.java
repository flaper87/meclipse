package org.mongodb.meclipse.views.objects;

import java.util.Set;

import org.eclipse.ui.views.properties.IPropertySource;
import org.mongodb.meclipse.MeclipsePlugin;
import org.mongodb.meclipse.views.FilterPlacement;
import org.mongodb.meclipse.views.objects.properties.CollectionPropertySource;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public abstract class CollectionBase extends TreeParent {

	public CollectionBase(String name) {
		super(name);
	}

	@Override
	public TreeObject [] getChildren() {
		Set<Filter> filters = MeclipsePlugin.getDefault().getFilters(new FilterPlacement(this));
		if(filters == null){
			return new TreeObject[0];
		}
		return (TreeObject [])filters.toArray(new TreeObject[filters.size()]);
	}
	
	public abstract DBObject getQuery();
	
	public abstract DBCollection getCollection();
	
    /**
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
	@Override
	@SuppressWarnings("rawtypes")
    public Object getAdapter(Class adapter) {
		 if (adapter == IPropertySource.class) {
			return new CollectionPropertySource(this);
		 }
       return null;
    }
}
