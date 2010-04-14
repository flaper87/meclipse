package org.mongo.meclipse.views.objects;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.mongo.meclipse.views.MeclipseView;

/*
 * The content provider class is responsible for
 * providing objects to the view. It can wrap
 * existing objects in adapters or simply return
 * objects as-is. These objects may be sensitive
 * to the current input of the view, or ignore
 * it and always show the same content 
 * (like Task List, for example).
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