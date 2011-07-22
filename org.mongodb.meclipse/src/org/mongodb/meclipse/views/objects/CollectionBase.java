package org.mongodb.meclipse.views.objects;

import java.util.Set;

import org.mongodb.meclipse.MeclipsePlugin;
import org.mongodb.meclipse.views.FilterPlacement;

public class CollectionBase extends TreeParent {

	public CollectionBase(String name) {
		super(name);
	}

	@Override
	public boolean hasChildren()
	{
		Set<Filter> filters = MeclipsePlugin.getDefault().getFilters(new FilterPlacement(this));
		if (filters == null)
			return false;
		if (filters.size() == 0)
			return false;

		return true;
	}
	
	@Override
	public TreeObject [] getChildren() {
		Set<Filter> filters = MeclipsePlugin.getDefault().getFilters(new FilterPlacement(this));
		
		return (TreeObject [])filters.toArray(new TreeObject[filters.size()]);
	}
}
