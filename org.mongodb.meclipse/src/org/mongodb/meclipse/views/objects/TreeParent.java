package org.mongodb.meclipse.views.objects;

import java.util.ArrayList;

/**
 * @author Flavio [FlaPer87] Percoco Premoli
 * @author Joey Mink (walknwind@GitHub)
 */
public class TreeParent extends TreeObject {
	private ArrayList children;
	
	public TreeParent(String name) {
		super(name);
		children = new ArrayList();
	}
	
	public void clearChildren()
	{
		children.clear();
	}
	
	public void addChild(TreeObject child) {
		children.add(child);
		child.setParent(this);
	}
	public void removeChild(TreeObject child) {
		children.remove(child);
		child.setParent(null);
	}
	public TreeObject [] getChildren() {
		return (TreeObject [])children.toArray(new TreeObject[children.size()]);
	}
	public boolean hasChildren() {
		return true;
	}
}