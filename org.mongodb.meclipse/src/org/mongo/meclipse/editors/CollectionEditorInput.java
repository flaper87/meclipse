package org.mongo.meclipse.editors;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.jface.resource.ImageDescriptor;
import org.mongo.meclipse.views.objects.Collection;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public class CollectionEditorInput implements IEditorInput {

	private final Collection person;

	public CollectionEditorInput(Collection person) {
		this.person = person;
	}

	public Collection getObject() {
		return person;
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
		return person.toString();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return person.toString();
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		}
		if (obj instanceof CollectionEditorInput) {
			return person.equals(((CollectionEditorInput) obj).getObject());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return person.hashCode();
	}
}