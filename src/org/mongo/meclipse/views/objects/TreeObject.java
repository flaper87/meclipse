package org.mongo.meclipse.views.objects;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.mongo.meclipse.views.MeclipseView;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 */
public class TreeObject implements IAdaptable {
	private String name;
	public MeclipseView view;
	private TreeParent parent;

	public TreeObject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setParent(TreeParent parent) {
		this.parent = parent;
	}

	public TreeParent getParent() {
		return parent;
	}

	public String toString() {
		return getName();
	}

	public Object getAdapter(Class key) {
		return null;
	}
	
	public void doubleClickAction() {
	}
	
	public void fillContextMenu(IMenuManager manager) {
	}
	
	public void setViewer(MeclipseView view) {
		this.view = view;
	}
}