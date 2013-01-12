package org.mongodb.meclipse.editors;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.jface.resource.ImageDescriptor;
import org.mongodb.meclipse.views.objects.Collection;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public class CollectionEditorInput implements IEditorInput {

	private final Collection collection;

	public CollectionEditorInput(Collection collection) {
		this.collection = collection;
	}

	public Collection getObject() {
		return collection;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return collection.toString();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return collection.toString();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		}
		if (obj instanceof CollectionEditorInput) {
			return collection.equals(((CollectionEditorInput) obj).getObject());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return collection.hashCode();
	}
}